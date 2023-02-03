package br.com.github.victorhugoof.api.cep.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@Document(collection = "migration_history")
public class MigrationHistoryEntity extends BaseEntity<String> {

    @Id
    private String name;

    @Field
    private Integer version;

    @Field
    private String description;

    @Field
    private ZonedDateTime installedOn;

    @Field
    private Long executionTime;

    @Field
    private boolean success;

    @Override
    public String getId() {
        return name;
    }
}
