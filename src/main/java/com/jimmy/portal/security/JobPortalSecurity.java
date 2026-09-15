package com.jimmy.portal.security;

import com.jimmy.portal.logging.RequestLoggingFilter;
import com.jimmy.portal.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class JobPortalSecurity {

    private final List<String> publicPaths;

    private final List<String> securedPaths;

    private final List<String> adminPaths;

    public JobPortalSecurity(@Qualifier("publicPaths") List<String> publicPaths,
                             @Qualifier("securedPaths") List<String> securedPaths,
                             @Qualifier("adminPaths") List<String> adminPaths
                             ) {
        this.publicPaths = publicPaths;
        this.securedPaths = securedPaths;
        this.adminPaths = adminPaths;
    }

    @Bean
    public SecurityFilterChain  configure(HttpSecurity http) {
        http.authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(publicPaths.toArray(new String[0])).permitAll()
                            .requestMatchers(adminPaths.toArray(new String[0])).hasRole("ADMIN")
                            .requestMatchers(securedPaths.toArray(new String[0])).authenticated()
                            .anyRequest().denyAll();
                }
        );
        http.addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class);
        http.exceptionHandling(exc -> {
            exc.authenticationEntryPoint(
                    (request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("""
                            {
                                "status": 401,
                                "message": "Authentication required"
                            }
                            """);
                    }
            );

            exc.accessDeniedHandler(
                    (request, response, accessDeniedException) -> {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("""
                            {
                                "status": 403,
                                "message": "Access denied"
                            }
                            """);
                    }
            );
        });

        http.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
