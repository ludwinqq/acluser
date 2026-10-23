package bo.quispe.edwin.domain.acluser.role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bo.quispe.edwin.domain.enums.RolePermissionStatus;

public class Role {

    private final UUID id;
    private String name;
    private String description;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Composicion: la lista vive dentro del Rol. Ella es la tabla intermedia en memoria
    private List<RolePermission> assignedPermissions;
    
    public Role(UUID id, String name, String description, List<RolePermission> assignedPermissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assignedPermissions = assignedPermissions != null ? new ArrayList<>(assignedPermissions): new ArrayList<>();
        this.status = 1;
    }

    public void changeName(String name) {
        if ( name != null || !name.isEmpty() ) {
            this.name = name;
        }
    }

    public void changeDescription(String description) {
        if ( description != null || !description.isEmpty() ) {
            this.description = description;
        }
    }
    
    // MOTOR DE DECISIONES: PROCESAR CAMBIOS MASIVOS
    public void applyPermissionChanges(List<RolePermissionChange> changes) {
        if ( changes == null ) return;
        for ( RolePermissionChange change: changes) {
            // 1. Buscamos si el permiso ya estaba registrado fisicamente en el Rol
            RolePermission existing = this.assignedPermissions.stream()
                    .filter(p -> p.getPermissionId().equals(change.getPermissionId()))
                    .findFirst()
                    .orElse(null);
            // 2. Evaluamos la regla exacta que describimos en la pantalla
            if (change.getTargetStatus() == RolePermissionStatus.IGNORADO) {
                // 1. Si el usuario eligio IGNORADO: se remueve fisicamente de nuestra coleccion
                if ( existing != null ) {
                    this.assignedPermissions.remove(existing);
                }
            } else {
                if ( existing != null ) {
                    // Si ya existía en la lista, solo mutamos su estado interno
                    existing.updateStatus(change.getTargetStatus());
                } else {
                    // Si no existía, creamos la nueva relación y la metemos a la lista del Rol
                    RolePermission newRelation = new RolePermission(
                        change.getPermissionId(),
                        null,
                        change.getTargetStatus()
                    );
                    this.assignedPermissions.add(newRelation);
                }
            }
        }
    }
    
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<RolePermission> getAssignedPermissions() { return new ArrayList<>(assignedPermissions); }

}
