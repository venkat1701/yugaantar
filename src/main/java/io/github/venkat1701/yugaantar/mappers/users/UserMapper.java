package io.github.venkat1701.yugaantar.mappers.users;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.models.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    public UserMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserDTO> getDtoClass() {
        return UserDTO.class;
    }
}