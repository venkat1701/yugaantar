package io.github.venkat1701.yugaantar.services.core.venues;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.mappers.venues.VenueMapper;
import io.github.venkat1701.yugaantar.models.venues.Venue;
import io.github.venkat1701.yugaantar.repositories.venues.VenueRepository;
import io.github.venkat1701.yugaantar.utilities.annotations.VenueSecurity;

public abstract class VenueService extends GenericPersistenceService<Venue, Long, VenueSecurity> {
    public VenueService(VenueRepository repository, VenueMapper mapper, GenericSecurityEvaluator evaluator) {
        super(repository, mapper, VenueSecurity.class, evaluator);
    }
}
