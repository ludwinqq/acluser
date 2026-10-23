package bo.quispe.edwin.infrastructure.persistence.acluser.permission;

import java.util.UUID;

import bo.quispe.edwin.infrastructure.persistence.BaseAuditableData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table (name = "permissions")
public class PermissionEntity extends BaseAuditableData {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "name", nullable = false, unique = true)
    private String name;

    @Column (name = "title", nullable = false, unique = true)
    private String title;

    @Column (name = "module", nullable = false)
    private String module;

    @Column (name = "description")
    private String description;

    public PermissionEntity() {
    }

    public PermissionEntity(UUID id, String name, String title, String module, String description) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.module = module;
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
