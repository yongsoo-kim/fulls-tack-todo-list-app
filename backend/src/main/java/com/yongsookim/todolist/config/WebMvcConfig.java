package com.yongsookim.todolist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final static int MAX_AGE_SECONDS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods(
                RequestMethod.GET.name(), RequestMethod.PUT.name(),
                RequestMethod.POST.name(), RequestMethod.DELETE.name(),
                RequestMethod.PATCH.name(), RequestMethod.OPTIONS.name()
            )
            .allowedHeaders("*")
            .exposedHeaders("X-Total-Count")
            .allowCredentials(true)
            .maxAge(MAX_AGE_SECONDS);
    }
}
