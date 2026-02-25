package com.jicd.stockmanager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada petición.
 * Se encarga de extraer el token, validarlo y configurar el contexto de seguridad si es válido.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * Metodo interno del filtro que procesa cada petición HTTP.
     *
     * @param request La petición HTTP.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException si ocurre un error de E/S.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Peticion para: {}", request.getRequestURI());
        try {
            // 1. Extraer el token de la cabecera Authorization
            String jwt = parseJwt(request);

            log.info("Token JWT: {}", jwt);
            // 2. Validar el token usando JwtUtils
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 3. Obtener el nombre de usuario del token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Crear el objeto de autenticación con las autoridades (roles) del usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Establecer la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Usuario autenticado: {}", username);
            }
        } catch (Exception e) {
            log.error("No se pudo configurar la autenticación del usuario: {}", e.getMessage());
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la cabecera 'Authorization' de la petición.
     *
     * @param request La petición HTTP.
     * @return String el token JWT o null si no se encuentra.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Quitar el prefijo "Bearer "
        }

        return null;
    }
}
