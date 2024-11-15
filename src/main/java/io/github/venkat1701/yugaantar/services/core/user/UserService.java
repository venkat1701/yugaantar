package io.github.venkat1701.yugaantar.services.core.user;

import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;

public abstract class UserService  extends GenericPersistenceService<User, Long> {
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
    }
}
