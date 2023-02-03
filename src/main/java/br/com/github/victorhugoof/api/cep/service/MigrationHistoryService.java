package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.MigrationHistoryEntity;
import reactor.core.publisher.Mono;

public interface MigrationHistoryService {
    Mono<MigrationHistoryEntity> findByName(String name);

    Mono<MigrationHistoryEntity> save(MigrationHistoryEntity history);
}
