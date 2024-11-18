package io.github.venkat1701.yugaantar.services.implementation.sponsors;

import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.mappers.sponsors.SponsorMapper;
import io.github.venkat1701.yugaantar.repositories.sponsors.SponsorshipRepository;
import io.github.venkat1701.yugaantar.services.core.sponsors.SponsorService;
import org.springframework.stereotype.Service;

@Service
public class SponsorServiceImplementation extends SponsorService {

    private final SponsorshipRepository sponsorshipRepository;
    private final SponsorMapper sponsorMapper;

    public SponsorServiceImplementation(SponsorshipRepository repository, SponsorMapper mapper, GenericSecurityEvaluator evaluator) {
        super(repository, mapper, evaluator);
        this.sponsorshipRepository = repository;
        this.sponsorMapper = mapper;
    }
}
