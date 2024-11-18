package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;

public enum RegistrationSecurity implements SecurityPermission {
    VIEW_ALL("getAll", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    VIEW("getById", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR", "USER"}),
    CREATE("save", new String[]{"USER", "ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    UPDATE("update", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    DELETE("delete", new String[]{"ADMIN", "MANAGER"}),
    APPROVE("approve", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    REJECT("reject", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    CANCEL("cancel", new String[]{"ADMIN", "MANAGER", "USER"}),
    VIEW_OWN("getOwn", new String[]{"USER"}),
    VIEW_PENDING("getPending", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    VIEW_APPROVED("getApproved", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    VIEW_REJECTED("getRejected", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"}),
    VIEW_CANCELLED("getCancelled", new String[]{"ADMIN", "MANAGER", "REGISTRATION_COORDINATOR"});

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