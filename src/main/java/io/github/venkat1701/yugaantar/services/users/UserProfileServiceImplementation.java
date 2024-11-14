package io.github.venkat1701.yugaantar.services.users;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.users.UserProfileMapper;
import io.github.venkat1701.yugaantar.models.users.UserProfile;
import io.github.venkat1701.yugaantar.repositories.users.UserProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImplementation extends GenericPersistenceService<UserProfile, Long> {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImplementation(UserProfileRepository repository, UserProfileMapper userProfileMapper) {
        super(repository, userProfileMapper);
        this.userProfileRepository = repository;
        this.userProfileMapper = userProfileMapper;
    }
}
