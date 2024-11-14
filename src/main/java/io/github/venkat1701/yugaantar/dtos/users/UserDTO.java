package io.github.venkat1701.yugaantar.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String address;
    private String bio;
    private String profilePictureURI;

}
