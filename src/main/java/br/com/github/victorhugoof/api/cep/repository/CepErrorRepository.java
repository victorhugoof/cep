package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CepErrorEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CepErrorRepository extends R2dbcRepository<CepErrorEntity, Integer> {

}
