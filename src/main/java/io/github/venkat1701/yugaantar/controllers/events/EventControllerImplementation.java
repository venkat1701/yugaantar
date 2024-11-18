package io.github.venkat1701.yugaantar.controllers.events;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;
import io.github.venkat1701.yugaantar.dtos.events.EventDTO;
import io.github.venkat1701.yugaantar.mappers.events.EventsMapper;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.services.core.events.EventService;
import io.github.venkat1701.yugaantar.utilities.annotations.EventSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
public class EventControllerImplementation extends GenericCrudController<Event, EventDTO, Long, EventSecurity> {

    private final EventService eventService;
    private final EventsMapper mapper;

    public EventControllerImplementation(EventService service, EventsMapper dtoConverter) {
        super(service, dtoConverter);
        this.eventService = service;
        this.mapper = dtoConverter;
    }
}
