package bo.quispe.edwin.application.acluser.role;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.application.acluser.role.mappers.RoleMapper;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;

@Service 
public class GetAllRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public GetAllRoleUseCase(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional (readOnly = true)
    public List<ResponseRoleDto> execute() {
        return roleRepository.findAll().stream().map(roleMapper::toResponse).toList();
    }
}
