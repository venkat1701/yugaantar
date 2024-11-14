package io.github.venkat1701.yugaantar.models.users;


import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.registrations.Registration;
import io.github.venkat1701.yugaantar.models.roles.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name="users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "gender")
    private String gender;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Registration> registrations = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Payment> payments = new HashSet<>();

}
