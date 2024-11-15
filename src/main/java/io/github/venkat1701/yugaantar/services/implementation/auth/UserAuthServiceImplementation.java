package io.github.venkat1701.yugaantar.services.implementation.auth;

import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.dtos.auth.AuthRequestDTO;
import io.github.venkat1701.yugaantar.dtos.auth.AuthResponseDTO;
import io.github.venkat1701.yugaantar.dtos.users.UserDTO;
import io.github.venkat1701.yugaantar.mappers.users.UserMapper;
import io.github.venkat1701.yugaantar.models.roles.Role;
import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.models.users.UserProfile;
import io.github.venkat1701.yugaantar.repositories.roles.RoleRepository;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import io.github.venkat1701.yugaantar.security.jwt.JwtProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthServiceImplementation extends GenericPersistenceService<User, Long> {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserAuthServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        Role role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().name());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(authority));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User registerUser(UserDTO userDTO) {
        Role role = roleRepository.findByName(RoleEnum.GUEST)
                .orElseThrow(() -> new RuntimeException("GUEST role not found"));

        User user = userMapper.fromDto(userDTO);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(userDTO.getFirstName());
        userProfile.setLastName(userDTO.getLastName());
        userProfile.setAddress(userDTO.getAddress());
        userProfile.setBio(userDTO.getBio());
        userProfile.setPhoneNumber(userDTO.getPhone());
        userProfile.setProfilePictureURI(userDTO.getProfilePictureURI());
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(LocalDateTime.now());

        user.setUserProfile(userProfile);
        return userRepository.save(user);
    }

    public AuthResponseDTO signup(UserDTO userDTO) {
        User createdUser = this.registerUser(userDTO);
        String jwt = jwtProvider.generateToken(new UsernamePasswordAuthenticationToken(createdUser.getEmail(), null, new ArrayList<>()), createdUser.getId());
        return new AuthResponseDTO(jwt, "User Registered");
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        Authentication authentication = this.authenticate(authRequestDTO.getEmail(), authRequestDTO.getPassword());
        User user = userRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        String jwt = jwtProvider.generateToken(authentication, user.getId());
        return new AuthResponseDTO(jwt, "User Login");
    }



    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}