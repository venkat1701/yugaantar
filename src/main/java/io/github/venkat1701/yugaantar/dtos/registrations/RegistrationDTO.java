package io.github.venkat1701.yugaantar.dtos.registrations;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDTO {
    private String email;
    private long eventID;
    private String registrationDate;
    private String registrationStatus;
    private int amountPaid;
}
