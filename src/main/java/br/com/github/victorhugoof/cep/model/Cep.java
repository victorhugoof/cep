package br.com.github.victorhugoof.cep.model;

import br.com.github.victorhugoof.cep.enums.OrigemCep;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

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
}
