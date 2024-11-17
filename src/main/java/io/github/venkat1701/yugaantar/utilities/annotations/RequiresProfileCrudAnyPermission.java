package io.github.venkat1701.yugaantar.utilities.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN',' SUPERADMIN')")
public @interface RequiresProfileCrudAnyPermission {
}
