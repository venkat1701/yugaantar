package io.github.venkat1701.yugaantar.controllers.users;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;

import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.services.core.user.UserService;
import io.github.venkat1701.yugaantar.utilities.annotations.SponsorSecurity;
import io.github.venkat1701.yugaantar.utilities.annotations.UserSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/users")
@RestController
public class UserControllerImplementation extends GenericCrudController<User, UserDTO, Long, UserSecurity> {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserControllerImplementation(UserService service, UserMapper mapper) {
        super(service, mapper);
        this.userService = service;
        this.userMapper = mapper;
    }

}
