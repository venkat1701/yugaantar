package io.github.venkat1701.yugaantar.utilities.annotations;


import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum EventSecurity implements SecurityPermission {

    VIEW_ALL("getAll", new String[]{"GUEST", "PARTICIPANT", "MANAGER","ADMIN", "SUPERADMIN"}),
    VIEW("getById", new String[]{"GUEST", "PARTICIPANT", "MANAGER","ADMIN", "SUPERADMIN"}),
    CREATE("save", new String[]{"SUPERADMIN","ADMIN", "MANAGER"}),
    UPDATE("update", new String[]{"SUPERADMIN","ADMIN", "MANAGER"}),
    DELETE("delete", new String[]{"SUPERADMIN","ADMIN"});

    private final String methodName;
    private final String[] allowedRoles;

    EventSecurity(String methodName, String[] allowedRoles) {
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