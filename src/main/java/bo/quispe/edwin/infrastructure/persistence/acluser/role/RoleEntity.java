package bo.quispe.edwin.infrastructure.persistence.acluser.role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bo.quispe.edwin.infrastructure.persistence.BaseAuditableData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "roles")
public class RoleEntity extends BaseAuditableData {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "name", nullable = false, unique = true)
    private String name;

    @Column (name = "description")
    private String description;

    @OneToMany (mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RolePermissionEntity> assignedPermissions = new ArrayList<>();

    public RoleEntity() {
    }

    public RoleEntity(UUID id, String name, String description, List<RolePermissionEntity> assignedPermissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assignedPermissions = assignedPermissions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RolePermissionEntity> getAssignedPermissions() {
        return assignedPermissions;
    }

    public void setAssignedPermissions(List<RolePermissionEntity> assignedPermissions) {
        this.assignedPermissions = assignedPermissions;
    }
   
}
