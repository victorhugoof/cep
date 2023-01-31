package br.com.github.victorhugoof.api.cep.integration.api;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.api.cep.integration.CepApi;
import br.com.github.victorhugoof.api.cep.integration.CepApiHandler;
import br.com.github.victorhugoof.api.cep.integration.CidadeApi;
import static java.util.Objects.*;
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
public class ViaCepHandler implements CepApiHandler {

    private static final String API_URL = "https://viaCep.com.br/ws/%s/json";
    private final WebClient webClient;

    @Getter
    @Value("${cep.via-cep.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.via-cep.ordem}")
    private Integer ordem;

    public ViaCepHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no ViaCep");
        return webClient.get()
                .uri(API_URL.formatted(parseCep(numCep)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ViaCepModel.class)
                .flatMap(res -> {
                    if (isNull(res) || res.erro()) {
                        return Mono.empty();
                    }
                    return Mono.just(res);
                })
                .filter(it -> nonNull(it) && isNotBlank(it.cep()) && numCep.equals(parseCep(it.cep())))
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                })
                .mapNotNull(this::toCepApi);
    }

    private CepApi toCepApi(ViaCepModel cep) {
        return CepApi.builder()
                .cep(parseCep(cep.cep()))
                .bairro(cep.bairro())
                .logradouro(cep.logradouro())
                .complemento(cep.complemento())
                .latitude(null)
                .longitude(null)
                .cidade(CidadeApi.builder()
                        .ibge(cep.ibge())
                        .nome(cep.localidade())
                        .estado(Estado.valueOf(cep.uf()))
                        .build())
                .origem(OrigemCep.VIA_CEP)
                .build();
    }

    private record ViaCepModel(String cep, String logradouro, String complemento,
                               String bairro, String localidade, String uf,
                               String unidade, Integer ibge, String gia, boolean erro) {

    }
}
