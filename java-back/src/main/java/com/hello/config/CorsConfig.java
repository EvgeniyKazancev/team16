package com.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    public void addCorsMapping(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                    .allowedOrigins("https://www.newsishorosho.ru")
                    .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                    .allowedHeaders("Content-Type","Authorized")
                    .allowCredentials(true)
                    .maxAge(3600);
    }
}
