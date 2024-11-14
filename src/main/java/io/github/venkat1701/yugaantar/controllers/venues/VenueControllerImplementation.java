package io.github.venkat1701.yugaantar.controllers.venues;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;
import io.github.venkat1701.yugaantar.dtos.venues.VenueDTO;
import io.github.venkat1701.yugaantar.mappers.venues.VenueMapper;
import io.github.venkat1701.yugaantar.models.venues.Venue;
import io.github.venkat1701.yugaantar.services.core.venues.VenueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/venues")
public class VenueControllerImplementation extends GenericCrudController<Venue, VenueDTO, Long> {

    private final VenueService venueService;
    private final VenueMapper venueMapper;

    public VenueControllerImplementation(VenueService venueService, VenueMapper mapper) {
        super(venueService, mapper);
        this.venueService = venueService;
        this.venueMapper = mapper;
    }
}
