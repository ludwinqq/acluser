package bo.quispe.edwin.presentation.acluser.user;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.quispe.edwin.application.acluser.user.CreateUserUseCase;
import bo.quispe.edwin.application.acluser.user.FindByIdUserUseCase;
import bo.quispe.edwin.application.acluser.user.GetUserPermissionsUseCase;
import bo.quispe.edwin.application.acluser.user.UpdateUserPermissionsUseCase;
import bo.quispe.edwin.application.acluser.user.dtos.CreateUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.ResponseUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionUpdateDto;
import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionViewDto;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    private final FindByIdUserUseCase findByIdUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final GetUserPermissionsUseCase getUserPermissionsUseCase;
    private final UpdateUserPermissionsUseCase updateUserPermissionsUseCase;

    public UserController(GetUserPermissionsUseCase getUserPermissionsUseCase,
            UpdateUserPermissionsUseCase updateUserPermissionsUseCase, CreateUserUseCase createUserUseCase, FindByIdUserUseCase findByIdUserUseCase) {
        this.getUserPermissionsUseCase = getUserPermissionsUseCase;
        this.updateUserPermissionsUseCase = updateUserPermissionsUseCase;
        this.createUserUseCase = createUserUseCase;
        this.findByIdUserUseCase = findByIdUserUseCase;
    }

    @GetMapping 
    public List<ResponseUserDto> getAllUsers() {
        return findByIdUserUseCase.execute();
    }

    @PostMapping 
    public ResponseUserDto createUser(@Valid @RequestBody CreateUserDto request) {
        return createUserUseCase.execute(request);
    }

    /**
     * Endpoint para la pantalla de "Gestionar Permisos Especiales del Usuario"
     * Devuelve la lista filtrando los ignorados en el Rol y calculando las herencias en tiempo real
     */
    @GetMapping("/{userId}/permissions")
    public List<UserPermissionViewDto> getUserPermissions(@PathVariable UUID userId) {
        List<UserPermissionViewDto> permissions = getUserPermissionsUseCase.execute(userId);
        return permissions;
    }

    /**
     * Endpoint para guardar las sobreescrituras manuales del usuario en bloque
     * Si pasa a HEREDADO de vuelta, el sistema desvincula y limpia físicamente la tabla intermedia
     */
    @PutMapping("/{userId}/permissions")
    public void updateUserPermissions(
            @PathVariable UUID userId, 
            @RequestBody List<UserPermissionUpdateDto> updates) {
        
        updateUserPermissionsUseCase.execute(userId, updates);
        //return ResponseEntity.noContent().build();
    }
}
