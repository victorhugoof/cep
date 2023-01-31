package br.com.github.victorhugoof.api.cep.integration;

import reactor.core.publisher.Mono;

public interface CepApiHandler {
    boolean isHabilitado();

    Integer getOrdem();

    Mono<CepApi> findCepApi(Integer numCep);
}
