package io.github.venkat1701.yugaantar.repositories.sponsors;

import io.github.venkat1701.yugaantar.models.sponsors.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsor, Long> {
}
