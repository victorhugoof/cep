package br.com.github.victorhugoof.api.cep.domain;

import br.com.github.victorhugoof.api.cep.enums.OrigemCep;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"cep"}, callSuper = false)
@Document(collection = "cep")
public class CepEntity extends BaseEntity<Integer> {

    @Id
    private Integer cep;

    @Field
    private String bairro;

    @Field
    private String complemento;

    @Field
    private String logradouro;

    @Field
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Point point;

    @Field("cidade_ibge")
    private Integer cidadeIbge;

    @Field
    private OrigemCep origem;

    @Field
    @Override
    public Integer getId() {
        return getCep();
    }
}
