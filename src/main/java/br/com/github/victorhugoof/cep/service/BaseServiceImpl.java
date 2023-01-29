package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.domain.BaseEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity<I>, I> {

    protected abstract R2dbcRepository<T, I> getRepository();

    Mono<T> save(T entity) {
        return Mono.justOrEmpty(entity.getId())
                .flatMap(getRepository()::existsById)
                .switchIfEmpty(Mono.just(false))
                .flatMap(exists -> {
                    if (exists) {
                        entity.hasNotNew();
                    } else {
                        entity.hasNew();
                    }
                    return getRepository().save(entity);
                });
    }
}
