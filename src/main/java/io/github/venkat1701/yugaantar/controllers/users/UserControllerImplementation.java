package io.github.venkat1701.yugaantar.controllers.users;

import io.github.venkat1701.yugaantar.commons.controllers.GenericController;

import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.services.users.UserServiceImplementation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/users")
@RestController
public class UserControllerImplementation extends GenericController<User, UserDTO, Long> {

    public UserControllerImplementation(UserServiceImplementation serviceImplementation, UserMapper mapper) {
        super(serviceImplementation, mapper);
    }
}
