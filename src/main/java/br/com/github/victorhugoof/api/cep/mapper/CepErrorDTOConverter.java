package br.com.github.victorhugoof.api.cep.mapper;

import br.com.github.victorhugoof.api.cep.domain.CepErrorEntity;
import br.com.github.victorhugoof.api.cep.model.CepError;
import br.com.github.victorhugoof.api.cep.helper.CepUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CepErrorDTOConverter {

    public Mono<CepError> toDto(CepErrorEntity cep) {
        var dto = CepError.builder()
                .cep(CepUtils.parseCep(cep.getCep()))
                .dataConsulta(cep.getDataConsulta())
                .build();
        return Mono.just(dto);
    }

    public Mono<CepErrorEntity> toEntity(CepError cep) {
        var entity = CepErrorEntity.builder()
                .cep(CepUtils.parseCep(cep.getCep()))
                .dataConsulta(cep.getDataConsulta())
                .build();
        return Mono.just(entity);
    }
}
