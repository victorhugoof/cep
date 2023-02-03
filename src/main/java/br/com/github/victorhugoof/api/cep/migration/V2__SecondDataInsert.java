package br.com.github.victorhugoof.api.cep.migration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class V2__SecondDataInsert implements BaseMigration {

    @Override
    public Mono<Object> run() {
        log.info("run run run run");
        return Mono.empty()
                .delaySubscription(Duration.ofSeconds(10));
    }
}
