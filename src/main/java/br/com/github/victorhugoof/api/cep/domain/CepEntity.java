package br.com.github.victorhugoof.api.cep.domain;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cep")
public class CepEntity extends BaseEntity<Integer> {

    @Id
    private Integer cep;

    @Column
    private String bairro;

    @Column
    private String complemento;

    @Column
    private String logradouro;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Column("cidade_ibge")
    private Integer cidadeIbge;

    @Column
    private OrigemCep origem;

    @Override
    public Integer getId() {
        return getCep();
    }
}
