package io.github.venkat1701.yugaantar.commons.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #target, @defaultSecurityPermissionResolver.resolve(#permission))")
public @interface SecuredResource {
    String permission();
    Class<?> securityClass() default Object.class;
}