package bo.quispe.edwin.infrastructure.persistence.acluser.mappers;

import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserPermission;
import bo.quispe.edwin.domain.enums.UserPermissionStatus;
import bo.quispe.edwin.infrastructure.persistence.acluser.permission.PermissionEntity;
import bo.quispe.edwin.infrastructure.persistence.acluser.user.UserEntity;
import bo.quispe.edwin.infrastructure.persistence.acluser.user.UserPermissionEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserEntityMapper {

    // JPA (Base de Datos) -> Dominio Puro
    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        // 1. Mapeamos la colección intermedia física a objetos puros del dominio
        List<UserPermission> domainCustomPermissions = entity.getCustomPermissions().stream()
                .map(up -> new UserPermission(
                        up.getPermission().getId(),
                        up.getPermission().getName(),
                        UserPermissionStatus.valueOf(up.getStatus())
                )).collect(Collectors.toList());

        // 2. Instanciamos el Agregado de Dominio Rico por medio de su constructor obligatorio
        return new User(
                entity.getId(),
                RoleEntityMapper.toDomain(entity.getRole()),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRequirePasswordChange(),
                entity.getStatus(),
                entity.getFailedAttempts(),
                entity.getLockoutUntil(),
                domainCustomPermissions
        );
    }

    
    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setRole(RoleEntityMapper.toEntity(domain.getRole()));
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword()); 
        entity.setRequirePasswordChange(domain.getRequirePasswordChange());
        entity.setStatus(domain.getStatus());
        
        //  NUEVO: Guardamos el estado del castigo calculado por el dominio
        entity.setFailedAttempts(domain.getFailedAttempts());
        entity.setLockoutUntil(domain.getLockoutUntil());
    
        List<UserPermissionEntity> permissionsEntities = domain.getCustomPermissions().stream()
                .map(dp -> {
                    // 1. Generamos el proxy del permiso
                    PermissionEntity permissionProxy = new PermissionEntity();
                    permissionProxy.setId(dp.getPermissionId());

                    // 2. Instanciamos la relación intermedia usando el constructor vacío
                    UserPermissionEntity upEntity = new UserPermissionEntity();
                    upEntity.setUser(entity);                // Vincula al padre Usuario
                    upEntity.setPermission(permissionProxy); // Vincula al proxy Permiso
                    upEntity.setStatus(dp.getStatus().name()); // Estado HABILITADO o DENEGADO
                    
                    // El ID se queda libre para la autogeneración de la BD
                    return upEntity;
                }).collect(Collectors.toList());
        // Asignamos la colección final procesada a la entidad
        entity.setCustomPermissions(permissionsEntities);
        
        return entity;
    }
}