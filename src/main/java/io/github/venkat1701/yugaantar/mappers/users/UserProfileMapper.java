package io.github.venkat1701.yugaantar.mappers.users;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.users.UserProfileDTO;
import io.github.venkat1701.yugaantar.models.users.UserProfile;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper extends GenericMapper<UserProfile, UserProfileDTO> {
    public UserProfileMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<UserProfile> getEntityClass() {
        return null;
    }

    @Override
    protected Class<UserProfileDTO> getDtoClass() {
        return null;
    }
}
