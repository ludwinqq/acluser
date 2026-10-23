package bo.quispe.edwin.infrastructure.persistence.acluser.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.infrastructure.persistence.acluser.mappers.UserEntityMapper;

@Repository 
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByIdWithFullAclContext(UUID userId) {
       // Recupera al usuario cargando su rol y sus sobreescrituras explícitas
       return userJpaRepository.findById(userId).map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return UserEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByUsernameWithFullAclContext(String username) {
        // 1. Spring Data JPA busca el registro básico por username
        // 2. Al pasarlo por UserEntityMapper.toDomain, el mapeador toca las listas del Rol y del Usuario
        // 3. Hibernate ejecuta las consultas perezosas automáticas y recolecta todo el árbol ACL
        return userJpaRepository.findByUsername(username)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public java.util.Optional<Integer> findStatusByUsername(String username) {
        // Retorna únicamente el número de estado (0 o 1) directamente de la entidad JPA, 
        // sin pasar por el UserEntityMapper que es el que causa el error de Lazy Loading.
        return userJpaRepository.findByUsername(username)
                .map(UserEntity::getStatus);
    }

    /*@Override
    public void invalidateAllUserSessionsByRoleId(UUID roleId) {
        // Ponemos en NULL el hash del refresh token para TODOS los usuarios vinculados a ese Rol de un solo golpe SQL.
    // Al no haber coincidencia de hash en su siguiente actividad, el filtro global los expulsará.
        entityManager.createQuery("UPDATE UserEntity u SET u.refreshTokenHash = null WHERE u.role.id = :roleId")
            .setParameter("roleId", roleId)
            .executeUpdate();
    }*/
    
}
