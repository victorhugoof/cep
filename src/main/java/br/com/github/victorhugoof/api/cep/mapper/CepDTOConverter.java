package br.com.github.victorhugoof.api.cep.mapper;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.api.cep.helper.PointConverter;
import br.com.github.victorhugoof.api.cep.model.Cep;
import static java.util.Objects.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CepDTOConverter {

    public Mono<Cep> toDto(CepEntity cep) {
        var pointConverter = new PointConverter(cep.getPoint());
        var dto = Cep.builder()
                .cep(parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .complemento(cep.getComplemento())
                .logradouro(cep.getLogradouro())
                .latitude(pointConverter.getLatitude())
                .longitude(pointConverter.getLongitude())
                .cidadeIbge(cep.getCidadeIbge())
                .origem(cep.getOrigem())
                .createdAt(cep.getCreatedAt())
                .updatedAt(cep.getUpdatedAt())
                .build();
        return Mono.just(dto);
    }

    public Mono<CepEntity> toEntity(Cep cep) {
        var entity = CepEntity.builder()
                .cep(parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .complemento(cep.getComplemento())
                .logradouro(cep.getLogradouro())
                .point(new PointConverter(cep.getLongitude(), cep.getLatitude()).getPoint())
                .cidadeIbge(cep.getCidadeIbge())
                .origem(cep.getOrigem())
                .createdAt(cep.getCreatedAt())
                .updatedAt(cep.getUpdatedAt())
                .build();
        return Mono.just(entity);
    }
}
