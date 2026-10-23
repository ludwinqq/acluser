package bo.quispe.edwin.application.acluser.role.dtos;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.RolePermissionStatus;

public class RolePermissionUpdateDto {
    
    private UUID permissionId;
    private RolePermissionStatus status;

    public RolePermissionUpdateDto() {}

    public RolePermissionUpdateDto(UUID permissionId, RolePermissionStatus status) {
        this.permissionId = permissionId;
        this.status = status;
    }

    public UUID getPermissionId() { return permissionId; }
    public void setPermissionId(UUID permissionId) { this.permissionId = permissionId; }
    public RolePermissionStatus getStatus() { return status; }
    public void setStatus(RolePermissionStatus status) { this.status = status; }
}