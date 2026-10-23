package bo.quispe.edwin.application.acluser.user;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionUpdateDto;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserPermissionChange;
import bo.quispe.edwin.domain.acluser.user.UserRepository;

@Service 
public class UpdateUserPermissionsUseCase {

    private final UserRepository userRepository;

    public UpdateUserPermissionsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional 
    public void execute(UUID userId, List<UserPermissionUpdateDto> dtos) {
        // 1. Recuperamos el Agregado del Usuario
        User user = userRepository.findByIdWithFullAclContext(userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario especificado no existe."));
        // 2. Traducimos los DTOs de entrada a Objetos de Valor del Dominio
        List<UserPermissionChange> domainChanges = dtos.stream()
                .map(dto -> new UserPermissionChange(dto.getPermissionId(), dto.getStatus()))
                .collect(Collectors.toList());
        // 3. ENCAPSULAMIENTO: El usuario limpia de su lista interna si vuelven a ser HEREDADOS
        user.applyPermissionChanges(domainChanges);

        // 4. Guardamos el agregado. Los registros desvinculados se borran automáticamente de la base de datos
        userRepository.save(user);

    }

}
