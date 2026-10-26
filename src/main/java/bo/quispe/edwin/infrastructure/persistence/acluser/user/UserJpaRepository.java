package bo.quispe.edwin.infrastructure.persistence.acluser.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    /*@Query("SELECT u FROM UserEntity u " +
           "LEFT JOIN FETCH u.role r " +
           "LEFT JOIN FETCH r.assignedPermissions " +
           "LEFT JOIN FETCH u.customPermissions " +
           "WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithFullAclContext(@Param("username") String username);*/

}
