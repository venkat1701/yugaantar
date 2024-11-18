package io.github.venkat1701.yugaantar.commons.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                .reduce(String::concat)
                .get()
                .replace("{authority=ROLE_", "")
                .replace("}", "")
                .equals(allowedRoles[0])
        );

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce(String::concat)
                .get()
                .replace("{authority=ROLE_", "")
                .replace("}", "")
                .equals(allowedRoles[0]);
    }
}
