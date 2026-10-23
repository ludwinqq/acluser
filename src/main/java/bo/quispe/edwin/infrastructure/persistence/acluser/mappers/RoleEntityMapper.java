package bo.quispe.edwin.infrastructure.persistence.acluser.mappers;

import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RolePermission;
import bo.quispe.edwin.domain.enums.RolePermissionStatus;
import bo.quispe.edwin.infrastructure.persistence.acluser.permission.PermissionEntity;
import bo.quispe.edwin.infrastructure.persistence.acluser.role.RoleEntity;
import bo.quispe.edwin.infrastructure.persistence.acluser.role.RolePermissionEntity;

import java.util.List;
import java.util.stream.Collectors;

public class RoleEntityMapper {

    // JPA -> Dominio
    public static Role toDomain(RoleEntity entity) {
        if (entity == null) return null;

        List<RolePermission> domainPermissions = entity.getAssignedPermissions().stream()
                .map(rp -> new RolePermission(
                        rp.getPermission().getId(),
                        rp.getPermission().getName(),
                        RolePermissionStatus.valueOf(rp.getStatus())
                )).collect(Collectors.toList());

        return new Role(entity.getId(), entity.getName(), entity.getDescription(), domainPermissions);
    }

    // Dominio -> JPA
    public static RoleEntity toEntity(Role domain) {
        if (domain == null) return null;

        // Instanciación directa por constructor estándar
        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        // Mapeo tradicional sin Builder
        List<RolePermissionEntity> permissionsEntities = domain.getAssignedPermissions().stream()
                .map(dp -> {
                // 1. Generamos el proxy del permiso
                PermissionEntity permissionProxy = new PermissionEntity();
                permissionProxy.setId(dp.getPermissionId());

                // 2. Instanciamos la relación intermedia usando el constructor vacío
                RolePermissionEntity rpEntity = new RolePermissionEntity();
                rpEntity.setRole(entity);              // Vincula al padre Rol
                rpEntity.setPermission(permissionProxy); // Vincula al proxy Permiso
                rpEntity.setStatus(dp.getStatus().name()); // Estado HABILITADO o DENEGADO
                
                // Nota: El campo ID no se setea aquí para que JPA lo autogenere en el INSERT
                return rpEntity;
                }).collect(Collectors.toList());

        entity.setAssignedPermissions(permissionsEntities);
        return entity;
    }
}
