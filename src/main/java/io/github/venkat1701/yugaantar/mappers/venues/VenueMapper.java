package io.github.venkat1701.yugaantar.mappers.venues;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.venues.VenueDTO;
import io.github.venkat1701.yugaantar.models.venues.Venue;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper extends GenericMapper<Venue, VenueDTO> {
    public VenueMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Venue> getEntityClass() {
        return Venue.class;
    }

    @Override
    protected Class<VenueDTO> getDtoClass() {
        return VenueDTO.class;
    }
}
