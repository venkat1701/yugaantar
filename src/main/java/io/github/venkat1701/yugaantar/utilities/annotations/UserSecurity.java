package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum UserSecurity implements SecurityPermission {
    VIEW_ALL("getAll", new String[]{"SUPERADMIN", "ADMIN", "GUEST", "PARTICIPANT", "MANAGER"}),
    VIEW("getById", new String[]{"SUPERADMIN", "ADMIN", "MANAGER", "PARTICIPANT", "GUEST"}),
    CREATE("save", new String[]{"GUEST", "SUPERADMIN", "ADMIN", "MANAGER"}),
    UPDATE("update", new String[]{"GUEST", "SUPERADMIN", "ADMIN", "MANAGER"}),
    DELETE("delete", new String[]{"SUPERADMIN", "ADMIN", "GUEST", "PARTICIPANT"});

    private final String methodName;
    private final String[] allowedRoles;

    UserSecurity(String methodName, String[] allowedRoles) {
        this.methodName = methodName;
        this.allowedRoles = allowedRoles;
        System.out.println("HellO: "+this.allowedRoles);
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