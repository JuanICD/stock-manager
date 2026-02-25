package com.jicd.stockmanager.security.configs;

import com.jicd.stockmanager.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración de seguridad principal para la aplicación.
 * Define la cadena de filtros, la gestión de sesiones y el codificador de contraseñas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Define el bean para el codificador de contraseñas.
     * Utiliza BCrypt, que es el estándar actual para el almacenamiento seguro de contraseñas.
     *
     * @return PasswordEncoder instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expone el AuthenticationManager como un bean.
     * Es necesario para realizar el proceso de autenticación manual en el controlador.
     *
     * @param configuration la configuración de autenticación de Spring.
     * @return AuthenticationManager.
     * @throws Exception si ocurre un error.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * @param http el objeto HttpSecurity para configurar la seguridad web.
     * @return SecurityFilterChain la cadena de filtros configurada.
     * @throws Exception si ocurre un error.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF ya que usamos JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // Configurar la gestión de sesiones como sin estado (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Definir reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso público a los endpoints de autenticación
                        .requestMatchers("/auth/**").permitAll()
                        // Ejemplo de protección por rol: solo ADMIN puede acceder a ciertos endpoints (si existieran)
                        // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Añadir el filtro de JWT antes del filtro de autenticación por nombre de usuario y contraseña
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


