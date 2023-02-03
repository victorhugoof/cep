package br.com.github.victorhugoof.api.cep.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T> implements Persistable<T>, Serializable {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean insert;

    @Getter
    @Field(name = "created_at")
    private ZonedDateTime createdAt;

    @Getter
    @Field(name = "updated_at")
    private ZonedDateTime updatedAt;

    public abstract T getId();

    public void asNew() {
        this.insert = true;
    }

    public void asNotNew() {
        this.insert = false;
    }

    @Override
    public boolean isNew() {
        return insert;
    }
}
