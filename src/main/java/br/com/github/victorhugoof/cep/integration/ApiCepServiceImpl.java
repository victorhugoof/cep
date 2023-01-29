package br.com.github.victorhugoof.cep.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class ApiCepServiceImpl implements ApiCepService {

    private final List<CepApiHandler> cepApiHandlers;

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        var encontrado = new AtomicBoolean();

        var services = getApisIntegrationCepOrdered();
        return services
                .flatMapSequential(cepApiHandler -> {
                    if (encontrado.get()) {
                        return Mono.empty();
                    }

                    return cepApiHandler.findCepApi(numCep)
                            .flatMap(res -> {
                                if (Objects.nonNull(res)) {
                                    encontrado.set(true);
                                }
                                return Mono.just(res);
                            });

                }, 1)
                .filter(Objects::nonNull)
                .next();
    }

    private Flux<CepApiHandler> getApisIntegrationCepOrdered() {
        var list = cepApiHandlers.stream() //
                .filter(CepApiHandler::isHabilitado) //
                .sorted(Comparator.comparingInt(CepApiHandler::getOrdem));
        return Flux.fromStream(list);
    }
}
