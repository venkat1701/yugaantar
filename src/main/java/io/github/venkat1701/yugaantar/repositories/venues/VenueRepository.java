package io.github.venkat1701.yugaantar.repositories.venues;

import io.github.venkat1701.yugaantar.models.venues.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
