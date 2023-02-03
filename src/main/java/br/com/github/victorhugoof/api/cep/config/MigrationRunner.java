package br.com.github.victorhugoof.api.cep.config;

import br.com.github.victorhugoof.api.cep.domain.MigrationHistoryEntity;
import br.com.github.victorhugoof.api.cep.migration.BaseMigration;
import br.com.github.victorhugoof.api.cep.service.MigrationHistoryService;
import static java.util.Objects.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.exception.ExceptionUtils.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MigrationRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final List<BaseMigration> migrationList;
    private final MigrationHistoryService migrationHistoryService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Starting migration run");
        Flux.fromIterable(migrationList)
                .sort(Comparator.comparing(BaseMigration::getVersion))
                .flatMapSequential(this::run, 1)
                .doOnComplete(() -> log.info("Migration finished"))
                .subscribe();
    }

    private Mono<MigrationHistoryEntity> run(BaseMigration migration) {
        return migrationHistoryService.findByName(migration.getName())
                .filter(MigrationHistoryEntity::isSuccess)
                .flatMap(history -> {
                    log.info("Migration {}, version={}, description={} has already executed", migration.getName(), migration.getVersion(), migration.getDescription());
                    return Mono.just(history);
                })
                .switchIfEmpty(Mono.defer(() -> executeMigration(migration)));
    }

    private Mono<MigrationHistoryEntity> executeMigration(BaseMigration migration) {
        var start = ZonedDateTime.now();
        log.info("Starting run of migration {}, version={}, description={}", migration.getName(), migration.getVersion(), migration.getDescription());
        return migration.run()
                .switchIfEmpty(Mono.just(true))
                .flatMap((res) -> saveHistory(migration, start, null, res))
                .onErrorResume(e -> saveHistory(migration, start, e, null).flatMap(his -> Mono.error(e)));
    }

    private Mono<MigrationHistoryEntity> saveHistory(BaseMigration migration, ZonedDateTime start, Throwable error, Object result) {
        var end = ZonedDateTime.now();
        var ellapsed = end.toInstant().toEpochMilli() - start.toInstant().toEpochMilli();
        var feedbackMessage = isNull(error) ? "success" : "error: %s".formatted(getRootCauseMessage(error));

        log.info("Migration {} ended in {}ms, with {}, result={}", migration.getName(), ellapsed, feedbackMessage, result);
        var history = MigrationHistoryEntity.builder()
                .name(migration.getName())
                .version(migration.getVersion())
                .description(migration.getDescription())
                .installedOn(end)
                .executionTime(ellapsed)
                .success(isNull(error))
                .build();
        return migrationHistoryService.save(history);
    }
}
