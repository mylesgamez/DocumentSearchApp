/**
 * Web configuration class to handle cross-origin resource sharing (CORS) settings 
 * for the application's RESTful APIs. This configuration allows specific origins 
 * to interact with the API endpoints and defines which HTTP methods are allowed.
 * 
 * @author Myles Gamez
 * @version 1.0
 */
package com.documentsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_ORIGIN = "http://localhost:3000";
    private static final String API_PATH_PATTERN = "/api/**";

    /**
     * Configures CORS mappings for the application.
     * <p>
     * This configuration specifies which API endpoints have CORS support,
     * the origins that are allowed, the HTTP methods that are supported,
     * and other related settings.
     * </p>
     *
     * @param registry the CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(API_PATH_PATTERN)
                .allowedOrigins(ALLOWED_ORIGIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Provides a custom CORS filter bean.
     * <p>
     * This bean defines the CORS configurations that should be applied
     * to specific paths or endpoints in the application.
     * </p>
     *
     * @return the configured CORS filter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin(ALLOWED_ORIGIN);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration(API_PATH_PATTERN, config);
        return new CorsFilter(source);
    }
}
