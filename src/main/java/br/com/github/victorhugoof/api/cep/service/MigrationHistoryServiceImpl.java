package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.MigrationHistoryEntity;
import br.com.github.victorhugoof.api.cep.repository.MigrationHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MigrationHistoryServiceImpl extends CachedCrudService<MigrationHistoryEntity, String> implements MigrationHistoryService {

    public MigrationHistoryServiceImpl(MigrationHistoryRepository repository) {
        super(repository);
    }

    @Override
    public Mono<MigrationHistoryEntity> findByName(String name) {
        return findById(MigrationHistoryEntity.builder().name(name).build());
    }

    @Override
    public Mono<MigrationHistoryEntity> save(MigrationHistoryEntity history) {
        return super.save(history);
    }

    @Override
    protected Logger log() {
        return log;
    }
}
