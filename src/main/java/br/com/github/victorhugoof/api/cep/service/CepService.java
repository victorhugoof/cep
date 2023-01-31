package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.model.Cep;
import reactor.core.publisher.Mono;

public interface CepService {

    Mono<Cep> findByCep(String cep);

    Mono<Cep> save(Cep cep);
}
