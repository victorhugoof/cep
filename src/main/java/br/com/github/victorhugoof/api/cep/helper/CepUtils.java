package br.com.github.victorhugoof.api.cep.helper;

import static java.util.Objects.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CepUtils {

    public static Integer parseCep(String cep) {
        if (isNull(cep)) {
            return 0;
        }
        return Integer.parseInt(StringUtils.getDigits(cep));
    }

    public static String parseCep(Integer cep) {
        if (isNull(cep)) {
            return "";
        }
        return StringUtils.leftPad(String.valueOf(cep), 8, '0');
    }

    public static String parseCepFormat(Integer cep) {
        return parseCep(cep).replaceFirst("(\\d{5})(\\d+)", "$1-$2");
    }
}
