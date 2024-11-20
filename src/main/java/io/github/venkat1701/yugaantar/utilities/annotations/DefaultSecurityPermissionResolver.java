package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component("defaultSecurityPermissionResolver")
public class DefaultSecurityPermissionResolver {

    private final Map<String, String[]> permissionRoleMap;

    public DefaultSecurityPermissionResolver() {
        this.permissionRoleMap = new HashMap<String, String[]>();
        Arrays.stream(EventSecurity.values())
                .forEach(security -> permissionRoleMap.put(security.getPermissionName(), security.getAllowedRoles()));

        Arrays.stream(RegistrationSecurity.values())
                .forEach(security -> permissionRoleMap.put(security.getPermissionName(), security.getAllowedRoles()));

        Arrays.stream(UserSecurity.values())
                .forEach(security -> permissionRoleMap.put(security.getPermissionName(), security.getAllowedRoles()));

    }

    public SecurityPermission resolve(String permissionName) {
        return new SecurityPermission() {
            @Override
            public String getPermissionName() {
                return permissionName;
            }

            @Override
            public String[] getAllowedRoles() {
                if(permissionRoleMap.containsKey(permissionName)) {
                    return permissionRoleMap.get(permissionName);
                }

                SecurityPermission eventPermission= findInEnum(EventSecurity.values(), permissionName);
                if(eventPermission != null) {
                    return eventPermission.getAllowedRoles();
                }

                SecurityPermission registrationPermission = findInEnum(RegistrationSecurity.values(), permissionName);
                if(registrationPermission != null) {

                    return registrationPermission.getAllowedRoles();
                }

                SecurityPermission userPermission = findInEnum(UserSecurity.values(), permissionName);
                if(userPermission != null)  {
                    return userPermission.getAllowedRoles();
                }

                return new String[]{"SUPERADMIN"};
            }
        };
    }
    private <T extends Enum<T> & SecurityPermission> SecurityPermission findInEnum(T[] values, String permissionName) {
        return Arrays.stream(values)
                .filter(security -> security.getPermissionName().equals(permissionName))
                .findFirst()
                .orElse(null);
    }
    public Set<String> getAllPermissions() {
        return this.permissionRoleMap.keySet();
    }
    public String[] getRolesForPermission(String permissionName) {
        return permissionRoleMap.getOrDefault(permissionName, new String[]{"SUPERADMIN"});
    }
}