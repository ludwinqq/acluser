package bo.quispe.edwin.domain.acluser.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.enums.RolePermissionStatus;
import bo.quispe.edwin.domain.enums.UserPermissionStatus;
import bo.quispe.edwin.domain.utils.PasswordEncoder;
import bo.quispe.edwin.domain.utils.PasswordValidator;

public class User {

    private final UUID id;
    private Role role;
    private String username;
    private String email;
    private String password;
    private String refreshTokenHash;
    private Boolean requirePasswordChange;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer failedAttempts = 0; // Inicializado por defecto
    private LocalDateTime lockoutUntil; // Tiempo de desbloqueo automático


    // Composicion: la lista vive dentro del User. Ella es la tabla intermedia en memoria
    private List<UserPermission> customPermissions; // filas fisicas en user_permissions

    public User(UUID id, Role role, String username, String email, String password, Boolean requirePasswordChange, Integer status, Integer failedAttempts, LocalDateTime lockoutUntil, List<UserPermission> customPermissions) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.requirePasswordChange = requirePasswordChange;
        this.customPermissions = customPermissions != null ? new ArrayList<>(customPermissions): new ArrayList<>();
        this.status = status;
        this.failedAttempts = failedAttempts != null ? failedAttempts : 0; // Asignación desde BD
        this.lockoutUntil = lockoutUntil; 
    }

    /**
     * LÓGICA DE NEGOCIO: El administrador da de baja al usuario.
     * Cambia el estado a 0 y destruye el token de refresco para expulsarlo de inmediato de la red.
     */
    public void deactivate() {
        this.status = 0;
        this.refreshTokenHash = null; // Rompe su sesión activa en caliente
        this.failedAttempts = 0;      // Limpia contadores por si acaso
        this.lockoutUntil = null;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    /**
     * LÓGICA DE NEGOCIO: El administrador reactiva o da de alta al usuario.
     * Restaura el estado a 1 y limpia cualquier bloqueo previo.
     */
    public void activate() {
        this.status = 1;
        this.failedAttempts = 0;
        this.lockoutUntil = null;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    /**
     * LÓGICA DE NEGOCIO: Permite al usuario cambiar su contraseña de forma voluntaria.
     * Valida obligatoriamente que la contraseña actual coincida con la registrada en el sistema.
     */
    public void updatePasswordVoluntary(String currentRawPassword, String newRawPassword, PasswordEncoder passwordEncoder) {
        // 1. Validar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(currentRawPassword, this.password)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        // 2. Validar que la nueva contraseña cumpla con las reglas básicas
        /*if (newRawPassword == null || newRawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
        }*/
        PasswordValidator.validate(newRawPassword);

        if (currentRawPassword.equals(newRawPassword) || passwordEncoder.matches(newRawPassword, this.password)) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a tu contraseña actual.");
        }
        
        if (currentRawPassword.equals(newRawPassword)) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
        }

        // 3. Aplicamos el cambio de forma segura y limpiamos las sesiones activas (Refresh Token)
        this.password = passwordEncoder.encode(newRawPassword);
        this.refreshTokenHash = null; // Fuerza un re-login por seguridad en otros dispositivos
        this.updatedAt = LocalDateTime.now();
        this.requirePasswordChange = false;
    }


    /**
     * LÓGICA DE NEGOCIO: Registra el nuevo hash del Refresh Token al iniciar sesión.
     * Si se le pasa 'null', limpia el token (sirve para el Logout).
     */
    //public void updateRefreshToken(String rawRefreshToken, PasswordEncoder passwordEncoder) {
    public void updateRefreshToken(String rawRefreshToken) {
        if (rawRefreshToken == null) {
            this.refreshTokenHash = null; // Logout
        } else {
            //this.refreshTokenHash = passwordEncoder.encode(rawRefreshToken);
            this.refreshTokenHash = rawRefreshToken;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * LÓGICA DE NEGOCIO CORREGIDA: Permite al administrador resetear la clave.
     * Fuerza la contraseña temporal, exige cambio obligatorio, y LIMPIARÁ cualquier bloqueo previo.
     */
    public void resetPasswordRequired(String temporaryPassword, PasswordEncoder passwordEncoder) {
        if (temporaryPassword == null || temporaryPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña temporal no puede estar vacía.");
        }
        this.password = passwordEncoder.encode(temporaryPassword);
        this.requirePasswordChange = true; // Obliga al cambio en su siguiente login
        this.refreshTokenHash = null;      // Cierra sesiones activas
        
        // NUEVO: El administrador rompe el bloqueo por intentos fallidos
        this.status = 1;                   // Regresa a estado Activo
        this.failedAttempts = 0;           // Reinicia el contador a cero
        this.lockoutUntil = null;          // Elimina la fecha de castigo
        
        this.updatedAt = java.time.LocalDateTime.now();
    }
    /*public void resetPasswordRequired(String temporaryPassword, PasswordEncoder passwordEncoder) {
        if (temporaryPassword == null || temporaryPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña temporal no puede estar vacía.");
        }

        this.password = passwordEncoder.encode(temporaryPassword);
        this.requirePasswordChange = true;
        this.refreshTokenHash = null;
        this.updatedAt = LocalDateTime.now();
    }*/

    /**
     * REGLA DE NEGOCIO CORREGIDA: Evalúa el Login aplicando políticas de bloqueo.
     */
    public boolean authenticate(String rawPassword, PasswordEncoder passwordEncoder) {
        // 1. Validar si está en Bloqueo Temporal (status == 2) y si ya expiró el tiempo de castigo (Ej: 15 minutos)
        if (this.status == 2) {
            if (this.lockoutUntil != null && LocalDateTime.now().isAfter(this.lockoutUntil)) {
                // El tiempo ya pasó: Restauramos la cuenta de forma automática en memoria
                this.status = 1;
                this.failedAttempts = 0;
                this.lockoutUntil = null;
            } else {
                throw new SecurityException("Su cuenta está bloqueada temporalmente por exceso de intentos.");
            }
        }

        if (this.status != 1) {
            return false; // Inactivo o dado de baja
        }

        // 2. Evaluar contraseña
        boolean isCorrect = passwordEncoder.matches(rawPassword, this.password);

        if (isCorrect) {
            // ÉXITO: Limpiamos el contador de fallos de inmediato
            this.failedAttempts = 0;
            this.lockoutUntil = null;
        } else {
            // FALLO: Incrementamos el contador
            this.failedAttempts = getFailedAttempts() + 1;
            
            if (this.failedAttempts >= 5) {
                this.status = 2; // Mutamos a Bloqueo Temporal
                this.lockoutUntil = LocalDateTime.now().plusMinutes(30); // Castigo de 15 minutos
                throw new SecurityException("Cuenta bloqueada por 30 minutos debido a 5 intentos fallidos.");
            }
        }
        System.out.println("contador " + failedAttempts);
        return isCorrect;
    } 
  
    /**
     * LÓGICA DE NEGOCIO: Obliga al usuario a cambiar su contraseña
     * y apaga la bandera de cambio obligatorio de forma automática.
     */
    public void forcePasswordChange(String newRawPassword, PasswordEncoder passwordEncoder) {
        // 1. EL CANDADO: Verificamos si la nueva contraseña es idéntica a la que ya tiene en la base de datos
        PasswordValidator.validate(newRawPassword);

        if (passwordEncoder.matches(newRawPassword, this.password)) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a tu contraseña actual.");
        }
        
        this.password = passwordEncoder.encode(newRawPassword);

        // Apagamos la bandera para que no lo vuelva a pedir en el siguiente login
        this.requirePasswordChange = false; 
        this.updatedAt = LocalDateTime.now();
    }

    /*public void changeStatus(Integer newStatus) {
        if ( newStatus == null ) {
            throw new IllegalArgumentException("Debe haber un estado.");
        }
    }
    public void changePassword(String newHashedPassword) {
        this.password = newHashedPassword;
        this.requirePasswordChange = false; // ¡Clave! Al cambiar la contraseña, ya no se debe exigir otro cambio.
    }*/


    /**
     * LÓGICA DE NEGOCIO 1: CONSTRUIR LA LISTA PARA LA PANTALLA
     * Mapea exactamente el comportamiento visual de tu segunda imagen.
     */
    public List<UserPermission> calculateDisplayAcl() {
        // 1. Mapa de las sobreescrituras actuales que tiene el usuario en BD
        Map<UUID, UserPermissionStatus> directOverrides = this.customPermissions.stream()
                    .collect(Collectors.toMap(UserPermission::getPermissionId, UserPermission::getStatus));
        
        // 2. Iteramos ÚNICAMENTE los permisos asignados del Rol (Filtra los IGNORADOS en el Rol de forma automática)
        
        return this.role.getAssignedPermissions().stream().map(rolePermission -> {
            UUID pId = rolePermission.getPermissionId();
            //Calculamos el estado heredado base segun Rol
            UserPermissionStatus finalStatus = (rolePermission.getStatus() == RolePermissionStatus.HABILITADO ) ? UserPermissionStatus.HEREDADO_HABILITADO: UserPermissionStatus.HEREDADO_DENEGADO;
            // Si el usuario guardó una sobreescritura manual en la BD, se antepone a la herencia
            if ( directOverrides.containsKey(pId) ) {
                finalStatus = directOverrides.get(pId);
            }
            return new UserPermission(pId, rolePermission.getPermissionName(), finalStatus);
        }).collect(Collectors.toList());
    }

    /**
     * LÓGICA DE NEGOCIO 2: PROCESAR CAMBIOS MASIVOS DESDE LA PANTALLA
     * Aplica la regla: si vuelve a ser HEREDADO, se borra de la tabla intermedia del usuario.
     */
    public void applyPermissionChanges(List<UserPermissionChange> changes) {
        if (changes == null) return;

        for (UserPermissionChange change : changes) {
            UserPermission existing = this.customPermissions.stream()
                    .filter(cp -> cp.getPermissionId().equals(change.getPermissionId()))
                    .findFirst()
                    .orElse(null);

            if (change.getTargetStatus() == UserPermissionStatus.HEREDADO_HABILITADO || 
                change.getTargetStatus() == UserPermissionStatus.HEREDADO_DENEGADO) {
                
                // REGLA: Si selecciona la opción heredada, se limpia y se remueve de la intermedia en el usuario
                if (existing != null) {
                    this.customPermissions.remove(existing);
                }
            } else {
                // REGLA: Si selecciona HABILITADO o DENEGADO explícito
                if (existing != null) {
                    existing.overrideStatus(change.getTargetStatus());
                } else {
                    UserPermission newOverride = new UserPermission(
                            change.getPermissionId(),
                            null,
                            change.getTargetStatus()
                    );
                    this.customPermissions.add(newOverride);
                }
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public Boolean getRequirePasswordChange() {
        //return requirePasswordChange;
        return requirePasswordChange != null ? requirePasswordChange : true;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<UserPermission> getCustomPermissions() {
        return customPermissions;
    }
    
    public Integer getFailedAttempts() { return failedAttempts != null ? failedAttempts : 0; }
    public LocalDateTime getLockoutUntil() { return lockoutUntil; }

}
