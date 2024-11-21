package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum EntryTicketSecurity implements SecurityPermission {

    VIEW_ALL("getAll", new String[]{ "ADMIN", "MANAGER"}),
    VIEW("getById", new String[]{"ADMIN", "MANAGER", "USER"}),
    CREATE("save", new String[]{"ADMIN", "MANAGER"}),
    UPDATE("update", new String[]{"ADMIN", "MANAGER"}),
    DELETE("delete", new String[]{"ADMIN"});
    
    private final String permissionName;
    private final String[] allowedRoles;

    EntryTicketSecurity(String permissionName, String[] allowedRoles) {
        this.permissionName = permissionName;
        this.allowedRoles = allowedRoles;
    }

    @Override
    public String getPermissionName() {
        return this.permissionName;
    }

    @Override
    public String[] getAllowedRoles() {
        return this.allowedRoles;
    }
}
