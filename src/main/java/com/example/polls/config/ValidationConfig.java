package com.example.polls.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.validation.Validator;

@Configuration
@Lazy
public class ValidationConfig {

    @Bean
    @Lazy
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(final Validator validator) {
        return hibernateProperties -> hibernateProperties.put("javax.persistence.validation.factory", validator);
    }
}
