package bo.quispe.edwin.application.acluser.permission.dtos;

public class CreatePermissionDto {
    private String name;
    private String title;
    private String module;
    private String description;
    
    public CreatePermissionDto() {
    }

    public CreatePermissionDto(String name, String title, String module, String description) {
        this.name = name;
        this.title = title;
        this.module = module;
        this.description = description;
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
