package com.jicd.stockmanager.security.jwt;

import com.jicd.stockmanager.security.configs.JwtProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Clase de utilidad para el manejo de JSON Web Tokens (JWT).
 * Proporciona métodos para generar, leer y validar tokens.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    // Importa la configuración de JWT desde app.yaml mediante JwtProperties
    private final JwtProperties properties;

    /**
     * Genera un token JWT basado en la autenticación del usuario.
     * Incluye el nombre de usuario y sus roles como claims.
     *
     * @param authentication El objeto de autenticación de Spring Security.
     * @return String el token JWT generado.
     */
    public String generateToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        // Obtenemos los roles del usuario y los convertimos a una lista de strings
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userPrincipal.getUsername()) // Identificador del usuario
                .claim("roles", roles)               // Añadimos los roles como claim personalizado
                .issuedAt(new Date())                // Fecha de emisión
                .expiration(new Date(System.currentTimeMillis() + properties.getExpiration())) // Fecha de expiración
                .signWith(getSigningKey())           // Firma del token con la clave secreta
                .compact();
    }

    /**
     * Obtiene el nombre de usuario (subject) contenido en el token JWT.
     *
     * @param token El token JWT.
     * @return String el nombre de usuario.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valida si un token JWT es legal, su firma es correcta y no ha expirado.
     *
     * @param authToken El token JWT a validar.
     * @return boolean true si es válido, false en caso contrario.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            log.error("Error al validar el token JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("La cadena de claims JWT está vacía: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene la clave de firma a partir de la clave secreta configurada.
     *
     * @return SecretKey la clave para firmar/verificar el JWT.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = properties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
