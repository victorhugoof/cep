package br.com.github.victorhugoof.cep.model;

import br.com.github.victorhugoof.cep.enums.OrigemCep;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CepCompleto {
    private String cep;
    private String bairro;
    private String complemento;
    private String logradouro;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Cidade cidade;
    private OrigemCep origem;
}