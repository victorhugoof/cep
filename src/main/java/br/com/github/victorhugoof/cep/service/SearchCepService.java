package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.model.CepCompleto;
import reactor.core.publisher.Mono;

public interface SearchCepService {
    Mono<CepCompleto> searchCep(String cep, boolean isForce);
}
