package bo.quispe.edwin.application.acluser.role;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.role.dtos.CreateRoleDto;
import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.application.acluser.role.mappers.RoleMapper;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;

@Service 
public class UpdateRoleUseCase {

    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    public UpdateRoleUseCase(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional 
    public ResponseRoleDto execute(UUID roleId, CreateRoleDto dto) {
        Role existingRole = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("No existe rol"));
        existingRole.changeName(dto.getName());
        existingRole.changeDescription(dto.getDescription());
        return roleMapper.toResponse(roleRepository.save(existingRole));
    }
}
