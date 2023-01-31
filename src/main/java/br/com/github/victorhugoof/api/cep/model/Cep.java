package br.com.github.victorhugoof.api.cep.model;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class Cep {

    private String cep;
    private String bairro;
    private String complemento;
    private String logradouro;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer cidadeIbge;
    private OrigemCep origem;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
