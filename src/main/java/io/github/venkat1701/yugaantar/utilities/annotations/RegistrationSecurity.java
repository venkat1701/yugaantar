package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum RegistrationSecurity implements SecurityPermission {
    VIEW_ALL("getAll", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    VIEW("getById", new String[]{"ADMIN", "MANAGER", "SUPERADMIN", "PARTICIPANT"}),
    CREATE("save", new String[]{"GUEST", "PARTICIPANT", "ADMIN", "MANAGER", "SUPERADMIN"}),
    UPDATE("update", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    DELETE("delete", new String[]{"SUPERADMIN", "ADMIN"}),
    APPROVE("approve", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    REJECT("reject", new String[]{"ADMIN", "SUPERADMIN"}),
    CANCEL("cancel", new String[]{"ADMIN", "MANAGER", "GUEST", "PARTICIPANT"}),
    VIEW_OWN("getOwn", new String[]{"PARTICIPANT", "GUEST"}),
    VIEW_PENDING("getPending", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    VIEW_APPROVED("getApproved", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    VIEW_REJECTED("getRejected", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"}),
    VIEW_CANCELLED("getCancelled", new String[]{"ADMIN", "MANAGER", "SUPERADMIN"});

    private final String methodName;
    private final String[] allowedRoles;

    RegistrationSecurity(String methodName, String[] allowedRoles) {
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