package gr.georgestaav.membershipservice.unit;

import gr.georgestaav.membershipservice.entity.Member;
import gr.georgestaav.membershipservice.entity.Membership;
import gr.georgestaav.membershipservice.exception.MemberAlreadyExistsException;
import gr.georgestaav.membershipservice.exception.ResourceNotFoundException;
import gr.georgestaav.membershipservice.repository.MemberRepository;
import gr.georgestaav.membershipservice.repository.MembershipRepository;
import gr.georgestaav.membershipservice.service.MembershipServiceImpl;
import gr.georgestaav.membershipservice.web.dto.MemberDto;
import gr.georgestaav.membershipservice.web.dto.MembershipCreatedEvent;
import gr.georgestaav.membershipservice.web.mappers.MemberMapper;
import gr.georgestaav.membershipservice.web.mappers.MembershipMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private MembershipMapper membershipMapper;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MembershipServiceImpl service;

    private MemberDto memberDto;
    private Member member;
    private Membership membership;

    @BeforeEach
    public void setup(){

        memberDto = new MemberDto();
        memberDto.setMobileNumber("1234567890");

        member = new Member();
        member.setMemberId(1L);
        member.setMobileNumber("1234567890");

        membership = new Membership();
        membership.setMemberId(1L);
    }



    @Test
    void givenMemberDoesNotExist_whenCreateMembership_thenMembershipIsSaved() {
        // GIVEN
        given(memberRepository.findByMobileNumber(any()))
                .willReturn(Optional.empty());

        given(memberMapper.toEntity(memberDto)).willReturn(member);
        given(memberRepository.save(member)).willReturn(member);
        given(membershipRepository.save(any())).willReturn(membership);


        // WHEN
        service.createMembership(memberDto);

        verify(memberRepository).save(member);
        verify(membershipRepository).save(any());
        verify(eventPublisher).publishEvent(any(MembershipCreatedEvent.class));
    }

    @Test
    void givenMemberExists_whenCreateMembership_thenThrowException() {
        // GIVEN
        given(memberRepository.findByMobileNumber(any()))
                .willReturn(Optional.of(new Member()));

        //WHEN & THEN
        assertThatThrownBy(() -> service.createMembership(memberDto))
                .isInstanceOf(MemberAlreadyExistsException.class);

        then(memberRepository).should(never()).save(any());
        then(membershipRepository).should(never()).save(any());
    }


    @Test
    void givenValidMobile_whenGetMembership_thenReturnMemberDto() {

        // GIVEN
        given(memberRepository.findByMobileNumber(member.getMobileNumber()))
                .willReturn(Optional.of(member));

        given(membershipRepository.findByMemberId(1L))
                .willReturn(Optional.of(membership));

        given(memberMapper.toDto(member, membership))
                .willReturn(memberDto);

        // WHEN
        MemberDto result = service.getMembership(member.getMobileNumber());

        // THEN
        assertThat(result).isNotNull();
        then(memberMapper).should().toDto(member, membership);

    }

    @Test
    void givenInvalidMobile_whenGetMembership_thenThrowException() {
        // GIVEN
        given(memberRepository.findByMobileNumber(any()))
                .willReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> service.getMembership("123"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
