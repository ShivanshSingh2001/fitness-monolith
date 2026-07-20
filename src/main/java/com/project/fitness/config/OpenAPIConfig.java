package com.project.fitness.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customApi(){
        return new OpenAPI().info(new Info()
                .title("Fitness Tracking API ")
                .version("v1.0").description("Production grade APIs")
                .contact(new Contact().name("Shivansh Fitness Tracker").url("http://fitnesstracker.com")
                        .email("singhshivansh814@gmail.com"))
                .license(new License().name("Apache 2.0").url("https://google.com")));
    }
}
