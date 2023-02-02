package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CepErrorEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CepErrorRepository extends ReactiveMongoRepository<CepErrorEntity, Integer> {

}
