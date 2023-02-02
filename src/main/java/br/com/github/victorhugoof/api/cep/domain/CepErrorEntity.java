package br.com.github.victorhugoof.api.cep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"cep"}, callSuper = false)
@Document(collection = "cep_error")
public class CepErrorEntity extends BaseEntity<Integer> {

    @Id
    private Integer cep;

    @Field
    private ZonedDateTime dataConsulta;

    @Field
    @Override
    public Integer getId() {
        return getCep();
    }

}
