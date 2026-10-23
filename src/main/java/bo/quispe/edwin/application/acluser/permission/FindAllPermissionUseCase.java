package bo.quispe.edwin.application.acluser.permission;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.permission.dtos.ResponsePermissionDto;
import bo.quispe.edwin.application.acluser.permission.mappers.PermissionMapper;
import bo.quispe.edwin.domain.acluser.permission.Permission;
import bo.quispe.edwin.domain.acluser.permission.PermissionRepository;

@Service 
public class FindAllPermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public FindAllPermissionUseCase(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /*public List<Permission> execute() {
        return permissionRepository.findAll();
    }*/
    @Transactional (readOnly = true)
    public List<ResponsePermissionDto> execute() {
        List<Permission> permission = permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toResponse).toList();
    }
}
