package com.springboot.blog.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
		title = "Blog Application",
		version = "1.0",
		description = "Spring boot blog application rest api"
		))
public class SwaggerConfig {

}