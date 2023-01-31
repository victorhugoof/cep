package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.mapper.CepDTOConverter;
import br.com.github.victorhugoof.api.cep.repository.CepRepository;
import br.com.github.victorhugoof.api.cep.domain.CepEntity;
import br.com.github.victorhugoof.api.cep.model.Cep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CepServiceImpl extends CachedCrudService<CepEntity, Integer> implements CepService {
    private final CepDTOConverter cepDTOConverter;

    public CepServiceImpl(CepRepository repository, CepDTOConverter cepDTOConverter) {
        super(repository);
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
}
