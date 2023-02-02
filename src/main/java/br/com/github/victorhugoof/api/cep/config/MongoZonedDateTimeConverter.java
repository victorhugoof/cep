package br.com.github.victorhugoof.api.cep.config;

import org.springframework.core.convert.converter.Converter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class MongoZonedDateTimeConverter {

    public static class ReadConverter implements Converter<Date, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Date date) {
            return date.toInstant().atZone(ZoneOffset.UTC);
        }
    }

    public static class WriteConverter implements Converter<ZonedDateTime, Date> {
        @Override
        public Date convert(ZonedDateTime zonedDateTime) {
            return Date.from(zonedDateTime.toInstant());
        }
    }
}
