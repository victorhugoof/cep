package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import br.com.github.victorhugoof.api.cep.mapper.CepDTOConverter;
import br.com.github.victorhugoof.api.cep.model.Cep;
import br.com.github.victorhugoof.api.cep.repository.CepRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
public class CepServiceImpl extends CachedCrudService<CepEntity, Integer> implements CepService {

    private final CepRepository cepRepository;
    private final CepDTOConverter cepDTOConverter;

    public CepServiceImpl(CepRepository repository, CepDTOConverter cepDTOConverter) {
        super(repository);
        this.cepRepository = repository;
        this.cepDTOConverter = cepDTOConverter;
    }

    @Override
    public Mono<Cep> findByCep(String cep) {
        return cepDTOConverter.toEntity(Cep.builder().cep(cep).build())
                .flatMap(this::findById)
                .flatMap(cepDTOConverter::toDto);
    }

    @Override
    public Mono<Cep> save(Cep cep) {
        return cepDTOConverter.toEntity(cep)
                .flatMap(this::save)
                .flatMap(cepDTOConverter::toDto);
    }

    @Override
    public Mono<Cep> findFirstByGeo(BigDecimal longitude, BigDecimal latitude, Integer precisaoMetros) {
        return cepRepository.findFirstByGeo(longitude, latitude, precisaoMetros)
                .flatMap(cepDTOConverter::toDto);
    }
}
