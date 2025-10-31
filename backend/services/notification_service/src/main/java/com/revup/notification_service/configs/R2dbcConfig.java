package com.revup.notification_service.configs;

import com.revup.notification_service.enums.NotificationType;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new NotificationTypeReadConverter());
        converters.add(new NotificationTypeWriteConverter());
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    static class NotificationTypeReadConverter implements Converter<String, NotificationType> {
        @Override
        public NotificationType convert(String source) {
            return NotificationType.valueOf(source);
        }
    }

    static class NotificationTypeWriteConverter implements Converter<NotificationType, String> {
        @Override
        public String convert(NotificationType source) {
            return source.name();
        }
    }
}
