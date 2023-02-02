package br.com.github.victorhugoof.api.cep.domain;

import br.com.github.victorhugoof.api.cep.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"ibge"}, callSuper = false)
@Document(collection = "cidade")
public class CidadeEntity extends BaseEntity<Integer> {

    @Id
    private Integer ibge;

    @Field
    private String nome;

    @Field
    private Estado estado;

    @Field
    @Override
    public Integer getId() {
        return getIbge();
    }
}
