package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.cep.mapper.CidadeDTOConverter;
import br.com.github.victorhugoof.cep.model.Cidade;
import br.com.github.victorhugoof.cep.repository.CidadeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CidadeServiceImpl extends CachedCrudService<CidadeEntity, Integer> implements CidadeService {
    private final CidadeDTOConverter cidadeDTOConverter;

    public CidadeServiceImpl(CidadeRepository repository, CidadeDTOConverter cidadeDTOConverter) {
        super(repository);
        this.cidadeDTOConverter = cidadeDTOConverter;
    }

    @Override
    public Mono<Cidade> findByIbge(Integer ibge) {
        return cidadeDTOConverter.toEntity(Cidade.builder().ibge(ibge).build())
                .flatMap(this::findById)
                .flatMap(cidadeDTOConverter::toDto);
    }

    @Override
    public Mono<Cidade> save(Cidade cidade) {
        return cidadeDTOConverter.toEntity(cidade)
                .flatMap(this::save)
                .flatMap(cidadeDTOConverter::toDto);
    }
}
