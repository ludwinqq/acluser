package bo.quispe.edwin.application.acluser.permission;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.permission.dtos.CreatePermissionDto;
import bo.quispe.edwin.application.acluser.permission.dtos.ResponsePermissionDto;
import bo.quispe.edwin.application.acluser.permission.mappers.PermissionMapper;
import bo.quispe.edwin.domain.acluser.permission.Permission;
import bo.quispe.edwin.domain.acluser.permission.PermissionRepository;

@Service 
public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public UpdatePermissionUseCase(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Transactional 
    public ResponsePermissionDto execute(UUID permissionId, CreatePermissionDto dto) {
        Permission existingPermission = permissionRepository.findById(permissionId).orElseThrow(() -> new IllegalArgumentException("No existe permiso"));
        existingPermission.changeName(dto.getName());
        existingPermission.changeDescription(dto.getDescription());
        existingPermission.changeModule(dto.getModule());
        existingPermission.changeTitle(dto.getTitle());
        Permission updated = permissionRepository.save(existingPermission);
        return permissionMapper.toResponse(updated);
    }

    /*public Permission execute(UUID permissionId, Permission permission) {
        Permission existingPermission = permissionRepository.findById(permissionId).orElseThrow(() -> new IllegalArgumentException("No existe permiso"));
        existingPermission.changeName(permission.getName());
        existingPermission.changeDescription(permission.getDescription());
        existingPermission.changeTitle(permission.getTitle());
        existingPermission.changeModule(permission.getModule());
        return permissionRepository.save(existingPermission);
    }*/
}
