package io.github.venkat1701.yugaantar.services.core.events;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.services.GenericCrudService;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.events.EventsMapper;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.repositories.events.EventRepository;
import io.github.venkat1701.yugaantar.utilities.annotations.EventSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EventService extends GenericPersistenceService<Event, Long, EventSecurity> {


    public EventService(EventRepository repository, EventsMapper mapper, GenericSecurityEvaluator evaluator) {
        super(repository, mapper, EventSecurity.class, evaluator);
    }
}
