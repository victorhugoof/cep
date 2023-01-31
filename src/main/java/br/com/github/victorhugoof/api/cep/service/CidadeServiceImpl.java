package br.com.github.victorhugoof.api.cep.service;

import br.com.github.victorhugoof.api.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.api.cep.enums.Estado;
import br.com.github.victorhugoof.api.cep.mapper.CidadeDTOConverter;
import br.com.github.victorhugoof.api.cep.model.Cidade;
import br.com.github.victorhugoof.api.cep.repository.CidadeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CidadeServiceImpl extends CachedCrudService<CidadeEntity, Integer> implements CidadeService {

    private final CidadeRepository repository;
    private final CidadeDTOConverter cidadeDTOConverter;

    public CidadeServiceImpl(CidadeRepository repository, CidadeDTOConverter cidadeDTOConverter) {
        super(repository);
        this.repository = repository;
        this.cidadeDTOConverter = cidadeDTOConverter;
    }

    @Override
    public Mono<Cidade> findByIbge(Integer ibge) {
        return cidadeDTOConverter.toEntity(Cidade.builder().ibge(ibge).build())
                .flatMap(this::findById)
                .flatMap(cidadeDTOConverter::toDto);
    }

    @Override
    public Mono<Cidade> findByNomeUf(String nome, Estado estado) {
        return repository.findFirstByNomeAndEstado(nome, estado)
                .flatMap(cidadeDTOConverter::toDto);
    }

    @Override
    public Mono<Cidade> saveIfNotExists(Cidade cidade) {
        return findByIbge(cidade.getIbge())
                .switchIfEmpty(
                        cidadeDTOConverter.toEntity(cidade)
                                .flatMap(this::save)
                                .flatMap(cidadeDTOConverter::toDto)
                );
    }
}
