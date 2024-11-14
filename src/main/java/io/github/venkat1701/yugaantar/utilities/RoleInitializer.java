package io.github.venkat1701.yugaantar.utilities;

import io.github.venkat1701.yugaantar.models.roles.Role;
import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import io.github.venkat1701.yugaantar.repositories.roles.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.initializeRoles();
    }

    private void initializeRoles() {
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.GUEST, RoleEnum.ADMIN, RoleEnum.SUPERADMIN, RoleEnum.MANAGER, RoleEnum.PARTICIPANT};
        Map<RoleEnum, String> roleDescriptions = Map.of(
                RoleEnum.GUEST, "Default user role",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.SUPERADMIN, "Super Administrator role",
                RoleEnum.MANAGER, "Manager role",
                RoleEnum.PARTICIPANT, "Participant role"
        );

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(roleDescriptions.get(roleName));
                roleRepository.save(role);
            });
        });
    }
}
