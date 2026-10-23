package bo.quispe.edwin.infrastructure.persistence.acluser.permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import bo.quispe.edwin.domain.acluser.permission.Permission;
import bo.quispe.edwin.domain.acluser.permission.PermissionRepository;
import bo.quispe.edwin.infrastructure.persistence.acluser.mappers.PermissionEntityMapper;

@Repository 
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    //private final PermissionEntityMapper permissionEntityMapper;

    //public PermissionRepositoryImpl(PermissionJpaRepository permissionJpaRepository, PermissionEntityMapper permissionEntityMapper) {
    public PermissionRepositoryImpl(PermissionJpaRepository permissionJpaRepository) {
        this.permissionJpaRepository = permissionJpaRepository;
    }

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = PermissionEntityMapper.toEntity(permission);
        
        PermissionEntity saved = permissionJpaRepository.save(entity);
        return PermissionEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return permissionJpaRepository.findById(id).map(PermissionEntityMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream().map(PermissionEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionJpaRepository.findByName(name).map(PermissionEntityMapper::toDomain);
    }

}
