package bo.quispe.edwin.presentation.acluser.permission;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.quispe.edwin.application.acluser.permission.CreatePermissionUseCase;
import bo.quispe.edwin.application.acluser.permission.FindAllPermissionUseCase;
import bo.quispe.edwin.application.acluser.permission.UpdatePermissionUseCase;
import bo.quispe.edwin.application.acluser.permission.dtos.CreatePermissionDto;
import bo.quispe.edwin.application.acluser.permission.dtos.ResponsePermissionDto;
import jakarta.validation.Valid;

@RestController 
@RequestMapping(value = "/api/permissions")
public class PermissionController {

    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final CreatePermissionUseCase createPermissionUseCase;
    private final FindAllPermissionUseCase findAllPermissionUseCase;

    public PermissionController(CreatePermissionUseCase createPermissionUseCase, FindAllPermissionUseCase findAllPermissionUseCase, UpdatePermissionUseCase updatePermissionUseCase) {
        this.createPermissionUseCase = createPermissionUseCase;
        this.findAllPermissionUseCase = findAllPermissionUseCase;
        this.updatePermissionUseCase = updatePermissionUseCase;
    }

    @PutMapping(name = "/{permissionId}")
    public ResponsePermissionDto updatePermission(@PathVariable UUID permissionId, @Valid @RequestBody CreatePermissionDto request) {
        //Permission permission = updatePermissionUseCase.execute(permissionId, permissionMapper.toDomain(request));
        //return permissionMapper.toResponse(updatePermissionUseCase.execute(permissionId, permissionMapper.toDomain(request)));
        return updatePermissionUseCase.execute(permissionId, request);
    }

    @PostMapping 
    public ResponsePermissionDto createPermission(@Valid @RequestBody CreatePermissionDto request) {
        return createPermissionUseCase.execute(request);
    }

    @GetMapping 
    public List<ResponsePermissionDto> getAllPermission() {
        //return findAllPermissionUseCase.execute().stream().map(p -> permissionMapper.toResponse(p)).toList();
        //return findAllPermissionUseCase.execute().stream().map(permissionMapper::toResponse).toList();
        return findAllPermissionUseCase.execute();
    }
}
