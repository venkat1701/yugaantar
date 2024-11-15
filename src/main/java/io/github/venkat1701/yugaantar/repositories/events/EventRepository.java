package io.github.venkat1701.yugaantar.repositories.events;

import io.github.venkat1701.yugaantar.models.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
