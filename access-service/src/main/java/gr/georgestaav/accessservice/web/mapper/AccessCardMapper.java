package gr.georgestaav.accessservice.web.mapper;

import gr.georgestaav.accessservice.entity.AccessCard;
import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccessCardMapper {

    AccessCardDto toDto(AccessCard accessCard);

    AccessCard toEntity(AccessCardDto accessCardDto);

}
