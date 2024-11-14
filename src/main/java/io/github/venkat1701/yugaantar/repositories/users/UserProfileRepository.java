package io.github.venkat1701.yugaantar.repositories.users;

import io.github.venkat1701.yugaantar.models.users.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
