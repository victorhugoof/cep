package br.com.github.victorhugoof.api.cep.migration;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import static br.com.github.victorhugoof.api.cep.helper.FluxUtils.*;
import br.com.github.victorhugoof.api.cep.model.Cep;
import br.com.github.victorhugoof.api.cep.service.CepService;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.ObjectUtils.*;
import static org.apache.commons.lang3.StringUtils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class V2__InsertCeps implements BaseMigration {

    @Autowired
    private CepService cepService;

    @Override
    public ParallelFlux<Cep> run() {
        return getCidades()
                .parallel()
                .flatMap(cepService::save);
    }

    private Flux<Cep> getCidades() {
        return getLinesFromResource("/data/ceps.csv")
                .skip(1) // skip first
                .map(line -> {
                    var data = line.split(";");
                    return Cep.builder()
                            .cep(getValue(data[0]))
                            .bairro(getValue(data[1]))
                            .complemento(getValue(data[2]))
                            .logradouro(getValue(data[3]))
                            .latitude(Optional.ofNullable(getValue(data[4])).map(BigDecimal::new).orElse(null))
                            .longitude(Optional.ofNullable(getValue(data[5])).map(BigDecimal::new).orElse(null))
                            .cidadeIbge(Integer.parseInt(data[6]))
                            .origem(OrigemCep.valueOf(data[7]))
                            .build();
                });
    }

    private String getValue(String val) {
        var strValue = firstNonNull(val, "").replace("\"", "");
        return isEmpty(strValue) ? null : strValue;
    }

}
