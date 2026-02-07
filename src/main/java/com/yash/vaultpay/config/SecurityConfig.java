package com.yash.vaultpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for development so Postman works
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/account/**").permitAll() // Allow testing deposits, Allow Login/Register without token
                        .anyRequest().authenticated() // Lock everything else
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // The industry standard hashing
    }
}