package br.com.github.victorhugoof.api.cep.model;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CepError {

    private String cep;
    private ZonedDateTime dataConsulta;
}
