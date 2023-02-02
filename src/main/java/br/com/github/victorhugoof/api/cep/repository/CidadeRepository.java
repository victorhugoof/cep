package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.api.cep.enums.Estado;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CidadeRepository extends ReactiveMongoRepository<CidadeEntity, Integer> {

//    @Query(" SELECT * FROM cidade WHERE UPPER(unaccent(nome)) = UPPER(unaccent(:nome)) AND estado = :estado ")
//    Mono<CidadeEntity> findFirstByNomeAndEstado(@Param("nome") String nome, @Param("estado") Estado estado);

    Mono<CidadeEntity> findFirstByNomeIgnoreCaseAndEstado(String nome, Estado estado);
}
