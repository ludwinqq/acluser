package bo.quispe.edwin.infrastructure.persistence.acluser.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import bo.quispe.edwin.domain.acluser.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationGlobalFilter extends OncePerRequestFilter {

    private final JwtTokenProviderAdapter jwtTokenProviderAdapter;
    private final UserRepository userRepository;

    public JwtAuthorizationGlobalFilter(JwtTokenProviderAdapter jwtTokenProviderAdapter, UserRepository userRepository) {
        this.jwtTokenProviderAdapter = jwtTokenProviderAdapter;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. OMITIR FILTRO ÚNICAMENTE EN EL LOGIN INICIAL
        if (path.contains("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String usernameToVerify = null;

        // 2. ESCENARIO A: El usuario navega normal (Tiene Cookie accessToken)
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtTokenProviderAdapter.validateToken(token)) {
            usernameToVerify = jwtTokenProviderAdapter.extractUsername(token);
        }

        // 3. ESCENARIO B: El usuario está en cambio obligatorio (No tiene token, usa cabecera X-User-Username)
        if (usernameToVerify == null) {
            usernameToVerify = request.getHeader("X-User-Username");
        }

        // =========================================================================
        //  EL CANDADO GENERAL: Si identificamos de quién es la petición, validamos su estado
        // =========================================================================
        if (usernameToVerify != null) {
            Integer status = userRepository.findStatusByUsername(usernameToVerify).orElse(null);

            if (status != null && status != 1) { // Cuentas dadas de baja en caliente (status = 0)
                
                // Limpiamos las cookies del navegador por si acaso
                clearCookies(response);

                // Bloqueamos la petición y respondemos con tu JSON corporativo estructurado
                sendCustomForbiddenResponse(response, path);
                return; //  FRENADO ABSOLUTO: La petición muere aquí, jamás llegará al Caso de Uso
            }
        }

        // Si el usuario está activo o es una ruta pública sin identificar, continúa normalmente
        filterChain.doFilter(request, response);
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie clearAccess = new Cookie("accessToken", null);
        clearAccess.setPath("/");
        clearAccess.setMaxAge(0);
        
        Cookie clearRefresh = new Cookie("refreshToken", null);
        clearRefresh.setPath("/");
        clearRefresh.setMaxAge(0);

        response.addCookie(clearAccess);
        response.addCookie(clearRefresh);
    }

    private void sendCustomForbiddenResponse(HttpServletResponse response, String path) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        response.setContentType("application/json;charset=UTF-8");

        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        String jsonResponse = String.format(
            "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"Su cuenta ha sido desactivada por el administrador.\",\n" +
            "  \"code\": \"403 FORBIDDEN\",\n" +
            "  \"timestamp\": \"%s\",\n" +
            "  \"data\": null\n" +
            "}", 
            currentTimestamp
        );

        response.getWriter().write(jsonResponse);
    }
}