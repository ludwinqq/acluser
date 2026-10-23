package bo.quispe.edwin.application.acluser.user.dtos;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.UserPermissionStatus;

public class UserPermissionUpdateDto {
    private UUID permissionId;
    private UserPermissionStatus status;

    public UserPermissionUpdateDto() {}

    public UserPermissionUpdateDto(UUID permissionId, UserPermissionStatus status) {
        this.permissionId = permissionId;
        this.status = status;
    }

    public UUID getPermissionId() { return permissionId; }
    public void setPermissionId(UUID permissionId) { this.permissionId = permissionId; }
    public UserPermissionStatus getStatus() { return status; }
    public void setStatus(UserPermissionStatus status) { this.status = status; }
}