package br.com.github.victorhugoof.cep.mapper;

import br.com.github.victorhugoof.cep.domain.CepEntity;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.model.Cep;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        return Mono.just(entity);
    }
}
