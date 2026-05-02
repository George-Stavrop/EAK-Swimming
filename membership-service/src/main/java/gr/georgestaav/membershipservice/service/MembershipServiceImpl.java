package gr.georgestaav.membershipservice.service;

import gr.georgestaav.membershipservice.exception.MemberAlreadyExistsException;
import gr.georgestaav.membershipservice.exception.ResourceNotFoundException;
import gr.georgestaav.membershipservice.constants.MembershipConstants;
import gr.georgestaav.membershipservice.web.dto.MemberDto;
import gr.georgestaav.membershipservice.web.dto.MembershipCreatedEvent;
import gr.georgestaav.membershipservice.web.dto.MembershipDto;
import gr.georgestaav.membershipservice.entity.Member;
import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.repository.MemberRepository;
import gr.georgestaav.membershipservice.repository.MembershipRepository;
import gr.georgestaav.membershipservice.web.mappers.MemberMapper;
import gr.georgestaav.membershipservice.web.mappers.MembershipMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {


    private final StreamBridge streamBridge;
    private MembershipRepository membershipRepository;
    private MemberRepository memberRepository;
    private MemberMapper memberMapper;
    private MembershipMapper membershipMapper;
    private ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public void createMembership(MemberDto memberDto) {

        //Check if member exists
        memberRepository.findByMobileNumber(memberDto.getMobileNumber())
                .ifPresent( optionalMember -> {
                    throw new MemberAlreadyExistsException("There is an existing membership with the number" + memberDto.getMobileNumber());
                });

        Member member = memberMapper.toEntity(memberDto);
        Member savedMember = memberRepository.save(member);

        Membership savedMembership = membershipRepository.save(createNewMembership(savedMember));

        eventPublisher.publishEvent(new MembershipCreatedEvent(
                savedMembership.getMembershipNumber(),
                savedMember.getName(),
                savedMember.getEmail(),
                savedMember.getMobileNumber()
        ));
    }


    private Membership createNewMembership(Member savedMember) {
        Membership newMembership = new Membership();
        newMembership.setMemberId(savedMember.getMemberId());
        long randomMembershipNumber = 1000000000L + new Random().nextInt(900000000);

        newMembership.setMembershipNumber(randomMembershipNumber);
        newMembership.setMembershipType(MembershipConstants.GENERAL_ACCESS);
        newMembership.setFacilityName(MembershipConstants.FACILITY_NAME);

        return newMembership;
    }

    @Override
    public MemberDto getMembership(String mobileNumber) {
       Member member =  memberRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "mobile", mobileNumber));

       Membership membership = membershipRepository.findByMemberId(member.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Membership", "customerId", member.getMemberId().toString()));

       return memberMapper.toDto(member,membership);


    }

    @Override
    public boolean updateMembership(MemberDto memberDto) {
        boolean isUpdated = false;
        MembershipDto membershipDto = memberDto.getMembershipDto();
        if (membershipDto != null) {
            Membership membership = membershipRepository.findById(membershipDto.getMembershipNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Membership", "MembershipNumber", membershipDto.getMembershipNumber().toString()));

            membershipMapper.updateEntity(membershipDto,membership);
            membershipRepository.save(membership);

            Long memberId = membership.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("Member", "MemberId", memberId.toString()));

            memberMapper.updateEntity(memberDto,member);
            memberRepository.save(member);
            isUpdated = true;

        }
        return isUpdated;
    }


    @Override
    public boolean deleteMembership(String mobileNumber) {

        Member member = memberRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        membershipRepository.deleteByMemberId(member.getMemberId());
        memberRepository.deleteById(member.getMemberId());
        return true;
    }

    @Override
    @Transactional
    public void updateEmailStatus(Long membershipNumber) {

        log.info("Updating DB status for membership: {}", membershipNumber);

         membershipRepository.findByMembershipNumber(membershipNumber)
                 .ifPresentOrElse(membership -> {
                     membership.setEmailSent(true);
                     membershipRepository.save(membership);
                     log.info("Status updated for membership: {}", membershipNumber);
                 },
                  () -> log.warn("Membership {} not found for status update", membershipNumber)                         );

    }
}


