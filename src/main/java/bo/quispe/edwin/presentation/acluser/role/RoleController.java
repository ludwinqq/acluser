package bo.quispe.edwin.presentation.acluser.role;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.quispe.edwin.application.acluser.role.CreateRoleUseCase;
import bo.quispe.edwin.application.acluser.role.GetAllRoleUseCase;
import bo.quispe.edwin.application.acluser.role.GetRolePermissionsUseCase;
import bo.quispe.edwin.application.acluser.role.UpdateRolePermissionsUseCase;
import bo.quispe.edwin.application.acluser.role.UpdateRoleUseCase;
import bo.quispe.edwin.application.acluser.role.dtos.CreateRoleDto;
import bo.quispe.edwin.application.acluser.role.dtos.ResponseRoleDto;
import bo.quispe.edwin.application.acluser.role.dtos.RolePermissionUpdateDto;
import bo.quispe.edwin.application.acluser.role.dtos.RolePermissionViewDto;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/roles")
public class RoleController {
    
    private final UpdateRoleUseCase updateRoleUseCase;
    private final GetRolePermissionsUseCase getRolePermissionsUseCase;
    private final UpdateRolePermissionsUseCase updateRolePermissionsUseCase;
    private final CreateRoleUseCase createRoleUseCase;
    private final GetAllRoleUseCase getAllRoleUseCase;
    
    public RoleController(GetRolePermissionsUseCase getRolePermissionsUseCase,
            UpdateRolePermissionsUseCase updateRolePermissionsUseCase, CreateRoleUseCase createRoleUseCase, GetAllRoleUseCase getAllRoleUseCase, UpdateRoleUseCase updateRoleUseCase) {
        this.getRolePermissionsUseCase = getRolePermissionsUseCase;
        this.updateRolePermissionsUseCase = updateRolePermissionsUseCase;
        this.createRoleUseCase = createRoleUseCase;
        this.getAllRoleUseCase = getAllRoleUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
    }

    @PutMapping ("/{roleId}")
    public ResponseRoleDto updatePermission(@PathVariable UUID roleId, @RequestBody CreateRoleDto request) {
        return updateRoleUseCase.execute(roleId, request);
    }

    @GetMapping 
    public List<ResponseRoleDto> getAll() {
        //return getAllRoleUseCase.execute().stream().map(roleMapper::toResponse).toList();
        return getAllRoleUseCase.execute();
    }

    @PostMapping 
    public ResponseRoleDto create(@Valid @RequestBody CreateRoleDto request) {
        //Role role = roleMapper.toDomain(request);
        return createRoleUseCase.execute(request);
    }

    @GetMapping("/{roleId}/permissions")
    public List<RolePermissionViewDto> getRolePermissions(@PathVariable UUID roleId) {
        List<RolePermissionViewDto> permissions = getRolePermissionsUseCase.execute(roleId);
        return permissions;
    }

    @PutMapping("/{roleId}/permissions")
    public void updateRolePermissions(
            @PathVariable UUID roleId, 
            @RequestBody List<RolePermissionUpdateDto> updates) {
        
        updateRolePermissionsUseCase.execute(roleId, updates);
        //return noContent().build(); // Retorna un 204 No Content indicando éxito rotundo
    }
}
