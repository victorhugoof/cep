package br.com.github.victorhugoof.api.cep.domain;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cidade")
public class CidadeEntity extends BaseEntity<Integer> {

    @Id
    private Integer ibge;

    @Column
    private String nome;

    @Column
    private Estado estado;

    @Override
    public Integer getId() {
        return getIbge();
    }
}
