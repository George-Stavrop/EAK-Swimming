package gr.georgestaav.membershipservice.web.mappers;


import gr.georgestaav.membershipservice.entity.Member;
import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.web.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {MembershipMapper.class})
public interface MemberMapper {

    @Mapping(target = "membershipDto", source = "membership")
    MemberDto toDto(Member member, Membership membership);

    Member toEntity(MemberDto memberDto);

    @Mapping(target = "memberId", ignore = true) // ID δεν αλλάζει
    void updateEntity(MemberDto memberDto, @MappingTarget Member member);
}
