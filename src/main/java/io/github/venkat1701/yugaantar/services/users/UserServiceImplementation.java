package io.github.venkat1701.yugaantar.services.users;

import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserServiceImplementation extends GenericPersistenceService<User, Long> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImplementation(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
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


    public boolean deleteUser(Long id) {
        return super.delete(id);
    }

    public List<User> getAllUsers() {
        return super.getAll();
    }

    public Optional<User> getUserById(Long id) {
        return super.getById(id);
    }
}
