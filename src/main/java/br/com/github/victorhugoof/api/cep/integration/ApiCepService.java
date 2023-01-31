package br.com.github.victorhugoof.api.cep.integration;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApiCepService {
    Mono<CepApi> findCepApi(Integer numCep);

    Mono<CepApi> findCepApi(BigDecimal longitude, BigDecimal latitude);

    Mono<CidadeApi> findCidadeApi(Integer ibge);

    Mono<CidadeApi> findCidadeApi(String nome, Estado estado);
}
