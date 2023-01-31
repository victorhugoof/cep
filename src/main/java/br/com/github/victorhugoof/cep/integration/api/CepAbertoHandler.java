package br.com.github.victorhugoof.cep.integration.api;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import static br.com.github.victorhugoof.cep.helper.CepUtils.*;
import br.com.github.victorhugoof.cep.integration.CepApi;
import br.com.github.victorhugoof.cep.integration.CepApiHandler;
import br.com.github.victorhugoof.cep.integration.CidadeApi;
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
public class CepAbertoHandler implements CepApiHandler {
	private static final String API_URL = "https://www.cepAberto.com/api/v3/cep?cep=%s";
	private final WebClient webClient;

	@Getter
	@Value("${cep.cep-aberto.habilitado}")
	private boolean habilitado;

	@Getter
	@Value("${cep.cep-aberto.ordem}")
	private Integer ordem;

	@Getter
	@Value("${cep.cep-aberto.token}")
	private String token;

	public CepAbertoHandler(WebClient.Builder builder) {
		this.webClient = builder.build();
	}

	@Override
	public Mono<CepApi> findCepApi(Integer numCep) {
		log.info("Buscando no CepAberto");
		return webClient.get()
				.uri(API_URL.formatted(parseCep(numCep)))
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Token token=%s".formatted(token))
				.retrieve()
				.bodyToMono(CepAbertoModel.class)
				.filter(it -> nonNull(it) && isNotBlank(it.cep()) && numCep.equals(parseCep(it.cep())))
				.onErrorResume(e -> {
					log.debug(e.getMessage());
					return Mono.empty();
				})
				.mapNotNull(this::toCepApi);
	}

	private CepApi toCepApi(CepAbertoModel cep) {
		return CepApi.builder()
				.cep(parseCep(cep.cep()))
				.bairro(cep.bairro())
				.logradouro(cep.logradouro())
				.complemento(cep.complemento())
				.latitude(cep.latitude())
				.longitude(cep.longitude())
				.cidade(CidadeApi.builder()
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
