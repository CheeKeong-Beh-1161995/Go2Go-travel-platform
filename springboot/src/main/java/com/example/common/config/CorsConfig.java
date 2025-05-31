package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 Set the source address for access
        corsConfiguration.addAllowedHeader("*"); // 2 Set the request header for the access source
        corsConfiguration.addAllowedMethod("*"); // 3 Set the request method for the access source
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 Configure cross-origin settings for the interface
        return new CorsFilter(source);
    }
}