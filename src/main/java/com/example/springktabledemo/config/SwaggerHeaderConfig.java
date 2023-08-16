package com.example.springktabledemo.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwaggerHeaderConfig {


    @Bean
    public OpenAPI customOpenAPI(@Value("KTable Application") String serviceTitle, @Value("1.0.0") String serviceVersion) {
        return new OpenAPI()
                .components(
                        new Components()

                )
                .info(new Info()
                        .title(serviceTitle)
                        .description("API Documentation for KTable Application")
                        .version("1.0.0"));
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        return builder;
    }
}
