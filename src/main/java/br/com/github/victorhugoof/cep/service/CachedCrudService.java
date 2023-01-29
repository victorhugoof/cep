package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
abstract class CachedCrudService<T extends BaseEntity<I>, I> {

    private final R2dbcRepository<T, I> repository;
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
        return existsById(entity)
                .flatMap(exists -> {
                    if (exists) {
                        entity.hasNotNew();
                    } else {
                        entity.hasNew();
                    }
                    return repository.save(entity);
                })
                .map(saved -> {
                    cache.put(saved.getId(), saved);
                    return saved;
                });
    }
}
