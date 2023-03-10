package br.com.github.victorhugoof.api.cep.service;

import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.api.cep.integration.ApiCepService;
import br.com.github.victorhugoof.api.cep.integration.CepApi;
import br.com.github.victorhugoof.api.cep.integration.CidadeApi;
import br.com.github.victorhugoof.api.cep.mapper.CepCompletoDTOConverter;
import br.com.github.victorhugoof.api.cep.model.Cep;
import br.com.github.victorhugoof.api.cep.model.CepCompleto;
import br.com.github.victorhugoof.api.cep.model.CepError;
import br.com.github.victorhugoof.api.cep.model.Cidade;
import br.com.github.victorhugoof.api.cep.model.SearchCepInput;
import br.com.github.victorhugoof.api.cep.model.SearchGeoInput;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import static java.util.Objects.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class SearchCepServiceImpl implements SearchCepService {

    private final CepService cepService;
    private final CepErrorService cepErrorService;
    private final CidadeService cidadeService;
    private final CepCompletoDTOConverter cepCompletoDTOConverter;
    private final ApiCepService apiCepService;
    private final Validator validator;

    @Override
    public Mono<CepCompleto> searchCep(SearchCepInput input) {
        var result = validator.validate(input);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }

        if (input.update()) {
            return searchAndPersist(input.cep(), true)
                    .flatMap(this::toCepCompleto);
        }

        return cepService.findByCep(input.cep())
                .switchIfEmpty(Mono.defer(() -> searchAndPersist(input.cep(), input.force())))
                .flatMap(this::toCepCompleto);
    }

    @Override
    public Mono<CepCompleto> searchGeo(SearchGeoInput input) {
        return cepService.findFirstByGeo(input.longitude(), input.latitude(), 5000)
                .switchIfEmpty(Mono.defer(() -> searchByGeoAndPersist(input.longitude(), input.latitude())))
                .flatMap(this::toCepCompleto);
    }

    private Mono<CepCompleto> toCepCompleto(Cep cep) {
        return cidadeService.findByIbge(cep.getCidadeIbge())
                .flatMap(cid -> cepCompletoDTOConverter.toDto(cep, cid));
    }

    private Mono<Cep> searchByGeoAndPersist(BigDecimal longitude, BigDecimal latitude) {
        return apiCepService.findCepApi(longitude, latitude)
                .flatMap(this::persist);
    }

    private Mono<Cep> searchAndPersist(String cep, boolean isForce) {
        return isConsultaPermitida(cep, isForce)
                .flatMap(isConsultar -> isConsultar ? search(cep) : Mono.empty());
    }

    private Mono<Cep> search(String cep) {
        return apiCepService.findCepApi(parseCep(cep))
                .flatMap(this::persist)
                .switchIfEmpty(Mono.defer(() -> salvaConsultaErro(cep)));
    }

    private Mono<Cep> persist(CepApi cepApi) {
        return Mono.just(cepApi)
                .flatMap(it -> searchCidade(it.getCidade())
                        .map(res -> {
                            it.setCidade(CidadeApi.builder()
                                    .ibge(res.getIbge())
                                    .nome(res.getNome())
                                    .estado(res.getEstado())
                                    .build());
                            return it;
                        }))
                .flatMap(it -> {
                    var dto = Cep.builder()
                            .cep(parseCep(it.getCep()))
                            .bairro(it.getBairro())
                            .complemento(it.getComplemento())
                            .logradouro(it.getLogradouro())
                            .latitude(it.getLatitude())
                            .longitude(it.getLongitude())
                            .cidadeIbge(it.getCidade().getIbge())
                            .origem(it.getOrigem())
                            .build();
                    return cepService.save(dto);
                });
    }

    private Mono<Cidade> searchCidade(CidadeApi cidade) {
        if (isNull(cidade.getIbge())) {
            return cidadeService.findByNomeUf(cidade.getNome(), cidade.getEstado())
                    .switchIfEmpty(Mono.defer(() ->
                            apiCepService.findCidadeApi(cidade.getNome(), cidade.getEstado())
                                    .map(this::toCidadeDto)
                                    .flatMap(cidadeService::saveIfNotExists)
                    ))
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("N??o foi poss??vel encontrar a cidade %s".formatted(cidade.getNome()))));
        }

        return cidadeService.findByIbge(cidade.getIbge())
                .switchIfEmpty(Mono.defer(() ->
                        apiCepService.findCidadeApi(cidade.getIbge())
                                .map(this::toCidadeDto)
                                .flatMap(cidadeService::saveIfNotExists)
                ))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("N??o foi poss??vel encontrar a cidade %s".formatted(cidade.getIbge()))));

    }

    private Mono<Cep> salvaConsultaErro(String cep) {
        return cepErrorService.save(CepError.builder()
                        .cep(cep)
                        .dataConsulta(ZonedDateTime.now())
                        .build())
                .flatMap(r -> Mono.empty());
    }

    private Mono<Boolean> isConsultaPermitida(String cep, boolean isForce) {
        if (isForce) {
            return Mono.just(true);
        }
        return cepErrorService.isConsultaPermitida(cep);
    }

    private Cidade toCidadeDto(CidadeApi cidade) {
        return Cidade.builder()
                .ibge(cidade.getIbge())
                .nome(cidade.getNome())
                .estado(cidade.getEstado())
                .build();
    }
}
