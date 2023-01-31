package br.com.github.victorhugoof.api.cep.mapper;

import br.com.github.victorhugoof.api.cep.domain.CepErrorEntity;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.api.cep.model.CepError;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CepErrorDTOConverter {

    public Mono<CepError> toDto(CepErrorEntity cep) {
        var dto = CepError.builder()
                .cep(parseCep(cep.getCep()))
                .dataConsulta(cep.getDataConsulta())
                .build();
        return Mono.just(dto);
    }

    public Mono<CepErrorEntity> toEntity(CepError cep) {
        var entity = CepErrorEntity.builder()
                .cep(parseCep(cep.getCep()))
                .dataConsulta(cep.getDataConsulta())
                .build();
        return Mono.just(entity);
    }
}
