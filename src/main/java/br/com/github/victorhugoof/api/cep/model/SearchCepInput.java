package br.com.github.victorhugoof.api.cep.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SearchCepInput(
        @Size(min = 8, max = 8)
        @Pattern(regexp = "[0-9]+", message = "deve conter somente números")
        @Min(value = 1)
        String cep,
        boolean force,
        boolean update
) {

}
