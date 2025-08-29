package fr.norsys.documentai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// Remove @EnableWebMvc - it can cause issues with Spring Boot
public class WebConfig implements WebMvcConfigurer {

    @Value("${front.url}")
    private String frontUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Add OPTIONS
                .allowedOrigins(frontUrl)
                .allowedHeaders("*")
                .allowCredentials(true) // Essential for JWT authentication
                .maxAge(3600);
    }
}