package br.com.github.victorhugoof.api.cep.mapper;

import br.com.github.victorhugoof.api.cep.model.Cidade;
import br.com.github.victorhugoof.api.cep.model.Cep;
import br.com.github.victorhugoof.api.cep.model.CepCompleto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CepCompletoDTOConverter {

    public Mono<CepCompleto> toDto(Cep cep, Cidade cidade) {
        var dto = CepCompleto.builder()
                .cep(cep.getCep())
                .bairro(cep.getBairro())
                .complemento(cep.getComplemento())
                .logradouro(cep.getLogradouro())
                .latitude(cep.getLatitude())
                .longitude(cep.getLongitude())
                .cidade(cidade)
                .origem(cep.getOrigem())
                .dataCadastro(cep.getCreatedAt())
                .dataAtualizacao(cep.getUpdatedAt())
                .build();
        return Mono.just(dto);
    }
}
