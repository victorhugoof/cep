package br.com.github.victorhugoof.cep.mapper;

import br.com.github.victorhugoof.cep.domain.CepErrorEntity;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.model.CepError;
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
