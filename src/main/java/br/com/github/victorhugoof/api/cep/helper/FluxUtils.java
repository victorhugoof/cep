package br.com.github.victorhugoof.api.cep.helper;

import static java.util.Objects.*;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@UtilityClass
public class FluxUtils {

    public static Flux<String> getLinesFromResource(String resource) {
        return Flux.using(
                () -> new InputStreamReader(requireNonNull(FluxUtils.class.getResourceAsStream(resource))),
                reader -> Flux.fromStream(new BufferedReader(reader).lines()),
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
