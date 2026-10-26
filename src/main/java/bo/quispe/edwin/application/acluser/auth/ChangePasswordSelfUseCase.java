package bo.quispe.edwin.application.acluser.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.user.dtos.ChangePasswordRequestDto;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.domain.utils.PasswordEncoder;

@Service 
public class ChangePasswordSelfUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ChangePasswordSelfUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(String username, ChangePasswordRequestDto dto) {
        // 1. REGLA DE PROCESO: Validar que la nueva contraseña y la confirmación coincidan
        //System.out.println(username + " - " + dto.getNewPassword() + " - " + dto.getConfirmPassword());
        if ( dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getConfirmPassword()) ) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmación no coinciden.");
        }

        User user = userRepository.findByUsernameWithFullAclContext(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // 3. ENCAPSULAMIENTO: El dominio valida la clave actual y aplica el cambio seguro
        user.updatePasswordVoluntary(dto.getCurrentPassword(), dto.getNewPassword(), passwordEncoder);
        
        // 4. Sincronizamos en la BD de forma limpia
        userRepository.save(user);
    }

}
