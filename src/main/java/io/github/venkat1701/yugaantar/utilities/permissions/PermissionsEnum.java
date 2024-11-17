package io.github.venkat1701.yugaantar.utilities.permissions;

import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import org.apache.catalina.Manager;

import java.util.HashSet;
import java.util.Set;

import static io.github.venkat1701.yugaantar.models.roles.RoleEnum.*;

public enum PermissionsEnum {

    USER_CRUD("Crud Repository", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    PROFILE_CRUD_OWN("Profile Own Operations", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    PROFILE_CRUD_ANY("Profile Update Any", Set.of(ADMIN, SUPERADMIN)),

    REGISTRATION_CREATE("Create Registration", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    REGISTRATION_READ("Read Registration", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    REGISTRATION_UPDATE("Update Registration", Set.of(ADMIN, SUPERADMIN)),
    REGISTRATION_DELETE("Delete Registration", Set.of(ADMIN, SUPERADMIN)),

    PAYMENT_CREATE("Creates Payment", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    PAYMENT_READ_OWN("Reads Own Payment", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    PAYMENT_READ_ANY("Reads Any Payment", Set.of(SUPERADMIN)),
    PAYMENT_UPDATE("Updates Payment", Set.of(SUPERADMIN)),
    PAYMENT_DELETE("Deletes Payment", Set.of(SUPERADMIN)),

    EVENT_CREATE("Creates Events", Set.of(MANAGER, ADMIN, SUPERADMIN)),
    EVENT_READ("Read Events", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    EVENT_UPDATE("Updates Events", Set.of(SUPERADMIN)),
    EVENT_DELETE("Deletes Events", Set.of(SUPERADMIN)),

    VENUE_CREATE("Creates Events", Set.of(MANAGER, ADMIN, SUPERADMIN)),
    VENUE_READ("Read Events", Set.of(GUEST, PARTICIPANT, MANAGER, ADMIN, SUPERADMIN)),
    VENUE_UPDATE("Updates Events", Set.of(MANAGER, ADMIN, SUPERADMIN)),
    VENUE_DELETE("Deletes Events", Set.of(SUPERADMIN));





    private String description;
    private Set<RoleEnum> permissibleRoles = new HashSet<>();

    PermissionsEnum(String description, Set<RoleEnum> permissibleRoles) {
        this.description = description;
        this.permissibleRoles = permissibleRoles;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<RoleEnum> getPermissibleRoles() {
        return this.permissibleRoles;
    }
}
