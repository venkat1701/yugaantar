package io.github.venkat1701.yugaantar.commons.security;

import io.github.venkat1701.yugaantar.utilities.annotations.DefaultSecurityPermissionResolver;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("genericSecurityEvaluator")
public class GenericSecurityEvaluator implements PermissionEvaluator {

    private final Map<String, Integer> roleWeights = new HashMap<>();
    private final DefaultSecurityPermissionResolver permissionResolver;

    public GenericSecurityEvaluator(DefaultSecurityPermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;

        // Define role weights
        roleWeights.put("ROLE_SUPERADMIN", 4);
        roleWeights.put("ROLE_ADMIN", 3);
        roleWeights.put("ROLE_MANAGER", 2);
        roleWeights.put("ROLE_PARTICIPANT", 1);
        roleWeights.put("ROLE_GUEST", 0);
    }

    public boolean hasPermission(Authentication authentication, SecurityPermission permission) {
        if (!isValidAuthentication(authentication) || permission == null) {
            return false;
        }
        System.out.println("ENUM:" +permission.getAllowedRoles());
        String[] allowedRoles = permission.getAllowedRoles();
        Set<String> userRoles = extractUserRoles(authentication);


        System.out.println("Evaluating permission: " + permission.getPermissionName());
        System.out.println("User roles: " + userRoles);
        System.out.println("Allowed roles: " + Arrays.toString(allowedRoles));

        return checkRoleMatch(userRoles, allowedRoles);
    }

    /**
     * Checks if the user has a permission by resolving its name.
     */
    public boolean hasPermission(Authentication authentication, String permissionName) {
        SecurityPermission resolvedPermission = permissionResolver.resolve(permissionName);
        return hasPermission(authentication, resolvedPermission);
    }

    /**
     * Verifies if the user's role weight meets or exceeds the required role's weight.
     */
    public boolean hasMinimumRole(Authentication authentication, String requiredRole) {
        if (!isValidAuthentication(authentication)) {
            return false;
        }
        String userRole = extractPrimaryRole(authentication);
        return getRoleWeight(userRole) >= getRoleWeight(requiredRole);
    }

    private boolean checkRoleMatch(Set<String> userRoles, String[] allowedRoles) {
        return Arrays.stream(allowedRoles)
                .anyMatch(userRoles::contains);
    }


    private boolean isValidAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    private Set<String> extractUserRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("{authority=ROLE_", ""))
                .map(role->role.replace("}", ""))
                .collect(Collectors.toSet());
    }

    private String extractPrimaryRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_GUEST");
    }

    private int getRoleWeight(String role) {
        return roleWeights.getOrDefault(role, 0);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
    public void setRoleWeight(String role, int weight) {
        roleWeights.put(role, weight);
    }
}
