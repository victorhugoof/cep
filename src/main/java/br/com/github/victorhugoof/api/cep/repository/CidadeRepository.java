package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.api.cep.enums.Estado;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CidadeRepository extends R2dbcRepository<CidadeEntity, Integer> {

    Mono<CidadeEntity> findFirstByNomeIgnoreCaseAndEstado(String nome, Estado estado);
}
