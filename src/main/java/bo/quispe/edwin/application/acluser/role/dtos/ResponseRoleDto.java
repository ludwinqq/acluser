package bo.quispe.edwin.application.acluser.role.dtos;

import java.util.UUID;

public class ResponseRoleDto {

    private UUID id;

    private String name;

    private String description;

    public ResponseRoleDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

}
