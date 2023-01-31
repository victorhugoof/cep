package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.model.Cidade;
import br.com.github.victorhugoof.api.cep.enums.Estado;
import reactor.core.publisher.Mono;

public interface CidadeService {

    Mono<Cidade> findByIbge(Integer ibge);

    Mono<Cidade> findByNomeUf(String nome, Estado estado);

    Mono<Cidade> saveIfNotExists(Cidade cidade);
}
