package bo.quispe.edwin.infrastructure.persistence.acluser.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bo.quispe.edwin.infrastructure.persistence.BaseAuditableData;
import bo.quispe.edwin.infrastructure.persistence.acluser.role.RoleEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*@Builder 
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor*/
@Entity 
@Table (name = "users")
public class UserEntity extends BaseAuditableData{

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column (name = "refresh_token_hash")
    private String refreshTokenHash;

    @Column (name = "require_password_change", nullable = false)
    private Boolean requirePasswordChange = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserPermissionEntity> customPermissions = new ArrayList<>();
    //private List<UserPermissionEntity> customPermissions;

    @Column(name = "failed_attempts", nullable = false, columnDefinition = "int default 0") // 🟢 Forzamos el nombre físico y el valor inicial en SQL
    private Integer failedAttempts = 0; 

    @Column(name = "lockout_until")
    private LocalDateTime lockoutUntil;

    public UserEntity() {
    }

    public UserEntity(UUID id, RoleEntity role, String username, String email, String password, String refreshTokenHash,
            List<UserPermissionEntity> customPermissions) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.refreshTokenHash = refreshTokenHash;
        this.requirePasswordChange = true;
        this.customPermissions = customPermissions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }

    public Boolean getRequirePasswordChange() {
        return requirePasswordChange;
    }

    public void setRequirePasswordChange(Boolean requirePasswordChange) {
        this.requirePasswordChange = requirePasswordChange;
    }

    public List<UserPermissionEntity> getCustomPermissions() {
        return customPermissions;
    }

    public void setCustomPermissions(List<UserPermissionEntity> customPermissions) {
        this.customPermissions = customPermissions;
    }

    public Integer getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(Integer failedAttempts) { this.failedAttempts = failedAttempts; }

    public LocalDateTime getLockoutUntil() { return lockoutUntil; }
    public void setLockoutUntil(LocalDateTime lockoutUntil) { this.lockoutUntil = lockoutUntil; }

}
