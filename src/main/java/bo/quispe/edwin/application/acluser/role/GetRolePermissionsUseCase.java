package bo.quispe.edwin.application.acluser.role;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.role.dtos.RolePermissionViewDto;
import bo.quispe.edwin.domain.acluser.permission.Permission;
import bo.quispe.edwin.domain.acluser.permission.PermissionRepository;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RolePermission;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;
import bo.quispe.edwin.domain.enums.RolePermissionStatus;

@Service 
public class GetRolePermissionsUseCase {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    public GetRolePermissionsUseCase(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional (readOnly = true)
    public List<RolePermissionViewDto> execute(UUID roleId) {
        // 1. Validamos que el rol exista y cargamos su contexto
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("No existe rol"));
        // 2. Traemos todo el catalogo global de permisos del sistema
        List<Permission> allPermissions = permissionRepository.findAll();
        // 3. Mapeamos las asignaciones fisicas actuales del Rol a un mapa de busqueda rapida [ID -> ESTADO]
        Map<UUID, RolePermissionStatus> assignedMap = role.getAssignedPermissions().stream()
                .collect(Collectors.toMap(RolePermission::getPermissionId, RolePermission::getStatus));
        // 4. Hacemos el cruce dinámico para armar la vista final de la pantalla
        return allPermissions.stream().map(p -> {
            // Regla: Si existe en la intermedia toma su estado, si no, por defecto es IGNORADO
            RolePermissionStatus status = assignedMap.getOrDefault(p.getId(), RolePermissionStatus.IGNORADO);
            return new RolePermissionViewDto(p.getId(), p.getName(), status);
        }).collect(Collectors.toList());
    }
}
