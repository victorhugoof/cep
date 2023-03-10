package br.com.github.victorhugoof.api.cep.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static org.apache.commons.lang3.ObjectUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Estado {
    AC(12, "Acre"),
    AL(27, "Alagoas"),
    AM(13, "Amazonas"),
    AP(16, "Amapá"),
    BA(29, "Bahia"),
    CE(23, "Ceará"),
    DF(53, "Distrito Federal"),
    ES(32, "Espírito Santo"),
    GO(52, "Goiás"),
    MA(21, "Maranhão"),
    MG(31, "Minas Gerais"),
    MS(50, "Mato Grosso do Sul"),
    MT(51, "Mato Grosso"),
    PA(15, "Pará"),
    PB(25, "Paraíba"),
    PE(26, "Pernambuco"),
    PI(22, "Piauí"),
    PR(41, "Paraná"),
    RJ(33, "Rio de Janeiro"),
    RN(24, "Rio Grande do Norte"),
    RO(11, "Rondônia"),
    RR(14, "Roraima"),
    RS(43, "Rio Grande do Sul"),
    SC(42, "Santa Catarina"),
    SE(28, "Sergipe"),
    SP(35, "São Paulo"),
    TO(17, "Tocantins");

    private final Integer ibge;
    private final String nome;

    public static Estado find(String nomeOrUf) {
        var term = stripAccents(firstNonNull(nomeOrUf, "").trim().toUpperCase());

        for (var item : values()) {
            if (item.getUf().equalsIgnoreCase(term)) {
                return item;
            }

            var nomeUf = stripAccents(firstNonNull(item.getNome(), "").trim().toUpperCase());
            if (nomeUf.equalsIgnoreCase(term)) {
                return item;
            }
        }

        throw new IllegalArgumentException("No enum constant for %s".formatted(nomeOrUf));
    }

    public String getUf() {
        return name();
    }
}
