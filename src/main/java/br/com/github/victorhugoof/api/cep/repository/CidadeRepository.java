package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.model.Cep;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CidadeRepository extends R2dbcRepository<CidadeEntity, Integer> {

    @Query(" SELECT * FROM cidade WHERE UPPER(unaccent(nome)) = UPPER(unaccent(:nome)) AND estado = :estado ")
    Mono<CidadeEntity> findFirstByNomeAndEstado(@Param("nome") String nome, @Param("estado") Estado estado);
}
