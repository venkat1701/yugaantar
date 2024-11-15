package io.github.venkat1701.yugaantar.mappers.events;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.events.EventDTO;
import io.github.venkat1701.yugaantar.models.events.Event;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventsMapper extends GenericMapper<Event, EventDTO> {

    public EventsMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Event> getEntityClass() {
        return Event.class;
    }

    @Override
    protected Class<EventDTO> getDtoClass() {
        return EventDTO.class;
    }
}
