package io.github.venkat1701.yugaantar.services.core.events;

import io.github.venkat1701.yugaantar.commons.services.GenericCrudService;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.events.EventsMapper;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.repositories.events.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EventService extends GenericPersistenceService<Event, Long> {


    public EventService(EventRepository repository, EventsMapper mapper) {
        super(repository, mapper);
    }
}
