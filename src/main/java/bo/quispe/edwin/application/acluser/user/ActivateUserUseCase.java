package bo.quispe.edwin.application.acluser.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;

@Service 
public class ActivateUserUseCase {

    private final UserRepository userRepository;

    public ActivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No existe el usuario"));
        existingUser.activate();
        userRepository.save(existingUser);
    }
}
