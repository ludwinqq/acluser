package bo.quispe.edwin.application.acluser.user;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.user.dtos.CreateUserDto;
import bo.quispe.edwin.application.acluser.user.dtos.ResponseUserDto;
import bo.quispe.edwin.application.acluser.user.mappers.UserMapper;
import bo.quispe.edwin.domain.acluser.role.Role;
import bo.quispe.edwin.domain.acluser.role.RoleRepository;
import bo.quispe.edwin.domain.acluser.user.User;
import bo.quispe.edwin.domain.acluser.user.UserRepository;
import bo.quispe.edwin.domain.utils.PasswordEncoder;
import bo.quispe.edwin.domain.utils.PasswordValidator;

@Service 
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseUserDto execute(CreateUserDto dto) {
        // =========================================================================
        // LOS ESCUDOS DE UNICIDAD (FALLAR RÁPIDO)
        // =========================================================================
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya se encuentra registrado.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico '" + dto.getEmail() + "' ya está asociado a otra cuenta.");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("No se puede crear el usuario: El Rol especificado no existe."));

        PasswordValidator.validate(dto.getPassword());
        String securePassword = passwordEncoder.encode(dto.getPassword());
        User savedUser = new User(
                null,
                role,
                dto.getUsername(),
                dto.getEmail(),
                securePassword,
                true,
                1,//dto.getStatus(),
                0,
                null,
                new ArrayList<>()
        );

        return userMapper.toResponse(userRepository.save(savedUser));
    }
}
