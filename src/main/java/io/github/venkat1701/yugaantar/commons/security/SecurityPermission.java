package io.github.venkat1701.yugaantar.commons.security;

import org.springframework.stereotype.Component;

import java.security.Permission;

@Component("securityClass")
public interface SecurityPermission{
    String getPermissionName();
    String[] getAllowedRoles();
}


