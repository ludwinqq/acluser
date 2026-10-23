package bo.quispe.edwin.domain.acluser.user;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.UserPermissionStatus;

public class UserPermissionChange {

    private final UUID permissionId;
    private final UserPermissionStatus targetStatus;
    
    public UserPermissionChange(UUID permissionId, UserPermissionStatus targetStatus) {
        
        if ( permissionId == null || targetStatus == null ) {
            throw new IllegalArgumentException("El ID de permiso y el estado objetivo son obligatorios.");
        }
        
        this.permissionId = permissionId;
        this.targetStatus = targetStatus;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public UserPermissionStatus getTargetStatus() {
        return targetStatus;
    }

}
