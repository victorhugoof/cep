package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.MigrationHistoryEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MigrationHistoryRepository extends ReactiveMongoRepository<MigrationHistoryEntity, String> {
}
