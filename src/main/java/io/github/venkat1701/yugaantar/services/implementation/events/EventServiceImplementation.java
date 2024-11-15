package io.github.venkat1701.yugaantar.services.implementation.events;

import io.github.venkat1701.yugaantar.mappers.events.EventsMapper;
import io.github.venkat1701.yugaantar.repositories.events.EventRepository;
import io.github.venkat1701.yugaantar.services.core.events.EventService;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImplementation extends EventService {

    private final EventRepository eventRepository;
    private final EventsMapper eventsMapper;

    public EventServiceImplementation(EventRepository eventRepository, EventsMapper mapper) {
        super(eventRepository, mapper);
        this.eventRepository = eventRepository;
        this.eventsMapper = mapper;
    }
}
