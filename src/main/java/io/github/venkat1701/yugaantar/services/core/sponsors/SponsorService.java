package io.github.venkat1701.yugaantar.services.core.sponsors;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.models.sponsors.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class SponsorService extends GenericPersistenceService<Sponsor, Long> {


    public SponsorService(JpaRepository<Sponsor, Long> repository, GenericMapper<Sponsor, ?> mapper) {
        super(repository, mapper);
    }
}
