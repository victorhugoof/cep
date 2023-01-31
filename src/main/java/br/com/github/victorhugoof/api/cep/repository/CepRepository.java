package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CepRepository extends R2dbcRepository<CepEntity, Integer> {

    @Query(" SELECT t.* " +
            " FROM (SELECT (POINT(longitude, latitude) <@> POINT(:longitude, :latitude)) * 1609 AS distancia_metros, " +
            "              cep.* " +
            "       FROM cep " +
            "       WHERE latitude IS NOT NULL " +
            "         AND longitude IS NOT NULL) AS t " +
            " WHERE t.distancia_metros <= :precisaoMetros " +
            " ORDER BY t.distancia_metros " +
            " LIMIT 1 ")
    Mono<CepEntity> findFirstByGeo(@Param("longitude") BigDecimal longitude, @Param("latitude") BigDecimal latitude, @Param("precisaoMetros") Integer precisaoMetros);
}
