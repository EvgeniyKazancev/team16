package com.hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
      registry.addMapping("/**")
                .allowedOrigins("https://www.newsishorosho.ru")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("Content-Type","Authorized", "text/plain")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
