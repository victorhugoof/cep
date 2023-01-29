package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.model.CepError;
import reactor.core.publisher.Mono;

public interface CepErrorService {

    Mono<Boolean> isConsultaPermitida(String cep);

    Mono<CepError> save(CepError cep);
}
