package br.com.github.victorhugoof.cep.integration.api.viacep;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.integration.CepApi;
import br.com.github.victorhugoof.cep.integration.CepApiHandler;
import static java.util.Objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ViaCepHandler implements CepApiHandler {

    private final WebClient webClient;

    public ViaCepHandler(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://viacep.com.br").build();
    }

    @Override
    public Integer getOrdem() {
        return 0;
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
		log.info("Buscando no VIACEP");
        return webClient.get()
                .uri(String.format("/ws/%s/json", CepUtils.parseCep(numCep)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ViaCepModel.class)
                .flatMap(res -> {
                    if (isNull(res) || res.erro()) {
						log.error("Erro ao encontrar no via cep, response = {}", res);
                        return Mono.empty();
                    }
                    return Mono.just(res);
                })
                .onErrorResume(e -> {
                    log.error(e.getMessage(), e);
                    return Mono.empty();
                })
                .mapNotNull(this::toCepApi);
    }

    private CepApi toCepApi(ViaCepModel cep) {
        return CepApi.builder()
                .cep(CepUtils.parseCep(cep.cep()))
                .bairro(cep.bairro())
                .logradouro(cep.logradouro())
                .complemento(cep.complemento())
                .latitude(null)
                .longitude(null)
                .cidade(CepApi.Cidade.builder()
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
