package bo.quispe.edwin.infrastructure.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import bo.quispe.edwin.infrastructure.persistence.acluser.auth.JwtAuthorizationGlobalFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration 
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthorizationGlobalFilter jwtAuthorizationGlobalFilter;

    public SecurityConfig(JwtAuthorizationGlobalFilter jwtAuthorizationGlobalFilter) {
        this.jwtAuthorizationGlobalFilter = jwtAuthorizationGlobalFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Desactivado para propósitos de API REST en práctica
        .authorizeHttpRequests(auth -> auth
            // 1. AUTENTICACIÓN (Rutas públicas del Panel de Control)
            .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
            
            // 2. 🛒 E-COMMERCE / CATÁLOGO (Rutas públicas de la Web)
            // Abrimos la puerta libre. Cualquier visitante anónimo podrá ver productos/catálogos sin tokens
            .requestMatchers("/api/public/**").permitAll() 
            
            // 3. BLOQUEO GENERAL ADMINISTRATIVO:
            // Cualquier otra petición (como tus endpoints de roles, usuarios, etc.) exige estar autenticado
            .anyRequest().authenticated()
        )
        // =========================================================================
        // MANEJO DE EXCEPCIONES GLOBALES DE SPRING SECURITY (HOMOLOGACIÓN JSON)
        // =========================================================================
        .exceptionHandling(exceptions -> exceptions
            // CASO A: Visitante anónimo que intenta saltarse la regla 3 e ingresar a rutas privadas (HTTP 401)
            .authenticationEntryPoint((request, response, authException) -> {
                sendCustomSecurityResponse(
                    response, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "401 UNAUTHORIZED", 
                    "Acceso denegado: Inicie sesión para acceder a este recurso."
                );
            })
            // CASO B: Usuario logueado pero su Rol en la matriz ACL no tiene el @PreAuthorize (HTTP 403)
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                sendCustomSecurityResponse(
                    response, 
                    HttpServletResponse.SC_FORBIDDEN, 
                    "403 FORBIDDEN", 
                    "Acceso restringido: Su rol no cuenta con los permisos necesarios para realizar esta acción."
                );
            })
        )
        // Inyectamos tu filtro global de aduana
        .addFilterBefore(jwtAuthorizationGlobalFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    /**
     * Método utilitario para inyectar el JSON corporativo exacto de tu proyecto
     */
    private void sendCustomSecurityResponse(HttpServletResponse response, int httpStatus, String codeString, String message) 
            throws java.io.IOException {
        
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=UTF-8");

        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        // Estructura idéntica a tu ApiResponse global
        String jsonResponse = String.format(
            "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"%s\",\n" +
            "  \"code\": \"%s\",\n" +
            "  \"timestamp\": \"%s\",\n" +
            "  \"data\": null\n" +
            "}", 
            message, codeString, currentTimestamp
        );

        response.getWriter().write(jsonResponse);
    }
}
