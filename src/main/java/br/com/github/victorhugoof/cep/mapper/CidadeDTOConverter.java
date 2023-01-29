package br.com.github.victorhugoof.cep.mapper;

import br.com.github.victorhugoof.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.cep.model.Cidade;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CidadeDTOConverter {

    public Mono<Cidade> toDto(CidadeEntity cidade) {
        var dto = Cidade.builder()
                .ibge(cidade.getIbge())
                .nome(cidade.getNome())
                .estado(cidade.getEstado())
                .build();
        return Mono.just(dto);
    }

    public Mono<CidadeEntity> toEntity(Cidade cidade) {
        var entity = CidadeEntity.builder()
                .ibge(cidade.getIbge())
                .nome(cidade.getNome())
                .estado(cidade.getEstado())
                .build();
        return Mono.just(entity);
    }
}
