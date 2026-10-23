package bo.quispe.edwin.domain.acluser.permission;

import java.time.LocalDateTime;
import java.util.UUID;

public class Permission {

    private final UUID id;
    private String name;
    private String description;
    private String title;
    private String module;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Permission(UUID id, String name, String description, String title, String module) {
        
        if ( name == null || name.isBlank() ) {
            throw new IllegalArgumentException("Requsito el nombre");
        }

        if ( title == null || title.isBlank() ) {
            throw new IllegalArgumentException("Requsito el title");
        }

        if ( module == null || module.isBlank() ) {
            throw new IllegalArgumentException("Requsito el module");
        }
        
        this.id = id;
        this.status = status == null ? 1 : status;
        this.name = name.trim();
        this.description = description;
        this.title = title;
        this.module = module;
    }

    public void changeName(String name) {
        if ( name != null && !name.isEmpty() ) {
            this.name = name.strip();
        }
    }

    public void changeTitle(String title) {
        if ( title != null && !title.isEmpty() ) {
            this.title = title.strip();
        }
    }

    public void changeModule(String module) {
        if ( module != null && !module.isEmpty() ) {
            this.module = module.strip();
        }
    }

    public void changeDescription(String description) {
        if ( description != null && !description.isEmpty() ) {
            this.description = description.strip();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getModule() {
        return module;
    }
    
}
