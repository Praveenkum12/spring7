package com.jimmy.portal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicPaths")
    List<String> publicPaths() {
        return List.of(
            "/api/auth/login/public",
            "/api/auth/register/public",
            "/api/companies/public",
            "/api/contacts/public",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v3/api-docs/**"
        );
    }

    @Bean(name = "securedPaths")
    List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

    @Bean(name = "adminPaths")
    List<String> adminPaths() {
        return List.of(
                "/api/contacts/admin",
                "/api/contacts/page/admin",
                "/api/contacts/sort/admin",
                "/api/contacts/${id}/status/admin"
        );
    }

}
