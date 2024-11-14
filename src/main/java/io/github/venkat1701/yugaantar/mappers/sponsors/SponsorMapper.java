package io.github.venkat1701.yugaantar.mappers.sponsors;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.sponsors.SponsorDTO;
import io.github.venkat1701.yugaantar.models.sponsors.Sponsor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SponsorMapper extends GenericMapper<Sponsor, SponsorDTO> {
    public SponsorMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Sponsor> getEntityClass() {
        return Sponsor.class;
    }

    @Override
    protected Class<SponsorDTO> getDtoClass() {
        return SponsorDTO.class;
    }
}
