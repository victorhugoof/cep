package br.com.github.victorhugoof.cep.integration;

import br.com.github.victorhugoof.cep.enums.Estado;
import br.com.github.victorhugoof.cep.enums.OrigemCep;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CepApi {

    private Integer cep;
    private String bairro;
    private String logradouro;
    private String complemento;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Cidade cidade;
    private OrigemCep origem;

    @Data
    @Builder
    public static final class Cidade {
        private Integer ibge;
        private String nome;
        private Estado estado;
    }
}
