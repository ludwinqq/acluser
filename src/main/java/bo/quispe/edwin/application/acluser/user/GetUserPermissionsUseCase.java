package bo.quispe.edwin.application.acluser.user;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionViewDto;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;

@Service 
public class GetUserPermissionsUseCase {
    
    private final UserRepository userRepository;

    public GetUserPermissionsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserPermissionViewDto> execute(UUID userId) {
        // 1. Cargamos al usuario con todo su contexto jerárquico (Roles y sobreescrituras)
        User user = userRepository.findByIdWithFullAclContext(userId).orElseThrow(() -> new IllegalArgumentException("No existe user"));

        // 2. Ejecutamos el algoritmo de cálculo de herencia que vive en tu modelo de Dominio Puro
        return user.calculateDisplayAcl().stream()
            .map(up -> new UserPermissionViewDto(
                up.getPermissionId(),
                up.getPermissionName(),
                up.getStatus()
            )).collect(Collectors.toList());
    }
}
