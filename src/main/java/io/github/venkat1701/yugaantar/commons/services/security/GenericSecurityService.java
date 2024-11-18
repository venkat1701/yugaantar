package io.github.venkat1701.yugaantar.commons.services.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

@Service
public class GenericSecurityService {

    private final Map<String, Integer> roleWeights = new HashMap<>();

    public GenericSecurityService() {
        roleWeights.put("ROLE_SUPERADMIN", 4);
        roleWeights.put("ROLE_ADMIN", 3);
        roleWeights.put("ROLE_MANAGER", 2);
        roleWeights.put("ROLE_PARTICIPANT", 1);
        roleWeights.put("ROLE_GUEST", 0);
    }

    public boolean hasMinimumRole(Authentication authentication, String requiredRole) {
        if(!isValidAuthentication(authentication)) return false;
        String userRole = extractUserRole(authentication);
        return getRoleWeight(userRole) >= getRoleWeight(requiredRole);
    }

    public boolean hasHigherOrEqualRole(Authentication authentication, String targetRole) {
        if(!isValidAuthentication(authentication)) return false;

        String userRoel = extractUserRole(authentication);
        return getRoleWeight(targetRole) >= getRoleWeight(targetRole);
    }

    public boolean isResourceOwner(Authentication authentication, Long resourceId, BiPredicate<String, Long> ownershipValidator) {
        if(!isValidAuthentication(authentication)) return false;

        String username = extractUsername(authentication);
        return ownershipValidator.test(username, resourceId);
    }

    public boolean canPerformAction(Authentication authentication, Long resourceId, String requiredRole, BiPredicate<String, Long> ownershipValidator, boolean allowOwner) {
        if(!isValidAuthentication(authentication)) return false;

        boolean hasRole = hasMinimumRole(authentication, requiredRole);
        if(hasRole) return true;

        return allowOwner && isResourceOwner(authentication, resourceId, ownershipValidator);

    }

    public void setRoleWeight(String role, int weight) {
        roleWeights.put(role, weight);
    }

    private boolean isValidAuthentication(Authentication authentication) {
        return authentication!=null && authentication.isAuthenticated();
    }

    private String extractUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return principal.toString();
    }

    private String extractUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_GUEST");
    }

    private int getRoleWeight(String role) {
        return roleWeights.getOrDefault(role, 0);
    }
}
