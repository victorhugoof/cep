package br.com.github.victorhugoof.cep.service;

import br.com.github.victorhugoof.cep.domain.CidadeEntity;
import br.com.github.victorhugoof.cep.mapper.CidadeDTOConverter;
import br.com.github.victorhugoof.cep.model.Cidade;
import br.com.github.victorhugoof.cep.repository.CidadeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CidadeServiceImpl extends BaseServiceImpl<CidadeEntity, Integer> implements CidadeService {
    @Getter
    private final CidadeRepository repository;
    private final CidadeDTOConverter cidadeDTOConverter;

    @Override
    public Mono<Cidade> findByIbge(Integer ibge) {
        return cidadeDTOConverter.toEntity(Cidade.builder().ibge(ibge).build())
                .mapNotNull(CidadeEntity::getId)
                .flatMap(repository::findById)
                .flatMap(cidadeDTOConverter::toDto);
    }

    @Override
    public Mono<Cidade> save(Cidade cidade) {
        return cidadeDTOConverter.toEntity(cidade)
                .flatMap(this::save)
                .flatMap(cidadeDTOConverter::toDto);
    }
}
