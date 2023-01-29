package br.com.github.victorhugoof.cep.integration;

import reactor.core.publisher.Mono;

public interface CepApiHandler {
    default boolean isHabilitado() {
        return true;
    }

    Integer getOrdem();

    Mono<CepApi> findCepApi(Integer numCep);
}
