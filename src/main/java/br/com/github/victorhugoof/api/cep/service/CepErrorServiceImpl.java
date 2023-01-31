package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.CepErrorEntity;
import br.com.github.victorhugoof.api.cep.mapper.CepErrorDTOConverter;
import br.com.github.victorhugoof.api.cep.model.CepError;
import br.com.github.victorhugoof.api.cep.repository.CepErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Slf4j
@Component
public class CepErrorServiceImpl extends CachedCrudService<CepErrorEntity, Integer> implements CepErrorService {

    @Value("${cep.expiracao-cep-error-dias:#{30}}")
    private Integer expiracaoCepErrorDias;
    private final CepErrorDTOConverter cepErrorDTOConverter;

    public CepErrorServiceImpl(CepErrorRepository repository, CepErrorDTOConverter cepErrorDTOConverter) {
        super(repository);
        this.cepErrorDTOConverter = cepErrorDTOConverter;
    }

    @Override
    public Mono<Boolean> isConsultaPermitida(String cep) {
        return findByCep(cep)
                .mapNotNull(it -> {
                    var expiracao = it.getDataConsulta().plusDays(expiracaoCepErrorDias);
                    return ZonedDateTime.now().compareTo(expiracao) >= 0;
                })
                .switchIfEmpty(Mono.just(true));
    }

    @Override
    public Mono<CepError> save(CepError cep) {
        return cepErrorDTOConverter.toEntity(cep)
                .flatMap(this::save)
                .flatMap(cepErrorDTOConverter::toDto);
    }

    private Mono<CepError> findByCep(String cep) {
        return cepErrorDTOConverter.toEntity(CepError.builder().cep(cep).build())
                .flatMap(this::findById)
                .flatMap(cepErrorDTOConverter::toDto);
    }
}
