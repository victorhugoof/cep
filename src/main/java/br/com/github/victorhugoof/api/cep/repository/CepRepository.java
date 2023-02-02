package br.com.github.victorhugoof.api.cep.repository;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CepRepository extends ReactiveMongoRepository<CepEntity, Integer> {

    Mono<GeoResult<CepEntity>> findFirstByPointNear(Point location, Distance distance);
}
