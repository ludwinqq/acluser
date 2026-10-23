package bo.quispe.edwin.application.acluser.permission.dtos;

import java.util.UUID;

public class ResponsePermissionDto {

    private UUID id;
    private String name;
    private String title;
    private String description;
    private String module;
    
    public ResponsePermissionDto(UUID id, String name, String title, String description, String module) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.module = module;
    }
    
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getModule() {
        return module;
    }

}
