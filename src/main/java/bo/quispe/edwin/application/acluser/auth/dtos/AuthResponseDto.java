package bo.quispe.edwin.application.acluser.auth.dtos;

import bo.quispe.edwin.domain.enums.LoginResultStatus;

public class AuthResponseDto {

    private final LoginResultStatus status;
    private final String accessToken;
    private final String refreshToken;
    
    public AuthResponseDto(LoginResultStatus status, String accessToken, String refreshToken) {
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResultStatus getStatus() {
        return status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
