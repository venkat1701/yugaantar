package io.github.venkat1701.yugaantar.utilities.annotations;

import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;


public enum SponsorSecurity implements SecurityPermission {
        VIEW_ALL("getAll", new String[]{"ADMIN", "MANAGER", "SPONSOR_COORDINATOR"}),
        VIEW("getById", new String[]{"ADMIN", "MANAGER", "SPONSOR_COORDINATOR", "USER"}),
        CREATE("save", new String[]{"ADMIN", "MANAGER", "SPONSOR_COORDINATOR"}),
        UPDATE("update", new String[]{"ADMIN", "MANAGER", "SPONSOR_COORDINATOR"}),
        DELETE("delete", new String[]{"ADMIN", "MANAGER"}),
        APPROVE("approve", new String[]{"ADMIN", "MANAGER"}),
        REJECT("reject", new String[]{"ADMIN", "MANAGER"}),
        VIEW_PENDING("getPending", new String[]{"ADMIN", "MANAGER", "SPONSOR_COORDINATOR"});

        private final String methodName;
        private final String[] allowedRoles;

        SponsorSecurity(String methodName, String[] allowedRoles) {
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
