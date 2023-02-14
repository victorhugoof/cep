package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.BaseEntity;
import static java.util.Optional.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
abstract class CachedCrudService<T extends BaseEntity<I>, I> {

    private final ReactiveMongoRepository<T, I> repository;
    private final Map<I, T> cache = new HashMap<>();

    Mono<Boolean> existsById(T entity) {
        return findById(entity).hasElement();
    }

    Mono<T> findById(T entity) {
        return Mono.justOrEmpty(entity.getId())
                .flatMap(it -> {
                    if (cache.containsKey(it)) {
                        return Mono.just(cache.get(it));
                    }

                    return repository.findById(it).map(loaded -> {
                        cache.put(loaded.getId(), loaded);
                        return loaded;
                    });
                });
    }

    Mono<T> save(T entity) {
        return findById(entity)
                .flatMap(existent -> {
                    entity.setUpdatedAt(ZonedDateTime.now());
                    entity.setCreatedAt(ofNullable(existent.getCreatedAt()).orElseGet(ZonedDateTime::now));
                    entity.asNotNew();
                    return repository.save(entity)
                            .doOnSubscribe((it) -> log().info("Updating {}", entity.getId()));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    entity.setUpdatedAt(ZonedDateTime.now());
                    entity.setCreatedAt(ZonedDateTime.now());
                    entity.asNew();
                    return repository.save(entity)
                            .doOnSubscribe((it) -> log().info("Creating {}", entity.getId()));
                }))
                .map(saved -> {
                    cache.put(saved.getId(), saved);
                    return saved;
                });
    }

    protected abstract Logger log();
}
