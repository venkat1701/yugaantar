package io.github.venkat1701.yugaantar.services.implementation.users;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import io.github.venkat1701.yugaantar.services.core.user.UserService;
import io.github.venkat1701.yugaantar.utilities.annotations.UserSecurity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation extends UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImplementation(UserRepository userRepository, UserMapper userMapper, GenericSecurityEvaluator evaluator) {
        super(userRepository, userMapper, evaluator);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return userRepository.findByEmail(userDTO.getEmail()).map(existingUser -> {
            User updatedUser = userMapper.fromDto(userDTO);
            updatedUser.setId(existingUser.getId());
            User savedUser = userRepository.save(updatedUser);
            return userMapper.toDto(savedUser);
        });
    }
}
