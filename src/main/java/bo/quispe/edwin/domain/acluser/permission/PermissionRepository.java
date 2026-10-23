package bo.quispe.edwin.domain.acluser.permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {

    Permission save(Permission permission);
    Optional<Permission> findById(UUID id);
    List<Permission> findAll();
    Optional<Permission> findByName(String name);
}
