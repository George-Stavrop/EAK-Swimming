package gr.georgestaav.membershipservice.web.mappers;


import gr.georgestaav.membershipservice.entity.Member;
import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.web.dto.MemberDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {MembershipMapper.class})
public interface MemberDetailsMapper {

    @Mapping(target = "membershipDto", source = "membership")
    MemberDetailsDto toDto(Member member, Membership membership);
}
