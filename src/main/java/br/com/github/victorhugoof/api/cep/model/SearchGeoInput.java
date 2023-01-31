package br.com.github.victorhugoof.api.cep.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SearchGeoInput(
        @NotNull BigDecimal latitude,
        @NotNull BigDecimal longitude
) {

}
