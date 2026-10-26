package bo.quispe.edwin.infrastructure.persistence.acluser.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Repository;

import bo.quispe.edwin.domain.acluser.auth.JwtTokenProviderRepository;
import bo.quispe.edwin.domain.acluser.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Repository 
public class JwtTokenProviderAdapter implements JwtTokenProviderRepository{

    // Clave secreta fija para desarrollo (Mínimo 32 caracteres)
    private final String SECRET_KEY_STRING = "mi_clave_secreta_super_segura_de_32_caracteres_minimo";
    private final SecretKey secretKey;

    private final long ACCESS_TOKEN_VALIDITY = 40 * 60 * 1000;   // 5 minutos
    private final long REFRESH_TOKEN_VALIDITY = 40 * 60 * 1000; // 40 minutos estrictos por actividad

    public JwtTokenProviderAdapter() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createAccessToken(User user, Map<String, Object> claims) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String createRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY); // Límite estricto de inactividad

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // El token expiró o la firma es inválida
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
