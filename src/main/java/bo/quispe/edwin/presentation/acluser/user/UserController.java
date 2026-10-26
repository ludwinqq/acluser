package bo.quispe.edwin.presentation.acluser.user;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.quispe.edwin.application.acluser.auth.LogoutUseCase;
import bo.quispe.edwin.application.acluser.user.ActivateUserUseCase;
import bo.quispe.edwin.application.acluser.user.CreateUserUseCase;
import bo.quispe.edwin.application.acluser.user.DeactivateUserUseCase;
import bo.quispe.edwin.application.acluser.user.FindByIdUserUseCase;
import bo.quispe.edwin.application.acluser.user.GetUserPermissionsUseCase;
import bo.quispe.edwin.application.acluser.user.UpdateUserPermissionsUseCase;
import bo.quispe.edwin.application.acluser.user.dtos.CreateUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.ResponseUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionUpdateDto;
import bo.quispe.edwin.application.acluser.user.dtos.UserPermissionViewDto;
import bo.quispe.edwin.domain.shared.constants.PermissionsMatrix;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    private final FindByIdUserUseCase findByIdUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final GetUserPermissionsUseCase getUserPermissionsUseCase;
    private final UpdateUserPermissionsUseCase updateUserPermissionsUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    private final LogoutUseCase logoutUseCase;

    public UserController(
        GetUserPermissionsUseCase getUserPermissionsUseCase,
        UpdateUserPermissionsUseCase updateUserPermissionsUseCase, 
        CreateUserUseCase createUserUseCase, 
        FindByIdUserUseCase findByIdUserUseCase, 
        ActivateUserUseCase activateUserUseCase, 
        DeactivateUserUseCase deactivateUserUseCase,
        LogoutUseCase logoutUseCase) {
        this.getUserPermissionsUseCase = getUserPermissionsUseCase;
        this.updateUserPermissionsUseCase = updateUserPermissionsUseCase;
        this.createUserUseCase = createUserUseCase;
        this.findByIdUserUseCase = findByIdUserUseCase;
        this.activateUserUseCase = activateUserUseCase;
        this.deactivateUserUseCase = deactivateUserUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        // 1. Si la cookie existe, ejecutamos la invalidación en la BD
        if (refreshToken != null) {
            logoutUseCase.execute(refreshToken);
        }
        // 2. LIMPIEZA TOTAL: Enviamos las cookies vacías y expiradas al cliente para borrarlas del navegador/Bruno
        Cookie clearAccess = new Cookie("accessToken", null);
        clearAccess.setPath("/");
        clearAccess.setMaxAge(0);

        Cookie clearRefresh = new Cookie("refreshToken", null);
        clearRefresh.setPath("/");
        clearRefresh.setMaxAge(0);

        response.addCookie(clearAccess);
        response.addCookie(clearRefresh);
    }
    
    @PutMapping ("/{userId}/activate")
    @PreAuthorize("hasAuthority(T(PermissionsMatrix).USERS.ACTIVAR.NAME)")
    public void activateUser(@PathVariable UUID userId) {
        activateUserUseCase.execute(userId);
    }
    
    @PutMapping ("/{userId}/deactivate")
    @PreAuthorize("hasAuthority(T(PermissionsMatrix).USERS.DESACTIVAR.NAME)")
        public void deactivateUser(@PathVariable UUID userId) {
        deactivateUserUseCase.execute(userId);
    }

    @GetMapping 
    //@PreAuthorize("hasAuthority('user.listar')")
    //@PreAuthorize("@auth.hasPermission('user.listar')")
    @PreAuthorize("@auth.hasPermission('" + PermissionsMatrix.USERS.LISTAR.NAME + "')")
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
