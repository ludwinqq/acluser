package bo.quispe.edwin.domain.acluser.user;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.UserPermissionStatus;

public class UserPermission {

    private final UUID permissionId;
    private final String permissionName;
    private UserPermissionStatus status;
    
    public UserPermission(UUID permissionId, String permissionName, UserPermissionStatus status) {
        
        if (permissionId == null || status == null) {
            throw new IllegalArgumentException("ID de permiso y Estado son obligatorios.");
        }
        
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.status = status;
    }

    // El escenario B aplicado a la mutacion del estado del usuario
    public void overrideStatus(UserPermissionStatus newStatus) {
        if ( newStatus == UserPermissionStatus.HEREDADO_HABILITADO || newStatus == UserPermissionStatus.HEREDADO_DENEGADO ) {
            throw new IllegalArgumentException("No puedes asignar un estado heredado directamente a la relación física.");
        }

        this.status = newStatus;
    }

    public UUID getPermissionId() { return permissionId; }
    public String getPermissionName() { return permissionName; }
    public UserPermissionStatus getStatus() { return status; }
    
}
