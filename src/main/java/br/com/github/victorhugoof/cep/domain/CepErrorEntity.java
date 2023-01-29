package br.com.github.victorhugoof.cep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cep_error")
public class CepErrorEntity extends BaseEntity<Integer> {

    @Id
    private Integer cep;

    @Column
    private ZonedDateTime dataConsulta;

    @Override
    public Integer getId() {
        return getCep();
    }

}
