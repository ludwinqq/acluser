package bo.quispe.edwin.application.acluser.role.mappers;

import org.springframework.stereotype.Component;

import bo.quispe.edwin.application.acluser.role.dtos.CreateRoleDto;
import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.domain.acluser.role.Role;

@Component 
public class RoleMapper {

    public Role toDomain(CreateRoleDto request) {
        if ( request == null ) return null;
        return new Role(null, request.getName(), request.getDescription(), null);
        
    }

    public ResponseRoleDto toResponse(Role role) {
        if ( role == null ) return null;
        return new ResponseRoleDto(role.getId(), role.getName(), role.getDescription());
    }
}
