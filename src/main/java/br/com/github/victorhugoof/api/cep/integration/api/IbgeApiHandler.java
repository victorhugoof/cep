package br.com.github.victorhugoof.api.cep.integration.api;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.integration.CidadeApi;
import br.com.github.victorhugoof.api.cep.integration.CidadeApiHandler;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class IbgeApiHandler implements CidadeApiHandler {

    private static final String API_URL_CIDADES = "https://servicodados.ibge.gov.br/api/v1/localidades/municipios/%s";
    private static final String API_URL_CIDADES_UF = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/%s/municipios";

    private final WebClient webClient;

    @Getter
    @Value("${cep.ibge.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.ibge.ordem}")
    private Integer ordem;

    public IbgeApiHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CidadeApi> findCidadeApi(Integer ibge) {
        log.info("Buscando no IBGE");
        return findCidade(ibge)
                .map(this::toCidadeApi);
    }

    @Override
    public Mono<CidadeApi> findCidadeApi(String nome, Estado estado) {
        log.info("Buscando no IBGE");
        return findCidade(nome, estado)
                .switchIfEmpty(Mono.defer(() -> findCidadeSecundario(nome, estado)))
                .map(this::toCidadeApi);
    }

    private CidadeApi toCidadeApi(CidadeIbge cidade) {
        return CidadeApi.builder()
                .ibge(cidade.id())
                .nome(cidade.nome())
                .estado(Estado.find(cidade.microrregiao().mesorregiao().estado().sigla()))
                .build();
    }

    private Mono<CidadeIbge> findCidade(Integer ibge) {
        return webClient.get()
                .uri(API_URL_CIDADES.formatted(ibge))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CidadeIbge.class)
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<CidadeIbge> findCidade(String nome, Estado estado) {
        return webClient.get()
                .uri(API_URL_CIDADES.formatted(stripAccents(nome)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CidadeIbge.class)
                .filter(it -> it.microrregiao().mesorregiao().estado().sigla().equals(estado.getUf()))
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<CidadeIbge> findCidadeSecundario(String nome, Estado estado) {
        return webClient.get()
                .uri(API_URL_CIDADES_UF.formatted(estado.getUf()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CidadeIbge.class)
                .filter(cidade -> stripAccents(nome).equalsIgnoreCase(stripAccents(cidade.nome())))
                .next()
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private record CidadeIbge(Integer id, String nome, Microregiao microrregiao) {

    }

    private record Microregiao(Integer id, String nome, Mesorregiao mesorregiao) {

    }

    private record Mesorregiao(Integer id, String nome, @JsonAlias("UF") EstadoIbge estado) {

    }

    private record EstadoIbge(Integer id, String nome, String sigla, Regiao regiao) {

    }

    private record Regiao(Integer id, String nome, String sigla) {

    }

}
