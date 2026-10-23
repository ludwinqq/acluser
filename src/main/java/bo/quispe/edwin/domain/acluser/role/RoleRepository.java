package bo.quispe.edwin.domain.acluser.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Role save(Role role);
    Optional<Role> findById(UUID id);
    List<Role> findAll();
    Optional<Role> findByName(String name);
    Optional<Role> findByIdWithPermissions(UUID id);
}
