package br.com.github.victorhugoof.cep.integration.api;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.integration.CepApi;
import br.com.github.victorhugoof.cep.integration.CepApiHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class CepConvcardHandler implements CepApiHandler {

    private final WebClient webClient;

    @Getter
    @Value("${cep.convcard.habilitado:#{true}}")
    private boolean habilitado;

    @Getter
    @Value("${cep.convcard.ordem:#{0}}")
    private Integer ordem;

    public CepConvcardHandler(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.convcard.com.br/api-cep").build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no CONVCARD");
        return webClient.get()
                .uri(String.format("/cep/%s", CepUtils.parseCep(numCep)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ConvcardCep.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage(), e);
                    return Mono.empty();
                })
                .mapNotNull(this::toCepApi);
    }

    private CepApi toCepApi(ConvcardCep cep) {
        return CepApi.builder()
                .cep(CepUtils.parseCep(cep.cep()))
                .bairro(cep.bairro())
                .logradouro(cep.logradouro())
                .complemento(cep.complemento())
                .latitude(cep.latitude())
                .longitude(cep.longitude())
                .cidade(CepApi.Cidade.builder()
                        .ibge(cep.cidade().ibge())
                        .nome(cep.cidade().nome())
                        .estado(Estado.valueOf(cep.cidade().estado().uf()))
                        .build())
                .origem(OrigemCep.CONVCARD)
                .build();
    }

    private record ConvcardCep(String cep, String bairro, String complemento,
                               String logradouro, ConvcardCidade cidade,
                               BigDecimal latitude, BigDecimal longitude,
                               ZonedDateTime dataCadastro, ZonedDateTime dataAtualizacao) {

    }

    private record ConvcardCidade(Integer ibge, String nome, ConvcardEstado estado) {

    }

    private record ConvcardEstado(Integer ibge, String nome, String uf) {

    }
}
