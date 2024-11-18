package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum DefaultSecurityPermission implements SecurityPermission {
    VIEW_ALL("VIEW_ALL", new String[]{"SUPERADMIN"}),
    VIEW("VIEW", new String[]{"SUPERADMIN"}),
    CREATE("CREATE", new String[]{"SUPERADMIN"}),
    UPDATE("UPDATE", new String[]{"SUPERADMIN"}),
    DELETE("DELETE", new String[]{"SUPERADMIN"});

    private final String permissionName;
    private final String[] allowedRoles;

    DefaultSecurityPermission(String permissionName, String[] allowedRoles) {
        this.permissionName = permissionName;
        this.allowedRoles = allowedRoles;
    }

    @Override
    public String getPermissionName() {
        return permissionName;
    }

    @Override
    public String[] getAllowedRoles() {
        return allowedRoles;
    }
}