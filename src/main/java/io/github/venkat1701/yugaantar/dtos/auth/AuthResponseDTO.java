package io.github.venkat1701.yugaantar.dtos.auth;

import io.github.venkat1701.yugaantar.models.roles.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {
    private String jwt;
    private String message;

    public AuthResponseDTO(String jwt, String userRegistered) {
        this.jwt = jwt;
        this.message = userRegistered;
    }
}
