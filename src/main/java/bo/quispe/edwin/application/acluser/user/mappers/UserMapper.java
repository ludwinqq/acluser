package bo.quispe.edwin.application.acluser.user.mappers;

import org.springframework.stereotype.Component;

import bo.quispe.edwin.application.acluser.user.dtos.CreateUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.ResponseUserDto;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserPermission;

@Component 
public class UserMapper {

    public User toDomain(CreateUserDto request, Role role) {
        if ( request == null ) return null;

        // Si el administrador lo creó, normalmente se le exige cambiar contraseña en el primer login
        Boolean requireChange = request.getRequirePasswordChange() != null ? request.getRequirePasswordChange() : true;

        /*UUID id, 
        Role role, 
        String username, 
        String email, 
        String password, 
        Boolean requirePasswordChange, 
        Integer status, 
        List<UserPermission> customPermissions*/
        return new User(
            null, 
            role, 
            request.getUsername(), 
            request.getEmail(), 
            request.getPassword(), 
            requireChange,
            request.getStatus(),
            0,
            null,
            null
        );
        //return new User(null, role, request.getUsername(), request.getEmail(), request.getPassword(), request.getRefreshTokenHash(), null);
    }

    public ResponseUserDto toResponse(User user) {
        if ( user == null ) return null;

        return new ResponseUserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRefreshTokenHash(), user.getRole().getId());
    }
}
