package bo.quispe.edwin.infrastructure.persistence.acluser.role;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID>{
    Optional<RoleEntity> findByName(String name);
    //Optional<RoleEntity> findByIdWithPermissions(UUID id);
}
