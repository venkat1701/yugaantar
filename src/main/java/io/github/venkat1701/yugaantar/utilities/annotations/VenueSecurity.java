package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum VenueSecurity implements SecurityPermission {
    VIEW_ALL("getAll", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    VIEW("getById", new String[]{"ADMIN", "MANAGER", "EVENT_COORDINATOR", "USER"}),
    CREATE("save", new String[]{"ADMIN", "MANAGER"}),
    UPDATE("update", new String[]{"ADMIN", "MANAGER", "EVENT_COORDINATOR"}),
    DELETE("delete", new String[]{"ADMIN"});

    private final String methodName;
    private final String[] allowedRoles;

    VenueSecurity(String methodName, String[] allowedRoles) {
        this.methodName = methodName;
        this.allowedRoles = allowedRoles;
    }

    @Override
    public String getPermissionName() {
        return this.methodName;
    }

    @Override
    public String[] getAllowedRoles() {
        return this.allowedRoles;
    }
}