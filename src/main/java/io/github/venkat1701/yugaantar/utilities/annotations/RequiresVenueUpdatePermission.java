package io.github.venkat1701.yugaantar.utilities.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('MANAGER',' SUPERADMIN',' ADMIN')")
public @interface RequiresVenueUpdatePermission {
}
