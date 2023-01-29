package br.com.github.victorhugoof.cep.repository;

import br.com.github.victorhugoof.cep.domain.CidadeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CidadeRepository extends R2dbcRepository<CidadeEntity, Integer> {

}
