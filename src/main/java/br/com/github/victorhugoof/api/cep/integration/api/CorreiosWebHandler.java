package br.com.github.victorhugoof.api.cep.integration.api;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.integration.CepApi;
import br.com.github.victorhugoof.api.cep.integration.CepApiHandler;
import br.com.github.victorhugoof.api.cep.integration.CidadeApi;
import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class CorreiosWebHandler implements CepApiHandler {

    private static final String API_URL = "https://buscacepinter.correios.com.br/app/cep/carrega-cep.php";
    private static final String API_URL_SECUNDARIA = "https://buscacepinter.correios.com.br/app/endereco/carrega-cep-endereco.php";
    private final WebClient webClient;

    @Getter
    @Value("${cep.correios-web.habilitado}")
    private boolean habilitado;

    @Getter
    @Value("${cep.correios-web.ordem}")
    private Integer ordem;

    public CorreiosWebHandler(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Mono<CepApi> findCepApi(Integer numCep) {
        log.info("Buscando no CorreiosWeb");
        return findCep(numCep)
                .switchIfEmpty(findCepSecundario(numCep))
                .map(this::toCepApi);
    }

    private Mono<CorreiosWebCep> findCep(Integer numCep) {
        return webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("cep", parseCep(numCep)))
                .retrieve()
                .bodyToMono(CorreiosWebResponse.class)
                .filter(it -> !it.isErro() && it.getTotal() > 0)
                .flatMapIterable(CorreiosWebResponse::getDados)
                .filter(it -> parseCep(it.getCep()).equals(numCep))
                .next()
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<CorreiosWebCep> findCepSecundario(Integer numCep) {
        return webClient.post()
                .uri(API_URL_SECUNDARIA)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("endereco", parseCep(numCep))
                        .with("pagina", "app/endereco/index.php")
                        .with("cpaux", "")
                        .with("mensagem_alerta", "")
                        .with("tipoCEP", "ALL")
                )
                .retrieve()
                .bodyToMono(CorreiosWebResponse.class)
                .filter(it -> !it.isErro() && it.getTotal() > 0)
                .flatMapIterable(CorreiosWebResponse::getDados)
                .filter(it -> parseCep(it.getCep()).equals(numCep))
                .next()
                .onErrorResume(e -> {
                    log.debug(e.getMessage());
                    return Mono.empty();
                });
    }

    private CepApi toCepApi(CorreiosWebCep cep) {
        return CepApi.builder()
                .cep(parseCep(cep.getCep()))
                .bairro(cep.getBairro())
                .logradouro(cep.getLogradouro())
                .complemento(null)
                .latitude(null)
                .longitude(null)
                .cidade(CidadeApi.builder()
                        .ibge(null)
                        .nome(cep.getCidade())
                        .estado(Estado.find(cep.getUf()))
                        .build())
                .origem(OrigemCep.CORREIOS_WEB)
                .build();
    }

    @Getter
    @AllArgsConstructor
    private static final class CorreiosWebResponse {

        private final boolean erro;
        private final String mensagem;
        private final Integer total;
        private final List<CorreiosWebCep> dados;
    }

    @Getter
    @AllArgsConstructor
    private static final class CorreiosWebCep {

        private final String cep;
        private final String uf;
        private final String localidade;
        private final String locNu;
        private final String localidadeSubordinada;
        private final String logradouroDNEC;
        private final String logradouroTextoAdicional;
        private final String logradouroTexto;
        private final String bairro;
        private final String baiNu;
        private final String nomeUnidade;
        private final String tipoCep;
        private final String numeroLocalidade;
        private final String situacao;

        public String getLogradouro() {
            return isBlank(logradouroTexto) ? logradouroDNEC : logradouroTexto;
        }

        public String getCidade() {
            return isBlank(localidadeSubordinada) ? localidade : localidadeSubordinada;
        }
    }
}
