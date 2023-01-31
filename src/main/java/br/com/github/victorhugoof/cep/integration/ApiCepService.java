package br.com.github.victorhugoof.cep.integration;

import br.com.github.victorhugoof.cep.enums.Estado;
import reactor.core.publisher.Mono;

public interface ApiCepService {
    Mono<CepApi> findCepApi(Integer numCep);

    Mono<CidadeApi> findCidadeApi(Integer ibge);

    Mono<CidadeApi> findCidadeApi(String nome, Estado estado);
}
