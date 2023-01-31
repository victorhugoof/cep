package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.model.Cep;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CepService {

    Mono<Cep> findByCep(String cep);

    Mono<Cep> save(Cep cep);

    Mono<Cep> findFirstByGeo(BigDecimal longitude, BigDecimal latitude, Integer precisaoMetros);
}
