package br.com.github.victorhugoof.cep.router;

import br.com.github.victorhugoof.cep.handler.CepHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class CepRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CepHandler handler) {
        return RouterFunctions
                .route(GET("/ceps/{cep}"), handler::findByCep);
    }
}
