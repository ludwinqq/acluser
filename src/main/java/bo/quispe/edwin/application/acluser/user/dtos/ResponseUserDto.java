package bo.quispe.edwin.application.acluser.user.dtos;

import java.util.UUID;

public class ResponseUserDto {

    private UUID id;

    private UUID roleId;

    private String username;
    
    private String email;

    private String refreshTokenHash;

    public ResponseUserDto(UUID id, String username, String email, String refreshTokenHash, UUID roleId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.refreshTokenHash = refreshTokenHash;
        this.roleId = roleId;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public UUID getRoleId() {
        return roleId;
    }

}
