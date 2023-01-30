package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.model.CepCompleto;
import br.com.github.victorhugoof.cep.model.SearchCepInput;
import reactor.core.publisher.Mono;

public interface SearchCepService {
    Mono<CepCompleto> searchCep(SearchCepInput input);
}
