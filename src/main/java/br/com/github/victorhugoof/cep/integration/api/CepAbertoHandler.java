package br.com.github.victorhugoof.cep.integration.api;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import br.com.github.victorhugoof.cep.helper.CepUtils;
import br.com.github.victorhugoof.cep.integration.CepApi;
import br.com.github.victorhugoof.cep.integration.CepApiHandler;
import static java.util.Objects.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
public class CepAbertoHandler implements CepApiHandler {
	private final WebClient webClient;

	@Getter
	@Value("${cep.cepaberto.habilitado:#{true}}")
	private boolean habilitado;

	@Getter
	@Value("${cep.cepaberto.ordem:#{0}}")
	private Integer ordem;

	@Getter
	@Value("${cep.cepaberto.token:#{'f0272ee35fe5718d3de24cec3f51b6d9'}}")
	private String token;

	public CepAbertoHandler(WebClient.Builder builder) {
		this.webClient = builder.baseUrl("https://www.cepaberto.com").build();
	}

	@Override
	public Mono<CepApi> findCepApi(Integer numCep) {
		log.info("Buscando no CEPABERTO");
		return webClient.get()
				.uri(String.format("/api/v3/cep?cep=%s", CepUtils.parseCep(numCep)))
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", String.format("Token token=%s", token))
				.retrieve()
				.bodyToMono(CepAbertoModel.class)
				.flatMap(res -> {
					if (isNull(res)) {
						log.error("Erro ao encontrar no cep aberto, response null");
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

	private CepApi toCepApi(CepAbertoModel cep) {
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
						.estado(Estado.valueOf(cep.estado().sigla()))
						.build())
				.origem(OrigemCep.CEP_ABERTO)
				.build();
	}

	private record CepAbertoModel(String cep, String complemento, String bairro, String logradouro,
								  BigDecimal altitude, BigDecimal latitude, BigDecimal longitude,
								  CepAbertoCidadeModel cidade, CepAbertoEstadoModel estado) {

	}

	private record CepAbertoCidadeModel(Integer ibge, String nome, Integer ddd) {}

	private record CepAbertoEstadoModel(String sigla) {}
}
