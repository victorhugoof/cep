package br.com.github.victorhugoof.api.cep.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity<T> implements Persistable<T>, Serializable {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean insert;

    @Getter
    @Column("created_at")
    private LocalDateTime createdAt;

    @Getter
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public void hasNew() {
        this.insert = true;
    }

    public void hasNotNew() {
        this.insert = false;
    }

    @Override
    public boolean isNew() {
        return insert;
    }
}
