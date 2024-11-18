package io.github.venkat1701.yugaantar.services.implementation.venues;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.mappers.venues.VenueMapper;
import io.github.venkat1701.yugaantar.repositories.venues.VenueRepository;
import io.github.venkat1701.yugaantar.services.core.venues.VenueService;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImplementation extends VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueServiceImplementation(VenueRepository repository, VenueMapper mapper, GenericSecurityEvaluator evaluator) {
        super(repository, mapper, evaluator);
        this.venueRepository = repository;
        this.venueMapper = mapper;
    }
}
