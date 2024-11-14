package io.github.venkat1701.yugaantar.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for UserProfile.
 * <p>
 * This class is used to transfer user profile data between different layers of the application.
 * </p>
 *
 * Author: Venkat
 */
@Getter
@Setter
@AllArgsConstructor
@NotBlank
public class UserProfileDTO {

    /**
     * Unique identifier for the user profile.
     */
    private Long id;

    /**
     * The user's first name.
     * <p>
     * This field is required and must not be blank.
     * </p>
     */
    @NotBlank
    private String firstName;

    /**
     * The user's last name.
     * <p>
     * This field is required and must not be blank.
     * </p>
     */
    @NotBlank
    private String lastName;

    /**
     * The user's phone number.
     * <p>
     * This field must match the specified regex pattern for international phone numbers.
     * </p>
     */
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phoneNumber;

    /**
     * The user's address.
     * <p>
     * This field can store a large amount of text, allowing for detailed address descriptions.
     * </p>
     */
    private String address;

    /**
     * The user's date of birth.
     * <p>
     * This field must be a past date, ensuring that the user is of legal age.
     * </p>
     */
    @Past
    private LocalDateTime dateOfBirth;

    /**
     * URI for the user's profile picture.
     */
    private String profilePictureUri;
}