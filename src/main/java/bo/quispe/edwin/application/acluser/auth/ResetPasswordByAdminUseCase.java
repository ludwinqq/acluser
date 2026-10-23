package bo.quispe.edwin.application.acluser.auth;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.domain.utils.PasswordEncoder;

@Service 
public class ResetPasswordByAdminUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ResetPasswordByAdminUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(UUID userId, String defaultPassword) {
        // 1. Buscamos al usuario por su ID
        User user = userRepository.findByIdWithFullAclContext(userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario especificado no existe."));

        // 2. Cambiamos la contraseña a la temporal y encendemos la bandera de cambio obligatorio
        // Usamos el comportamiento rico del dominio
        user.resetPasswordRequired(defaultPassword, passwordEncoder);
        
        // 3. Guardamos los cambios
        userRepository.save(user);
    }
}
