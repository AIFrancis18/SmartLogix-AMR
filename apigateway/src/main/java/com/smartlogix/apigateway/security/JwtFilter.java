package com.smartlogix.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.esTokenValido(token)) {

                String correo = jwtUtil.extraerCorreo(token);
                String rol = jwtUtil.extraerRol(token); // 🔥 NUEVO

                if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 🔥 ASIGNAMOS EL ROL A SPRING SECURITY
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    correo,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("Usuario autenticado: " + correo + " | Rol: " + rol);
                }

            } else {
                System.out.println("Token inválido en Gateway");
            }
        }

        filterChain.doFilter(request, response);
    }
}