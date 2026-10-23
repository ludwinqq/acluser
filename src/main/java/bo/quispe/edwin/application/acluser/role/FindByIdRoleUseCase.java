package bo.quispe.edwin.application.acluser.role;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.application.acluser.role.mappers.RoleMapper;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;

@Service 
public class FindByIdRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public FindByIdRoleUseCase(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional (readOnly = true)
    public ResponseRoleDto execute(UUID roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role no encontrado"));
        return roleMapper.toResponse(role);
    }
}
