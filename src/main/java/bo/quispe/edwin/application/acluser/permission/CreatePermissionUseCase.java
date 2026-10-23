package bo.quispe.edwin.application.acluser.permission;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.permission.dtos.CreatePermissionDto;
import bo.quispe.edwin.application.acluser.permission.dtos.ResponsePermissionDto;
import bo.quispe.edwin.application.acluser.permission.mappers.PermissionMapper;
import bo.quispe.edwin.domain.acluser.permission.Permission;
import bo.quispe.edwin.domain.acluser.permission.PermissionRepository;

@Service 
public class CreatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public CreatePermissionUseCase(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /*@Transactional
    public Permission execute(Permission permission) {
        return permissionRepository.save(permission);
    }*/
    @Transactional
    public ResponsePermissionDto execute(CreatePermissionDto dto) {
        Permission permission = permissionMapper.toDomain(dto);
        Permission saved = permissionRepository.save(permission);
        return permissionMapper.toResponse(saved);
    }
    
}
