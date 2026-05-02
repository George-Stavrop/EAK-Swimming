package gr.georgestaav.membershipservice.web.mappers;

import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.web.dto.MembershipDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MembershipMapper {

    MembershipDto toDto(Membership membership);

    Membership toEntity(MembershipDto membershipDto);

    @Mapping(target = "membershipNumber", ignore = true)
    @Mapping(target = "memberId", ignore = true)
    void updateEntity(MembershipDto membershipDto, @MappingTarget Membership membership);

}
