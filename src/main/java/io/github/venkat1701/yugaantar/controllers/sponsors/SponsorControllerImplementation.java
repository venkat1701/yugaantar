package io.github.venkat1701.yugaantar.controllers.sponsors;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;
import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.dtos.sponsors.SponsorDTO;
import io.github.venkat1701.yugaantar.models.sponsors.Sponsor;
import io.github.venkat1701.yugaantar.utilities.annotations.SponsorSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sponsors")
public class SponsorControllerImplementation extends GenericCrudController<Sponsor, SponsorDTO, Long, SponsorSecurity> {


    public SponsorControllerImplementation(GenericPersistenceService<Sponsor, Long, SponsorSecurity> service, GenericMapper<Sponsor, SponsorDTO> dtoConverter) {
        super(service, dtoConverter);
    }


}
