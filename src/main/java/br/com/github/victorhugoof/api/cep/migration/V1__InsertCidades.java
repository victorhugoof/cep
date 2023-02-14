package br.com.github.victorhugoof.api.cep.migration;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import static br.com.github.victorhugoof.api.cep.helper.FluxUtils.*;
import br.com.github.victorhugoof.api.cep.model.Cidade;
import br.com.github.victorhugoof.api.cep.service.CidadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

@Slf4j
@Component
public class V1__InsertCidades implements BaseMigration {

    @Autowired
    private CidadeService cidadeService;

    @Override
    public ParallelFlux<Cidade> run() {
        return getCidades()
                .parallel()
                .flatMap(cidadeService::saveIfNotExists);
    }

    private Flux<Cidade> getCidades() {
        return getLinesFromResource("/data/cidades.csv")
                .skip(1) // skip first
                .map(line -> {
                    var data = line.split(";");
                    return Cidade.builder()
                            .ibge(Integer.parseInt(data[0]))
                            .nome(data[1])
                            .estado(Estado.valueOf(data[2]))
                            .build();
                });
    }
}
