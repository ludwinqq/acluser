package bo.quispe.edwin.infrastructure.persistence.acluser.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;
import bo.quispe.edwin.infrastructure.persistence.acluser.mappers.RoleEntityMapper;

@Repository 
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Role save(Role role) {
        // 1. Traducimos el Agregado del Dominio con sus colecciones mutadas a Entidad JPA
        RoleEntity entity = RoleEntityMapper.toEntity(role);
        // 2. Persistimos. Hibernate analiza la lista física y ejecuta inserts, updates o deletes (huérfanos) automáticamente
        RoleEntity saved = roleJpaRepository.save(entity);
        return RoleEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id).map(RoleEntityMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream().map(RoleEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(RoleEntityMapper::toDomain);
    }

    @Override
    public Optional<Role> findByIdWithPermissions(UUID id) {
        // En JPA tradicional, un findById estándar carga las colecciones perezosas (LAZY) 
        // cuando el mapeador accede a ellas dentro de la misma transacción.
        return roleJpaRepository.findById(id).map(RoleEntityMapper::toDomain);
    }
    
}
