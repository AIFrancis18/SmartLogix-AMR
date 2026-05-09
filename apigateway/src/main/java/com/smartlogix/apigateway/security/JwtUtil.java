package com.smartlogix.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private final String SECRET = "clave_super_secreta_123456789_clave_larga_segura_2026";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔥 MÉTODO CENTRALIZADO (evita repetir código)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔥 EXTRAER CORREO
    public String extraerCorreo(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 NUEVO: EXTRAER ROL
    public String extraerRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // 🔥 VALIDAR TOKEN
    public boolean esTokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }
}