package bo.quispe.edwin.presentation.acluser.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.quispe.edwin.application.acluser.auth.ChangePasswordSelfUseCase;
import bo.quispe.edwin.application.acluser.auth.LoginUseCase;
import bo.quispe.edwin.application.acluser.auth.LogoutUseCase;
import bo.quispe.edwin.application.acluser.auth.RefreshTokenUseCase;
import bo.quispe.edwin.application.acluser.auth.ResetPasswordByAdminUseCase;
import bo.quispe.edwin.application.acluser.auth.dtos.AuthResponseDto;
import bo.quispe.edwin.application.acluser.auth.dtos.LoginRequestDto;
import bo.quispe.edwin.application.acluser.user.dtos.ChangePasswordRequestDto;
import bo.quispe.edwin.domain.enums.LoginResultStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController 
@RequestMapping ("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ChangePasswordSelfUseCase changePasswordSelfUseCase;
    private final ResetPasswordByAdminUseCase resetPasswordByAdminUseCase;

    public AuthController(LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase,
            LogoutUseCase logoutUseCase, ChangePasswordSelfUseCase changePasswordSelfUseCase,
            ResetPasswordByAdminUseCase resetPasswordByAdminUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
        this.changePasswordSelfUseCase = changePasswordSelfUseCase;
        this.resetPasswordByAdminUseCase = resetPasswordByAdminUseCase;
    }

    /**
     * 1. INICIAR SESIÓN (LOGIN)
     * Evalúa credenciales, intercepta si requiere cambio de contraseña (devuelve CAMBIO_CONTRASENA_REQUERIDO)
     * o emite los tokens (Access Token a 5 min y Refresh Token a 40 min).
     */
    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody LoginRequestDto loginRequestDto, 
            HttpServletResponse response) {
        
        // 1. Ejecutamos el caso de uso de manera estándar
        AuthResponseDto result = loginUseCase.execute(loginRequestDto);
        
        Map<String, Object> body = new HashMap<>();
        body.put("status", result.getStatus());

        // 2. Si el usuario se autenticó exitosamente, inyectamos los tokens en Cookies HttpOnly
        if (result.getStatus() == LoginResultStatus.AUTENTICADO) {
            
            // Cookie para el Access Token (Vence en 5 minutos)
            Cookie accessCookie = new Cookie("accessToken", result.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(false); // Cambiar a true si usas HTTPS en producción
            accessCookie.setPath("/");
            accessCookie.setMaxAge(5 * 60); // 5 minutos de vigencia
            
            // Cookie para el Refresh Token (Vence en 40 minutos estrictos)
            Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(40 * 60); // 40 minutos de vigencia

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
        }

        // Devolvemos el JSON de respuesta limpio de tokens
        return body;
    }

    /**
     * 2. REFRESCAR TOKEN POR ACTIVIDAD (SLIDING EXPIRATION)
     * El frontend lo llama en segundo plano periódicamente mientras haya clics/actividad.
     * Si pasaron más de 40 minutos de inactividad, fallará y forzará el cierre técnico.
     */
    @PostMapping("/refresh")
    public Map<String, Object> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        
        if (refreshToken == null) {
            throw new SecurityException("No se encontró la cookie de sesión.");
        }

        // Ejecutamos el caso de uso enviando el token extraído de la cookie
        AuthResponseDto result = refreshTokenUseCase.execute(refreshToken);

        // Rotación de Cookies con los nuevos tiempos extendidos (40 min más de actividad)
        Cookie accessCookie = new Cookie("accessToken", result.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(5 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(40 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        Map<String, Object> body = new HashMap<>();
        body.put("status", result.getStatus());
        return body;
    }

    /**
     * 3. CAMBIO DE CONTRASEÑA OBLIGATORIO (POR EL MISMO USUARIO AL INICIAR)
     * Se invoca cuando el login previo retornó "CAMBIO_CONTRASENA_REQUERIDO".
     * Apaga la bandera 'requirePasswordChange' en el dominio y actualiza la clave.
     */
    @PutMapping("/password/change-self")
    public void changePasswordSelf(
            @RequestHeader("X-User-Username") String username, 
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        changePasswordSelfUseCase.execute(username, changePasswordRequestDto);
    }

    /**
     * 4. REINICIAR CONTRASEÑA (POR EL ADMINISTRADOR)
     * El administrador resetea la clave de un usuario y vuelve a encender 
     * obligatoriamente la bandera 'requirePasswordChange = true'.
     */
    @PutMapping("/password/reset-admin/{userId}")
    public void resetPasswordByAdmin(
            @PathVariable UUID userId, 
            @RequestParam String defaultPassword) {
        resetPasswordByAdminUseCase.execute(userId, defaultPassword);
        //return ResponseEntity.noContent().build();
    }

    /**
     * 5. CERRAR SESIÓN VOLUNTARIO (LOGOUT)
     * Borra físicamente el hash del Refresh Token de la base de datos invalidando la sesión de inmediato.
     */
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization-Refresh") String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        logoutUseCase.execute(token);
        //return ResponseEntity.noContent().build();
    }
}