package br.com.github.victorhugoof.api.cep.model;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cidade {

    private Integer ibge;
    private String nome;
    private Estado estado;
}
