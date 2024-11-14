package io.github.venkat1701.yugaantar.controllers.auth;


import io.github.venkat1701.yugaantar.dtos.auth.AuthRequestDTO;
import io.github.venkat1701.yugaantar.dtos.auth.AuthResponseDTO;
import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.services.auth.UserAuthServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    private final UserAuthServiceImplementation userAuthServiceImplementation;
    private final UserMapper userMapper;
    public AuthenticationController(UserAuthServiceImplementation userAuthServiceImplementation, UserMapper userMapper) {
        this.userAuthServiceImplementation = userAuthServiceImplementation;
        this.userMapper = userMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody UserDTO userDTO) {
        AuthResponseDTO response = this.userAuthServiceImplementation.signup(userDTO);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signin(@RequestBody AuthRequestDTO userDTO) {
        return ResponseEntity.ok(this.userAuthServiceImplementation.login(userDTO));
    }
}
