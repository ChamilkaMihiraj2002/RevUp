package com.revup.project_service.configs;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import com.revup.project_service.enums.ProjectStatus;
import com.revup.project_service.enums.MilestoneStatus;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ProjectStatusReadConverter());
        converters.add(new ProjectStatusWriteConverter());
        converters.add(new MilestoneStatusReadConverter());
        converters.add(new MilestoneStatusWriteConverter());
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    static class ProjectStatusReadConverter implements Converter<String, ProjectStatus> {
        @Override
        public ProjectStatus convert(String source) {
            return ProjectStatus.valueOf(source);
        }
    }

    static class ProjectStatusWriteConverter implements Converter<ProjectStatus, String> {
        @Override
        public String convert(ProjectStatus source) {
            return source.name();
        }
    }
    
    static class MilestoneStatusReadConverter implements Converter<String, MilestoneStatus> {
        @Override
        public MilestoneStatus convert(String source) {
            return MilestoneStatus.valueOf(source);
        }
    }

    static class MilestoneStatusWriteConverter implements Converter<MilestoneStatus, String> {
        @Override
        public String convert(MilestoneStatus source) {
            return source.name();
        }
    }
}
