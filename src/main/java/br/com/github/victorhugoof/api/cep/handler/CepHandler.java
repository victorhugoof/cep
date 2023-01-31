package br.com.github.victorhugoof.api.cep.handler;

import br.com.github.victorhugoof.api.cep.model.SearchGeoInput;
import br.com.github.victorhugoof.api.cep.service.SearchCepService;
import br.com.github.victorhugoof.api.cep.model.SearchCepInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

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

    public Mono<ServerResponse> findByGeo(ServerRequest request) {
        var latitude = NumberUtils.createBigDecimal(request.queryParam("latitude").orElse(null));
        var longitude = NumberUtils.createBigDecimal(request.queryParam("longitude").orElse(null));
        var contentType = request.headers().contentType().orElse(MediaType.APPLICATION_JSON);

        return searchCepService.searchGeo(new SearchGeoInput(latitude, longitude))
                .flatMap(res -> ServerResponse.ok().contentType(contentType).body(BodyInserters.fromValue(res)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
