package bo.quispe.edwin.domain.acluser.role;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.RolePermissionStatus;

public class RolePermission {

    private final UUID permissionId;
    private final String permissionName;
    private RolePermissionStatus status;
    
    public RolePermission(UUID permissionId, String permissionName, RolePermissionStatus status) {
        
        if ( permissionId == null || status == null ) {
            throw new IllegalArgumentException("ID de permiso y Estado son obligatorios.");
        }
        
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.status = status;
    }

    public void updateStatus(RolePermissionStatus newStatus) {
        if ( newStatus == RolePermissionStatus.IGNORADO ) {
            throw new IllegalArgumentException("No puedes asignar IGNORADO directamente aquí. El Rol debe remover este objeto de su lista.");
        }

        this.status = newStatus;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public RolePermissionStatus getStatus() {
        return status;
    }

}
