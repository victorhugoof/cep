package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.model.CepError;
import reactor.core.publisher.Mono;

public interface CepErrorService {

    Mono<Boolean> isConsultaPermitida(String cep);

    Mono<CepError> save(CepError cep);
}
