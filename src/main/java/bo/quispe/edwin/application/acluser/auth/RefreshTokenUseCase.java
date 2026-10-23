package bo.quispe.edwin.application.acluser.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import bo.quispe.edwin.application.acluser.auth.dtos.AuthResponseDto;
import bo.quispe.edwin.domain.acluser.auth.JwtTokenProviderRepository;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.domain.enums.LoginResultStatus;
import bo.quispe.edwin.domain.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final UserRepository userRepository;
    private final JwtTokenProviderRepository jwtTokenProviderRepository;
    private final PasswordEncoder passwordEncoder;
    
    /*public RefreshTokenUseCase(UserRepository userRepository, JwtTokenProviderRepository jwtTokenProviderRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProviderRepository = jwtTokenProviderRepository;
        this.passwordEncoder = passwordEncoder;
    }*/

    public AuthResponseDto execute(String currentRawRefreshToken) {
        // 1. Validamos físicamente que el token recibido no esté expirado (límite estricto de 40 min)
        if (currentRawRefreshToken == null || !jwtTokenProviderRepository.validateToken(currentRawRefreshToken)) {
            throw new SecurityException("Sesión expirada por inactividad. Inicie sesión de nuevo.");
        }

        // 2. Extraemos el username codificado dentro del token para buscar al usuario
        String username = jwtTokenProviderRepository.extractUsername(currentRawRefreshToken);
        User user = userRepository.findByUsernameWithFullAclContext(username)
                .orElseThrow(() -> new SecurityException("Acceso denegado."));

        // 3. CONTROL DE SEGURIDAD: Verificamos que el token enviado coincida con el hash de la BD
        if ( user.getRefreshTokenHash() == null || 
            !passwordEncoder.matches(currentRawRefreshToken, user.getRefreshTokenHash())) {
            throw new SecurityException("Token de sesión inválido o ya utilizado.");
        }

        // 4. Re-calculamos los claims de permisos vigentes en memoria (usando tu propiedad real de nombre)
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());
        claims.put("permissions", user.calculateDisplayAcl().stream()
                .filter(up -> up.getStatus().name().contains("HABILITADO"))
                .map(up -> up.getPermissionName()) // Adaptado a tu propiedad real del dominio
                .collect(Collectors.toList()));
    
        // 5. EFECTO ACTIVIDAD: Generamos un par de tokens totalmente nuevos con otros 40 minutos limpios
        String newAccessToken = jwtTokenProviderRepository.createAccessToken(user, claims);
        String newRefreshToken = jwtTokenProviderRepository.createRefreshToken(user.getUsername());

        // 6. El dominio muta al usuario guardando el hash del nuevo token de refresco (Rotación de Tokens)
        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new AuthResponseDto(LoginResultStatus.AUTENTICADO, newAccessToken, newRefreshToken);
    }
}
