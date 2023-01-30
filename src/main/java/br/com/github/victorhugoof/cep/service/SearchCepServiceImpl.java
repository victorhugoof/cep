package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.integration.ApiCepService;
import br.com.github.victorhugoof.cep.mapper.CepCompletoDTOConverter;
import br.com.github.victorhugoof.cep.model.Cep;
import br.com.github.victorhugoof.cep.model.CepCompleto;
import br.com.github.victorhugoof.cep.model.CepError;
import br.com.github.victorhugoof.cep.model.Cidade;
import br.com.github.victorhugoof.cep.model.SearchCepInput;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

        return cepService.findByCep(input.cep())
                .switchIfEmpty(searchAndPersist(input.cep(), input.force()))
                .flatMap(this::toCepCompleto);
    }

    private Mono<CepCompleto> toCepCompleto(Cep cep) {
        return cidadeService.findByIbge(cep.getCidadeIbge())
                .flatMap(cid -> cepCompletoDTOConverter.toDto(cep, cid));
    }

    private Mono<Cep> searchAndPersist(String cep, boolean isForce) {
        return isConsultaPermitida(cep, isForce)
                .flatMap(isConsultar -> isConsultar ? buscarCepConvcard(cep) : Mono.empty());
    }

    private Mono<Cep> buscarCepConvcard(String cep) {
        return apiCepService.findCepApi(CepUtils.parseCep(cep))
                .flatMap(it -> {
                    var cidade = Cidade.builder()
                            .ibge(it.getCidade().getIbge())
                            .nome(it.getCidade().getNome())
                            .estado(Estado.valueOf(it.getCidade().getEstado().getUf()))
                            .build();

                    return cidadeService.saveIfNotExists(cidade).map(s -> it); // FIXME como faz um tap?
                })
                .flatMap(it -> {
                    var dto = Cep.builder()
                            .cep(CepUtils.parseCep(it.getCep()))
                            .bairro(it.getBairro())
                            .complemento(it.getComplemento())
                            .logradouro(it.getLogradouro())
                            .latitude(it.getLatitude())
                            .longitude(it.getLongitude())
                            .cidadeIbge(it.getCidade().getIbge())
                            .origem(it.getOrigem())
                            .build();
                    return cepService.save(dto);
                })
                .switchIfEmpty(salvaConsultaErro(cep));
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
}
