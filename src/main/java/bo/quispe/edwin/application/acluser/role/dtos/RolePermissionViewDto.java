package bo.quispe.edwin.application.acluser.role.dtos;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.RolePermissionStatus;

public class RolePermissionViewDto {
    private final UUID permissionId;
    private final String permissionName;
    private final RolePermissionStatus status; // HABILITADO, DENEGADO o IGNORADO

    public RolePermissionViewDto(UUID permissionId, String permissionName, RolePermissionStatus status) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.status = status;
    }

    // Getters estándar
    public UUID getPermissionId() { return permissionId; }
    public String getPermissionName() { return permissionName; }
    public RolePermissionStatus getStatus() { return status; }
}
