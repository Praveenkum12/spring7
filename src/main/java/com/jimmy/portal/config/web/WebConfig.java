package com.jimmy.portal.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy.portal.logging.RequestLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .useMediaTypeParameter(
                        MediaType.parseMediaType("application/vnd.eazyapp+json"),
                        "v"
                )
                .addSupportedVersions("1.0", "2.0", "3.0")
                .setDefaultVersion("1.0");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", _ -> true);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter(
            ObjectMapper objectMapper
    ) {

        FilterRegistrationBean<RequestLoggingFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(
                new RequestLoggingFilter(objectMapper)
        );

        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        return registration;
    }
}