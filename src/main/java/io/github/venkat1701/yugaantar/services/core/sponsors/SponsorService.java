package io.github.venkat1701.yugaantar.services.core.sponsors;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.models.sponsors.Sponsor;
import io.github.venkat1701.yugaantar.utilities.annotations.SponsorSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class SponsorService extends GenericPersistenceService<Sponsor, Long, SponsorSecurity> {


    public SponsorService(JpaRepository<Sponsor, Long> repository, GenericMapper<Sponsor, ?> mapper, GenericSecurityEvaluator evaluator) {
        super(repository, mapper, SponsorSecurity.class, evaluator);
    }
}
