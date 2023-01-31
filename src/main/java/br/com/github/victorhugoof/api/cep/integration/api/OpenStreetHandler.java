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

import java.math.BigDecimal;

@Slf4j
@Component
public class OpenStreetHandler implements CepApiHandler {

    private static final String API_URL = "https://nominatim.openstreetmap.org/search.php?country=BR&postalcode=%s&addressdetails=1&format=json";
    private final WebClient webClient;

    @Getter
    @Value("${cep.open-street.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.open-street.ordem}")
    private Integer ordem;

    public OpenStreetHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no OpenStreet");
        return doExecuteFind(parseCep(numCep))
                .switchIfEmpty(Mono.defer(() -> doExecuteFind(parseCepFormat(numCep))))
                .mapNotNull(this::toCepApi);
    }

    private Mono<OpenStreetModel> doExecuteFind(String numCep) {
        return webClient.get()
                .uri(API_URL.formatted(numCep))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(OpenStreetModel.class)
                .filter(res -> nonNull(res)
                        && "postcode".equals(res.type())
                        && nonNull(res.address())
                        && isNotBlank(res.address().cidade())
                        && isNotBlank(res.address().state())
                        && parseCep(res.address().postcode()).equals(parseCep(numCep)))
                .next()
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private CepApi toCepApi(OpenStreetModel cep) {
        return CepApi.builder()
                .cep(parseCep(cep.address().postcode()))
                .bairro(cep.address().suburb())
                .logradouro(cep.address().road())
                .complemento(null)
                .latitude(cep.lat())
                .longitude(cep.lon())
                .cidade(CidadeApi.builder()
                        .ibge(null)
                        .nome(cep.address().cidade())
                        .estado(Estado.find(cep.address().state()))
                        .build())
                .origem(OrigemCep.OPEN_STREET)
                .build();
    }

    private record OpenStreetModel(Integer placeId, BigDecimal lat, BigDecimal lon, String type, OpenStreetAddressModel address) {

    }

    private record OpenStreetAddressModel(String road, String suburb, String town, String city, String state, String postcode) {

        public String cidade() {
            return isBlank(city()) ? town() : city();
        }
    }
}
