package br.com.github.victorhugoof.cep.model;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CepError {
    private String cep;
    private ZonedDateTime dataConsulta;
}
