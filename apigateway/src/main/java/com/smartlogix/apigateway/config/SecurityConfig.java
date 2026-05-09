package com.smartlogix.apigateway.config;

import com.smartlogix.apigateway.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // 🔓 PUBLICO
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                .requestMatchers("/usuarios/login").permitAll()

                // 👑 ADMIN → gestiona usuarios
                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                // =========================
                // 📦 PEDIDOS
                // =========================

                // 🔹 CREAR pedido → SOLO OPERADOR
                .requestMatchers(HttpMethod.POST, "/pedidos").hasRole("OPERADOR")

                // 🔹 VER pedidos → TODOS LOS ROLES INTERNOS
                .requestMatchers(HttpMethod.GET, "/pedidos/**")
                        .hasAnyRole("ADMIN", "OPERADOR", "LOGISTICA")

                // 🔹 ACTUALIZAR estado → SOLO LOGISTICA
                .requestMatchers(HttpMethod.PUT, "/pedidos/**")
                        .hasRole("LOGISTICA")

                // =========================
                // 🚚 ENVÍOS
                // =========================

                // 🔹 VER envíos → LOGISTICA + ADMIN
                .requestMatchers(HttpMethod.GET, "/envios/**")
                        .hasAnyRole("ADMIN", "LOGISTICA")

                // 🔹 CREAR envío → SOLO LOGISTICA
                .requestMatchers(HttpMethod.POST, "/envios")
                        .hasRole("LOGISTICA")

                // 🔒 TODO LO DEMÁS
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}