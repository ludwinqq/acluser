package bo.quispe.edwin.domain.acluser.role;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.RolePermissionStatus;

public class RolePermissionChange {

    private final UUID permissionId;
    private final RolePermissionStatus targetStatus;
    
    public RolePermissionChange(UUID permissionId, RolePermissionStatus targetStatus) {
        
        if ( permissionId == null || targetStatus == null ) {
            throw new IllegalArgumentException("El ID de permiso y el estado objetivo son obligatorios.");
        }

        this.permissionId = permissionId;
        this.targetStatus = targetStatus;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public RolePermissionStatus getTargetStatus() {
        return targetStatus;
    }
    
}
