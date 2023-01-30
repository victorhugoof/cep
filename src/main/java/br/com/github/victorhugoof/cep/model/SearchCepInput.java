package br.com.github.victorhugoof.cep.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SearchCepInput(
        @Size(min = 8, max = 8) @Pattern(regexp = "[0-9]+", message = "deve conter somente números") String cep,
        boolean force
) {

}
