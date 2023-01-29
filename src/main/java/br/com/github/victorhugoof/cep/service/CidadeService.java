package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.model.Cidade;
import reactor.core.publisher.Mono;

public interface CidadeService {

    Mono<Cidade> findByIbge(Integer ibge);

    Mono<Cidade> save(Cidade cidade);
}
