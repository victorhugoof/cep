package br.com.github.victorhugoof.cep.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Data
public abstract class BaseEntity<T> implements Persistable<T>, Serializable {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean insert;

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
