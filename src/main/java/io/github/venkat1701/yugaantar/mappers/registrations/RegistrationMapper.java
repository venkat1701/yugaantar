package io.github.venkat1701.yugaantar.mappers.registrations;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.models.registrations.Registration;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper extends GenericMapper<Registration, RegistrationDTO> {
    public RegistrationMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Registration> getEntityClass() {
        return Registration.class;
    }

    @Override
    protected Class<RegistrationDTO> getDtoClass() {
        return RegistrationDTO.class;
    }
}
