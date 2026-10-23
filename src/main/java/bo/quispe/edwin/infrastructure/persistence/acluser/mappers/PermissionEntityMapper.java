package bo.quispe.edwin.infrastructure.persistence.acluser.mappers;

import bo.quispe.edwin.domain.acluser.permission.Permission; // Ajusta según tu paquete real de Dominio
import bo.quispe.edwin.infrastructure.persistence.acluser.permission.PermissionEntity;

public class PermissionEntityMapper {

    // JPA (Base de Datos) -> Dominio Puro
    public static Permission toDomain(PermissionEntity entity) {
        if (entity == null) return null;
        
        return new Permission(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getTitle(),
                entity.getModule()
        );
    }

    // Dominio Puro -> JPA (Base de Datos)
    public static PermissionEntity toEntity(Permission domain) {
        if (domain == null) return null;
        
        // Constructor estándar explícito
        return new PermissionEntity(
                domain.getId(),
                domain.getName(),
                domain.getTitle(),
                domain.getModule(),
                domain.getDescription()
        );
    }
}