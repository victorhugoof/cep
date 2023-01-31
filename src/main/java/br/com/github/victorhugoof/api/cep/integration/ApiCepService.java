package br.com.github.victorhugoof.api.cep.integration;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import reactor.core.publisher.Mono;

public interface ApiCepService {
    Mono<CepApi> findCepApi(Integer numCep);

    Mono<CidadeApi> findCidadeApi(Integer ibge);

    Mono<CidadeApi> findCidadeApi(String nome, Estado estado);
}
