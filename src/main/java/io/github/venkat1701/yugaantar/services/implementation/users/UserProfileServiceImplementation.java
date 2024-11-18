package io.github.venkat1701.yugaantar.services.implementation.users;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.users.UserProfileMapper;
import io.github.venkat1701.yugaantar.models.users.UserProfile;
import io.github.venkat1701.yugaantar.repositories.users.UserProfileRepository;
import io.github.venkat1701.yugaantar.utilities.annotations.UserSecurity;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImplementation extends GenericPersistenceService<UserProfile, Long, UserSecurity> {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImplementation(UserProfileRepository repository, UserProfileMapper userProfileMapper, GenericSecurityEvaluator evaluator) {
        super(repository, userProfileMapper, UserSecurity.class,evaluator);
        this.userProfileRepository = repository;
        this.userProfileMapper = userProfileMapper;
    }
}
