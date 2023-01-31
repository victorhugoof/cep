package br.com.github.victorhugoof.api.cep.integration;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CepApiHandler {

    boolean isHabilitado();

    Integer getOrdem();

    Mono<CepApi> findCepApi(Integer numCep);

    default Mono<CepApi> findCepApi(BigDecimal longitude, BigDecimal latitude) {
        return Mono.empty();
    }
}
