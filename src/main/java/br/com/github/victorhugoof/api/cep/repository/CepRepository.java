package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CepRepository extends R2dbcRepository<CepEntity, Integer> {

}
