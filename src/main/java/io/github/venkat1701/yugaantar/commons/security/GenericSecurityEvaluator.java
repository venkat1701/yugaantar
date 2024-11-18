package io.github.venkat1701.yugaantar.commons.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("genericSecurityEvaluator")
public class GenericSecurityEvaluator {

    public boolean hasPermission(
            Authentication authentication,
            Object target,
            SecurityPermission permission
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String[] allowedRoles = permission.getAllowedRoles();
        System.out.println("Allowed roles: " + Arrays.toString(allowedRoles));
        System.out.println("User roles: " + authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> Arrays.stream(allowedRoles)
                        .anyMatch(role -> authority.equals(role.replace("ROLE_", "")))));

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> Arrays.stream(allowedRoles)
                        .anyMatch(role -> authority.equals(role.replace("ROLE_", ""))));
    }
}
