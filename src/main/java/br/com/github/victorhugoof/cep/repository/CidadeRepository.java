package br.com.github.victorhugoof.cep.repository;

import br.com.github.victorhugoof.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.cep.enums.Estado;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CidadeRepository extends R2dbcRepository<CidadeEntity, Integer> {

    Mono<CidadeEntity> findFirstByNomeIgnoreCaseAndEstado(String nome, Estado estado);
}
