package bo.quispe.edwin.application.acluser.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.quispe.edwin.application.acluser.user.dtos.ResponseUserDto;
import bo.quispe.edwin.application.acluser.user.mappers.UserMapper;
import bo.quispe.edwin.domain.acluser.user.UserRepository;

@Service 
public class FindByIdUserUseCase {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public FindByIdUserUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional (readOnly = true)
    public List<ResponseUserDto> execute() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }
}
