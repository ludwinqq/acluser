package bo.quispe.edwin.application.acluser.permission.mappers;

import org.springframework.stereotype.Component;

import bo.quispe.edwin.application.acluser.permission.dtos.CreatePermissionDto;
import bo.quispe.edwin.application.acluser.permission.dtos.ResponsePermissionDto;
import bo.quispe.edwin.domain.acluser.permission.Permission;

@Component 
public class PermissionMapper {

    public Permission toDomain(CreatePermissionDto request) {
        if ( request == null ) return null;
        return  new Permission(null, request.getName(), request.getDescription(), request.getTitle(),request.getModule());
    }

    public ResponsePermissionDto toResponse(Permission permission) {
        if ( permission == null ) return null;
        return new ResponsePermissionDto(permission.getId(), permission.getName(), permission.getTitle(), permission.getDescription(), permission.getModule());
    }
}
