package br.com.github.victorhugoof.api.cep.mapper;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import br.com.github.victorhugoof.api.cep.helper.CepUtils;
import br.com.github.victorhugoof.api.cep.model.Cep;
import static java.util.Objects.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZoneId;

@Component
public class CepDTOConverter {

    public Mono<Cep> toDto(CepEntity cep) {
        var dto = Cep.builder()
                .cep(CepUtils.parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .complemento(cep.getComplemento())
                .logradouro(cep.getLogradouro())
                .latitude(cep.getLatitude())
                .longitude(cep.getLongitude())
                .cidadeIbge(cep.getCidadeIbge())
                .origem(cep.getOrigem())
                .createdAt(cep.getCreatedAt().atZone(ZoneId.systemDefault()))
                .updatedAt(cep.getUpdatedAt().atZone(ZoneId.systemDefault()))
                .build();
        return Mono.just(dto);
    }

    public Mono<CepEntity> toEntity(Cep cep) {
        var entity = CepEntity.builder()
                .cep(CepUtils.parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .complemento(cep.getComplemento())
                .logradouro(cep.getLogradouro())
                .latitude(cep.getLatitude())
                .longitude(cep.getLongitude())
                .cidadeIbge(cep.getCidadeIbge())
                .origem(cep.getOrigem())
                .build();

        if (nonNull(cep.getCreatedAt())) {
            entity.setCreatedAt(cep.getCreatedAt().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        }

        if (nonNull(cep.getUpdatedAt())) {
            entity.setUpdatedAt(cep.getUpdatedAt().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        }
        return Mono.just(entity);
    }
}
