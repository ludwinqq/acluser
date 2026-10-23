package bo.quispe.edwin.application.acluser.role;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import bo.quispe.edwin.application.acluser.role.dtos.RolePermissionUpdateDto;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RolePermissionChange;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;

@Service 
public class UpdateRolePermissionsUseCase {
    
    private final RoleRepository roleRepository;

    public UpdateRolePermissionsUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void execute(UUID roleId, List<RolePermissionUpdateDto> dtos) {
        // 1. Recuperamos el Agregado Completo
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe."));

        // 2. Convertimos los DTOs a Objetos de Valor del Dominio (Traducción de capas)
        List<RolePermissionChange> domainChanges = dtos.stream()
                .map(dto -> new RolePermissionChange(dto.getPermissionId(), dto.getStatus()))
                .collect(Collectors.toList());
        // 3. ENCAPSULAMIENTO: El Dominio toma las decisiones en memoria (Añade, edita o remueve de la lista)
        role.applyPermissionChanges(domainChanges);
        // 4. Persistimos el estado final del agregado. Hibernate ejecuta los INSERTS, UPDATES o DELETES
        roleRepository.save(role);
    }
}
