package io.github.venkat1701.yugaantar.repositories.roles;

import io.github.venkat1701.yugaantar.models.roles.Role;
import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
