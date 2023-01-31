package br.com.github.victorhugoof.cep.handler;

import br.com.github.victorhugoof.cep.model.SearchCepInput;
import br.com.github.victorhugoof.cep.service.SearchCepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
public class CepHandler {

    private final SearchCepService searchCepService;

    public Mono<ServerResponse> findByCep(ServerRequest request) {
        var cep = request.pathVariable("cep");
        var force = Boolean.parseBoolean(request.queryParam("force").orElse("false"));
        var update = Boolean.parseBoolean(request.queryParam("update").orElse("false"));
        var contentType = request.headers().contentType().orElse(MediaType.APPLICATION_JSON);

        return searchCepService.searchCep(new SearchCepInput(cep, force, update))
                .flatMap(res -> ServerResponse.ok().contentType(contentType).body(BodyInserters.fromValue(res)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
