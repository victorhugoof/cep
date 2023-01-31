package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.model.Cidade;
import reactor.core.publisher.Mono;

public interface CidadeService {

    Mono<Cidade> findByIbge(Integer ibge);

    Mono<Cidade> findByNomeUf(String nome, Estado estado);

    Mono<Cidade> saveIfNotExists(Cidade cidade);
}
