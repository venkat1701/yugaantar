package io.github.venkat1701.yugaantar.commons.security;

import org.springframework.stereotype.Component;

@Component("defaultSecurityPermissionResolver")
public class DefaultSecurityPermissionResolver {
    public SecurityPermission resolve(String permissionName) {
        return new SecurityPermission() {
            @Override
            public String getPermissionName() {
                return permissionName;
            }

            @Override
            public String[] getAllowedRoles() {
                return new String[]{"SUPERADMIN"};
            }
        };
    }
}