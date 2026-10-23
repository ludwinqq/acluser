package bo.quispe.edwin.application.acluser.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.role.dtos.CreateRoleDto;
import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.application.acluser.role.mappers.RoleMapper;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;

@Service 
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public CreateRoleUseCase(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public ResponseRoleDto execute(CreateRoleDto dto) {
        Role role = roleMapper.toDomain(dto);
        return roleMapper.toResponse(roleRepository.save(role));
    }
}
