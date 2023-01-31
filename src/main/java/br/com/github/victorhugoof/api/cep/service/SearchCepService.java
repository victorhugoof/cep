package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.model.CepCompleto;
import br.com.github.victorhugoof.api.cep.model.SearchCepInput;
import reactor.core.publisher.Mono;

public interface SearchCepService {
    Mono<CepCompleto> searchCep(SearchCepInput input);
}
