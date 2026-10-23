package bo.quispe.edwin.application.acluser.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.domain.acluser.auth.JwtTokenProviderRepository;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;

@Service 
public class LogoutUseCase {

    private final UserRepository userRepository;
    private final JwtTokenProviderRepository jwtTokenProviderRepository;
    
    public LogoutUseCase(UserRepository userRepository, JwtTokenProviderRepository jwtTokenProviderRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProviderRepository = jwtTokenProviderRepository;
    }

    @Transactional
    public void execute(String rawRefreshToken) {
        // 1. Validamos que el token sea estructuralmente correcto
        if (rawRefreshToken == null || !jwtTokenProviderRepository.validateToken(rawRefreshToken)) {
            return; // Si el token ya expiró o es inválido, la sesión ya está cerrada
        }

        // 2. Extraemos el nombre de usuario codificado en el token
        String username = jwtTokenProviderRepository.extractUsername(rawRefreshToken);

        // 3. Buscamos al usuario en el repositorio
        User user = userRepository.findByUsernameWithFullAclContext(username).orElse(null);

        if (user != null) {
            // 4. LÓGICA DE NEGOCIO: Mandamos 'null' para borrar físicamente el hash del Refresh Token de la BD
            user.updateRefreshToken(null);
            
            // 5. Sincronizamos con la persistencia
            userRepository.save(user);
        }
    }
}
