package io.github.venkat1701.yugaantar.utilities.permissions;

import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import org.springframework.javapoet.AnnotationSpec;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.TypeSpec;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Paths;
import java.util.Arrays;

public class AnnotationGenerator {
    public static void main(String[] args) {
        Arrays.stream(PermissionsEnum.values())
                .forEach(permission -> {
                    var annotationName = "Requires"+getCamelCaseName(permission.name())+"Permission";
                    var roles = permission.getPermissibleRoles().stream()
                            .map(RoleEnum::name)
                            .toArray(String[]::new);

                    String rolesSpEL = String.join(", ", roles);
                    var preAuthValue = "hasAnyRole('"+rolesSpEL.replace(",","','")+"')";
                    AnnotationSpec annotation = AnnotationSpec.builder(PreAuthorize.class)
                            .addMember("value", "$S", preAuthValue)
                            .build();
                    AnnotationSpec retention = AnnotationSpec.builder(Retention.class)
                            .addMember("value", "$T.RUNTIME", RetentionPolicy.class)
                            .build();
                    TypeSpec annotationType = TypeSpec.annotationBuilder(annotationName)
                            .addAnnotation(retention)
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(annotation)
                            .build();
                    JavaFile javaFile = JavaFile.builder("io.github.venkat1701.yugaantar.utilities.annotations", annotationType)
                            .build();

                    try{
                        javaFile.writeTo(Paths.get("src/main/java"));
                    } catch(IOException ie) {
                        throw new RuntimeException(ie);
                    }
                });
    }

    private static String getCamelCaseName(String name) {
        return Arrays.stream(name.split("_"))
                .map(s -> s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase())
                .reduce("", String::concat);
    }
}
