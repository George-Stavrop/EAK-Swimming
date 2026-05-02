package gr.georgestaav.accessservice.unit;


import gr.georgestaav.accessservice.constants.AccessConstants;
import gr.georgestaav.accessservice.entity.AccessCard;
import gr.georgestaav.accessservice.exception.AccessCardAlreadyExistsException;
import gr.georgestaav.accessservice.exception.ResourceNotFoundException;
import gr.georgestaav.accessservice.repository.AccessCardRepository;
import gr.georgestaav.accessservice.service.AccessCardServiceImpl;
import gr.georgestaav.accessservice.service.client.SubscriptionFeignClient;
import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import gr.georgestaav.accessservice.web.dto.AccessCheckDto;
import gr.georgestaav.accessservice.web.dto.SubscriptionDto;
import gr.georgestaav.accessservice.web.mapper.AccessCardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

    @Mock
    private AccessCardRepository accessCardRepository;

    @Mock
    private AccessCardMapper mapper;

    @Mock
    private SubscriptionFeignClient subscriptionFeignClient;

    @InjectMocks
    private AccessCardServiceImpl service;

    private AccessCard accessCard;

    @BeforeEach
    void setUp() {
        accessCard = new AccessCard();
        accessCard.setAccessCardId(1L);
        accessCard.setMobileNumber("6912345678");
        accessCard.setAccessCardNumber("100000000001");
        accessCard.setAccessCardType(AccessConstants.RFID_CARD);
        accessCard.setIsActive(true);
    }


    @Test
    void givenMobileDoesNotExist_whenCreateAccessCard_thenAccessCardIsSaved() {
        given(accessCardRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.empty());

        service.createAccessCard("6912345678");

        verify(accessCardRepository).save(any(AccessCard.class));
    }

    @Test
    void givenMobileAlreadyExists_whenCreateAccessCard_thenThrowException() {
        given(accessCardRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(accessCard));

        assertThatThrownBy(() -> service.createAccessCard("6912345678"))
                .isInstanceOf(AccessCardAlreadyExistsException.class);

        then(accessCardRepository).should(never()).save(any());
    }

    // GET
    @Test
    void givenValidMobile_whenGetAccessCard_thenReturnAccessCardDto() {
        AccessCardDto accessCardDto = new AccessCardDto();
        accessCardDto.setMobileNumber("6912345678");

        given(accessCardRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(accessCard));
        given(mapper.toDto(accessCard))
                .willReturn(accessCardDto);

        AccessCardDto result = service.getAccessCard("6912345678");

        assertThat(result).isNotNull();
        assertThat(result.getMobileNumber()).isEqualTo("6912345678");
        then(mapper).should().toDto(accessCard);
    }

    @Test
    void givenInvalidMobile_whenGetAccessCard_thenThrowException() {
        given(accessCardRepository.findByMobileNumber(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAccessCard("6912345678"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // CAN ENTER
    @Test
    void givenCardNotFound_whenCanEnter_thenReturnCardNotFound() {
        given(accessCardRepository.findByAccessCardNumber("100000000001"))
                .willReturn(Optional.empty());

        AccessCheckDto result = service.canEnter("100000000001");

        assertThat(result.allowed()).isFalse();
        assertThat(result.reason()).isEqualTo(AccessConstants.CARD_NOT_FOUND);
    }

    @Test
    void givenCardInactive_whenCanEnter_thenReturnCardInactive() {
        accessCard.setIsActive(false);

        given(accessCardRepository.findByAccessCardNumber("100000000001"))
                .willReturn(Optional.of(accessCard));

        AccessCheckDto result = service.canEnter("100000000001");

        assertThat(result.allowed()).isFalse();
        assertThat(result.reason()).isEqualTo(AccessConstants.CARD_INACTIVE);
        then(subscriptionFeignClient).should(never()).getSubscription(any());
    }

    @Test
    void givenSubscriptionServiceUnavailable_whenCanEnter_thenReturnUnavailable() {
        given(accessCardRepository.findByAccessCardNumber("100000000001"))
                .willReturn(Optional.of(accessCard));
        given(subscriptionFeignClient.getSubscription("6912345678"))
                .willReturn(null);

        AccessCheckDto result = service.canEnter("100000000001");

        assertThat(result.allowed()).isFalse();
        assertThat(result.reason()).isEqualTo(AccessConstants.SUBSCRIPTION_SERVICE_UNAVAILABLE);
    }

    @Test
    void givenSubscriptionExpired_whenCanEnter_thenReturnSubscriptionExpired() {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setActive(false);

        given(accessCardRepository.findByAccessCardNumber("100000000001"))
                .willReturn(Optional.of(accessCard));
        given(subscriptionFeignClient.getSubscription("6912345678"))
                .willReturn(subscriptionDto);

        AccessCheckDto result = service.canEnter("100000000001");

        assertThat(result.allowed()).isFalse();
        assertThat(result.reason()).isEqualTo(AccessConstants.SUBSCRIPTION_EXPIRED);
    }

    @Test
    void givenAllChecksPass_whenCanEnter_thenReturnAccessGranted() {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setActive(true);

        given(accessCardRepository.findByAccessCardNumber("100000000001"))
                .willReturn(Optional.of(accessCard));
        given(subscriptionFeignClient.getSubscription("6912345678"))
                .willReturn(subscriptionDto);

        AccessCheckDto result = service.canEnter("100000000001");

        assertThat(result.allowed()).isTrue();
        assertThat(result.reason()).isEqualTo(AccessConstants.ACCESS_GRANTED);
    }

    @Test
    void givenActiveCard_whenToggleAccessCard_thenCardBecomesInactive() {
        accessCard.setIsActive(true);

        given(accessCardRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(accessCard));

        service.toggleAccessCard("6912345678");

        assertThat(accessCard.getIsActive()).isFalse();
        verify(accessCardRepository).save(accessCard);
    }
}
