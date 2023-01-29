package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.model.Cep;
import reactor.core.publisher.Mono;

public interface CepService {

    Mono<Cep> findByCep(String cep);

    Mono<Cep> save(Cep cep);
}
