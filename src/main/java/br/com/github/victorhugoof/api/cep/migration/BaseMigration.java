package br.com.github.victorhugoof.api.cep.migration;

import reactor.core.CorePublisher;

public interface BaseMigration {
    String SEPARATOR = "__";

    CorePublisher<?> run();

    default String getName() {
        return getClass().getSimpleName();
    }

    default Integer getVersion() {
        var name = getName();
        return Integer.parseInt(name.substring(1, name.indexOf(SEPARATOR)));
    }

    default String getDescription() {
        var name = getName();
        return name.substring(name.indexOf(SEPARATOR) + SEPARATOR.length());
    }
}
