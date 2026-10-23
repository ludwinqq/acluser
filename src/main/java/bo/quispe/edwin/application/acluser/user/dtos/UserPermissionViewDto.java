package bo.quispe.edwin.application.acluser.user.dtos;

import java.util.UUID;

import bo.quispe.edwin.domain.enums.UserPermissionStatus;

public class UserPermissionViewDto {
    private final UUID permissionId;
    private final String permissionName;
    private final UserPermissionStatus status; // HABILITADO, DENEGADO, HEREDADO_HABILITADO, HEREDADO_DENEGADO

    public UserPermissionViewDto(UUID permissionId, String permissionName, UserPermissionStatus status) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.status = status;
    }

    public UUID getPermissionId() { return permissionId; }
    public String getPermissionName() { return permissionName; }
    public UserPermissionStatus getStatus() { return status; }
}
