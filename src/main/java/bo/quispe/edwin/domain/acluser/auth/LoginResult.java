package bo.quispe.edwin.domain.acluser.auth;

import bo.quispe.edwin.domain.enums.LoginResultStatus;

public class LoginResult {

    private final LoginResultStatus status;
    private final String accessToken;
    private final String refreshToken;
    
    public LoginResult(LoginResultStatus status, String accessToken, String refreshToken) {
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Constructor para estados de bloqueo o error (Donde no se deben emitir tokens)
    public LoginResult(LoginResultStatus status) {
        this.status = status;
        this.accessToken = null;
        this.refreshToken = null;
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
