package br.com.github.victorhugoof.api.cep.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public abstract class BaseEntity<T> implements Persistable<T>, Serializable {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean insert;

    @Getter
    @CreatedDate
    @Field(name = "created_at")
    private ZonedDateTime createdAt;

    @Getter
    @LastModifiedDate
    @Field(name = "updated_at")
    private ZonedDateTime updatedAt;

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
