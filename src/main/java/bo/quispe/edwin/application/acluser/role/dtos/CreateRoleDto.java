package bo.quispe.edwin.application.acluser.role.dtos;

import jakarta.validation.constraints.NotBlank;

public class CreateRoleDto {
    
    @NotBlank (message = "Name required")
    private String name;
    
    private String description;

    public CreateRoleDto() {}

    public CreateRoleDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}