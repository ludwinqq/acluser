package bo.quispe.edwin.domain.acluser.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByIdWithFullAclContext(UUID userId);
    User save(User user);
    Optional<User> findByUsernameWithFullAclContext(String username);
    Optional<Integer> findStatusByUsername(String username);
    //void invalidateAllUserSessionsByRoleId(UUID roleId);
}
