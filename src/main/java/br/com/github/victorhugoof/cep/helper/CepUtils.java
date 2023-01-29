package br.com.github.victorhugoof.cep.helper;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CepUtils {

    public static Integer parseCep(String cep) {
        return Integer.parseInt(StringUtils.getDigits(cep));
    }

    public static String parseCep(Integer cep) {
        return StringUtils.leftPad(String.valueOf(cep), 8, '0');
    }
}
