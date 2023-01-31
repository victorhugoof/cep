package br.com.github.victorhugoof.cep.integration.api;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import static br.com.github.victorhugoof.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.cep.integration.CepApi;
import br.com.github.victorhugoof.cep.integration.CepApiHandler;
import br.com.github.victorhugoof.cep.integration.CidadeApi;
import com.fasterxml.jackson.annotation.JsonAlias;
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
public class PostmonHandler implements CepApiHandler {

    private static final String API_URL = "https://api.postmon.com.br/v1/cep/%s";
    private final WebClient webClient;

    @Getter
    @Value("${cep.postmon.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.postmon.ordem}")
    private Integer ordem;

    public PostmonHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no Postmon");
        return webClient.get()
                .uri(API_URL.formatted(parseCep(numCep)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PostmonModel.class)
                .filter(it -> nonNull(it) && isNotBlank(it.cep()) && numCep.equals(parseCep(it.cep())))
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                })
                .mapNotNull(this::toCepApi);
    }

    private CepApi toCepApi(PostmonModel cep) {
        return CepApi.builder()
                .cep(parseCep(cep.cep()))
                .bairro(cep.bairro())
                .logradouro(cep.logradouro())
                .complemento(cep.complemento())
                .latitude(null)
                .longitude(null)
                .cidade(CidadeApi.builder()
                        .ibge(cep.cidadeInfo().codigoIbge())
                        .nome(cep.cidade())
                        .estado(Estado.valueOf(cep.estado()))
                        .build())
                .origem(OrigemCep.POSTMON)
                .build();
    }

    private record PostmonModel(String cep, String complemento, String bairro, String logradouro, String cidade, String estado,
                                @JsonAlias("cidade_info") PostmonCidadeModel cidadeInfo,
                                @JsonAlias("estado_info") PostmonEstadoModel estadoInfo
    ) {

    }

    private record PostmonCidadeModel(
            @JsonAlias("codigo_ibge") Integer codigoIbge,
            @JsonAlias("area_km2") String areaKm2
    ) {

    }

    private record PostmonEstadoModel(
            @JsonAlias("codigo_ibge") Integer codigoIbge,
            @JsonAlias("area_km2") String areaKm2,
            String nome
    ) {

    }
}
