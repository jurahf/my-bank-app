package com.bank.front.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        // Строим и возвращаем цепочку фильтров безопасности
        return http.build();
    }
}