package br.com.github.victorhugoof.api.cep.model;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class CepCompleto {
    private String cep;
    private String bairro;
    private String complemento;
    private String logradouro;
    private Cidade cidade;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private OrigemCep origem;
    private ZonedDateTime dataCadastro;
    private ZonedDateTime dataAtualizacao;
}
