package br.com.github.victorhugoof.api.cep.integration;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ApiCepServiceImpl implements ApiCepService {

    private final List<CepApiHandler> cepApiHandlers;
    private final List<CidadeApiHandler> cidadeApiHandlers;

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        return findFirstResult(getApisIntegrationCepOrdered(), handler -> handler.findCepApi(numCep));
    }

    @Override
    public Mono<CepApi> findCepApi(BigDecimal longitude, BigDecimal latitude) {
        return findFirstResult(getApisIntegrationCepOrdered(), handler -> handler.findCepApi(longitude, latitude));
    }

    @Override
    public Mono<CidadeApi> findCidadeApi(Integer ibge) {
        return findFirstResult(getApisIntegrationCidadeOrdered(), handler -> handler.findCidadeApi(ibge));
    }

    @Override
    public Mono<CidadeApi> findCidadeApi(String nome, Estado estado) {
        return findFirstResult(getApisIntegrationCidadeOrdered(), handler -> handler.findCidadeApi(nome, estado));
    }

    private <T, R> Mono<R> findFirstResult(Flux<T> services, Function<T, Mono<R>> caller) {
        var encontrado = new AtomicBoolean();

        return services
                .flatMapSequential(service -> {
                    if (encontrado.get()) {
                        return Mono.empty();
                    }

                    return caller.apply(service)
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

    private Flux<CidadeApiHandler> getApisIntegrationCidadeOrdered() {
        var list = cidadeApiHandlers.stream() //
                .filter(CidadeApiHandler::isHabilitado) //
                .sorted(Comparator.comparingInt(CidadeApiHandler::getOrdem));
        return Flux.fromStream(list);
    }
}
