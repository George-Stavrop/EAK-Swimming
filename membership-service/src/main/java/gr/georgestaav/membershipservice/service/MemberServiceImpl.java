package gr.georgestaav.membershipservice.service;

import gr.georgestaav.membershipservice.entity.Member;
import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.exception.ResourceNotFoundException;
import gr.georgestaav.membershipservice.repository.MemberRepository;
import gr.georgestaav.membershipservice.repository.MembershipRepository;
import gr.georgestaav.membershipservice.service.client.AccessFeignClient;
import gr.georgestaav.membershipservice.service.client.SubscriptionFeignClient;
import gr.georgestaav.membershipservice.web.dto.AccessCardDto;
import gr.georgestaav.membershipservice.web.dto.MemberDetailsDto;
import gr.georgestaav.membershipservice.web.dto.SubscriptionDto;
import gr.georgestaav.membershipservice.web.mappers.MemberDetailsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private MemberDetailsMapper mapper;

    private MembershipRepository membershipRepository;
    private MemberRepository memberRepository;

    private AccessFeignClient accessFeignClient;
    private SubscriptionFeignClient subscriptionFeignClient;

    @Override
    public MemberDetailsDto getMemberDetails(String mobileNumber) {
        Member member =  memberRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "mobile", mobileNumber));

        Membership membership = membershipRepository.findByMemberId(member.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "customerId", member.getMemberId().toString()));

        MemberDetailsDto memberDetailsDto = mapper.toDto(member, membership);

        log.info("Fetching subscription for mobileNumber: {}", mobileNumber);
        SubscriptionDto subscriptionDto = subscriptionFeignClient.getSubscription(mobileNumber);
        log.info("Subscription fetched successfully for mobileNumber: {}", mobileNumber);
        memberDetailsDto.setSubscriptionDto(subscriptionDto);

        log.info("Fetching access card for mobileNumber: {}", mobileNumber);
        AccessCardDto accessCardDto = accessFeignClient.getAccessCard(mobileNumber);
        log.info("Access card fetched successfully for mobileNumber: {}", mobileNumber);
        memberDetailsDto.setAccessCardDto(accessCardDto);


        return memberDetailsDto;
    }
}
