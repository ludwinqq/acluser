package bo.quispe.edwin.application.acluser.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.auth.dtos.AuthResponseDto;
import bo.quispe.edwin.application.acluser.auth.dtos.LoginRequestDto;
import bo.quispe.edwin.domain.acluser.auth.JwtTokenProviderRepository;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.domain.enums.LoginResultStatus;
import bo.quispe.edwin.domain.utils.PasswordEncoder;

@Service 
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderRepository jwtTokenProviderRepository;

    public LoginUseCase(UserRepository userRepository, 
                        PasswordEncoder passwordEncoder, 
                        JwtTokenProviderRepository jwtTokenProviderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProviderRepository = jwtTokenProviderRepository;
    }
    
    @Transactional 
    public AuthResponseDto execute(LoginRequestDto dto) {
        // 1. Buscamos el agregado del usuario trayendo todo el árbol jerárquico ACL
        User user = userRepository.findByUsernameWithFullAclContext(dto.getUsername()).orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        boolean isAutenticado;
        try {
            // 2. Intentamos autenticar. Si llega al 5to fallo, el dominio mutará a status=2 
            // y lanzará un SecurityException que capturaremos abajo.
            isAutenticado = user.authenticate(dto.getPassword(), passwordEncoder);
            
        } catch (SecurityException e) {
            // CAPTURA DEL BLOQUEO: Si el dominio lanzó el bloqueo de los 15 minutos,
            // guardamos físicamente el status=2 y la fecha de castigo en la BD antes de relanzar el error.
            userRepository.save(user);
            throw e; // Redirige el mensaje exacto a tu ApiResponseAdvice corporativo
        }

        // 3. ESCENARIO DE CONTRASEÑA INCORRECTA (Intentos 1 al 4)
        if (!isAutenticado) {
            // Guardamos el incremento del contador (failed_attempts + 1) en la base de datos
            userRepository.save(user); 
            return new AuthResponseDto(LoginResultStatus.CREDENCIALES_INVALIDAS, null, null);
        }

        // 4. ESCENARIO DE CAMBIO OBLIGATORIO DE CONTRASEÑA
        if (user.getRequirePasswordChange()) {
            userRepository.save(user); // Guarda por si se limpiaron intentos fallidos previos
            return new AuthResponseDto(LoginResultStatus.CAMBIO_CONTRASENA_REQUERIDO, null, null);
        }

        /*// 2. El Dominio valida la contraseña y el estado activo en memoria
        if ( !user.authenticate(dto.getPassword(), passwordEncoder) ) {
            return new AuthResponseDto(LoginResultStatus.CREDENCIALES_INVALIDAS, null, null);
        }

        // 3. REQUERIMIENTO: Validar cambio obligatorio de contraseña
        if ( user.getRequirePasswordChange() ) {
            return new AuthResponseDto(LoginResultStatus.CAMBIO_CONTRASENA_REQUERIDO, null, null);
        }*/

        // 4. Armamos los Claims del Access Token cargando sus permisos calculados de forma dinámica
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());

        // Filtramos para meter en el token únicamente los slugs de permisos que terminaron HABILITADOS
        claims.put("permissions", user.calculateDisplayAcl().stream()
                .filter(up -> up.getStatus().name().contains("HABILITADO"))
                .map(up -> up.getPermissionName())
                .collect(Collectors.toList()));
        
        // 5. El Token Provider genera las cadenas JWT (El Refresh Token se configura a 40 min en la infra)
        String accessToken = jwtTokenProviderRepository.createAccessToken(user, claims);
        String refreshToken = jwtTokenProviderRepository.createRefreshToken(user.getUsername());

        // 6. El Dominio muta al usuario asignándole el nuevo Hash del Refresh Token
        //user.updateRefreshToken(refreshToken, passwordEncoder);
        user.updateRefreshToken(refreshToken);

        
        // 7. Persistimos el cambio de la sesión en la base de datos
        userRepository.save(user);

        return new AuthResponseDto(LoginResultStatus.AUTENTICADO, accessToken, refreshToken);
    }
}
