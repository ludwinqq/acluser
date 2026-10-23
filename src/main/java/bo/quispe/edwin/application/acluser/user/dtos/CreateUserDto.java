package bo.quispe.edwin.application.acluser.user.dtos;

import java.util.UUID;

public class CreateUserDto {

    private UUID roleId;

    private String username;

    private String email;

    private String password;

    private Integer status;

    private Boolean requirePasswordChange;
    //private String refreshTokenHash;

    public CreateUserDto() {
    }

    public CreateUserDto(UUID roleId, String username, String email, String password, Boolean requirePasswordChange, Integer status) {
    //public CreateUserDto(UUID roleId, String username, String email, String password, String refreshTokenHash) {
        this.roleId = roleId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        //this.refreshTokenHash = refreshTokenHash;
        this.requirePasswordChange = requirePasswordChange;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRequirePasswordChange() {
        return requirePasswordChange;
    }

    public void setRequirePasswordChange(Boolean requirePasswordChange) {
        this.requirePasswordChange = requirePasswordChange;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    

    /*public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }*/

}
