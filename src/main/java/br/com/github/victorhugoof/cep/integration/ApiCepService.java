package br.com.github.victorhugoof.cep.integration;

import reactor.core.publisher.Mono;

public interface ApiCepService {
    Mono<CepApi> findCepApi(Integer numCep);
}
