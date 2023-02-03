package br.com.github.victorhugoof.api.cep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(final LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(new MongoZonedDateTimeConverter.ReadConverter(), new MongoZonedDateTimeConverter.WriteConverter()));
    }
}

