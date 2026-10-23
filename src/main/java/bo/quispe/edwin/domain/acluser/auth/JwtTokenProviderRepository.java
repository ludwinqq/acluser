package bo.quispe.edwin.domain.acluser.auth;

import java.util.Map;

import bo.quispe.edwin.domain.acluser.user.User;

public interface JwtTokenProviderRepository {

    /**
     * Genera el Access Token inyectando los claims del ACL 
     * (los permisos calculados que el usuario tiene habilitados).
     */
    String createAccessToken(User user, Map<String, Object> claims);

    /**
     * Genera el Refresh Token inicializado estrictamente para durar 40 minutos.
     */
    String createRefreshToken(String username);

    /**
     * Extrae el nombre de usuario (subject) de las entrañas de un token.
     */
    String extractUsername(String token);

    /**
     * Valida si el token es estructuralmente íntegro y si no ha expirado temporalmente.
     */
    boolean validateToken(String token);
}
