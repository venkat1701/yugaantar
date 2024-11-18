package io.github.venkat1701.yugaantar.services.core.user;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import io.github.venkat1701.yugaantar.utilities.annotations.UserSecurity;

public abstract class UserService  extends GenericPersistenceService<User, Long, UserSecurity> {
    public UserService(UserRepository userRepository, UserMapper userMapper, GenericSecurityEvaluator evaluator) {
        super(userRepository, userMapper, UserSecurity.class, evaluator);
    }
}
