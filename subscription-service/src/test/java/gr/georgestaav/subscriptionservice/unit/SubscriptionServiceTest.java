package gr.georgestaav.subscriptionservice.unit;

import gr.georgestaav.subscriptionservice.constants.SubscriptionConstants;
import gr.georgestaav.subscriptionservice.entity.Subscription;
import gr.georgestaav.subscriptionservice.exception.ResourceNotFoundException;
import gr.georgestaav.subscriptionservice.exception.SubscriptionAlreadyExistsException;
import gr.georgestaav.subscriptionservice.repository.SubscriptionRepository;
import gr.georgestaav.subscriptionservice.service.SubscriptionServiceImpl;
import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;
import gr.georgestaav.subscriptionservice.web.mapper.SubscriptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    SubscriptionRepository subscriptionRepository;

    @Mock
    SubscriptionMapper mapper;

    @InjectMocks
    SubscriptionServiceImpl service;

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        subscription = new Subscription();
        subscription.setSubscriptionId(1L);
        subscription.setMobileNumber("6912345678");
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setAmountPaid(BigDecimal.valueOf(SubscriptionConstants.MONTHLY_FEE));
    }

    @Test
    void givenMobileDoesNotExist_whenCreateSubscription_thenSubscriptionIsSaved() {
        given(subscriptionRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.empty());

        service.createSubscription("6912345678");

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void givenMobileAlreadyExists_whenCreateSubscription_thenThrowException() {
        given(subscriptionRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(subscription));

        assertThatThrownBy(() -> service.createSubscription("6912345678"))
                .isInstanceOf(SubscriptionAlreadyExistsException.class);

        then(subscriptionRepository).should(never()).save(any());
    }


    @Test
    void givenValidMobile_whenGetSubscription_thenReturnSubscriptionDto() {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setMobileNumber("6912345678");

        given(subscriptionRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(subscription));
        given(mapper.toDto(subscription))
                .willReturn(subscriptionDto);

        SubscriptionDto result = service.getSubscription("6912345678");

        assertThat(result).isNotNull();
        assertThat(result.getMobileNumber()).isEqualTo("6912345678");
        then(mapper).should().toDto(subscription);
    }

    @Test
    void givenInvalidMobile_whenGetSubscription_thenThrowException() {
        given(subscriptionRepository.findByMobileNumber(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSubscription("6912345678"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void givenExpiredSubscription_whenRenewSubscription_thenExtendFromToday() {
        subscription.setEndDate(LocalDate.now().minusDays(5)); // έληξε

        given(subscriptionRepository.findByMobileNumber("6912345678"))
                .willReturn(Optional.of(subscription));

        service.renewSubscription("6912345678");

        // Πάντα από σήμερα ανεξάρτητα αν είχε λήξει
        assertThat(subscription.getEndDate()).isEqualTo(LocalDate.now().plusMonths(1));
        verify(subscriptionRepository).save(subscription);
    }


    @Test
    void givenFutureEndDate_whenIsActive_thenReturnTrue() {
        subscription.setEndDate(LocalDate.now().plusDays(1));
        assertThat(subscription.isActive()).isTrue();
    }

    @Test
    void givenPastEndDate_whenIsActive_thenReturnFalse() {
        subscription.setEndDate(LocalDate.now().minusDays(1));
        assertThat(subscription.isActive()).isFalse();
    }

    @Test
    void givenNullEndDate_whenIsActive_thenReturnFalse() {
        subscription.setEndDate(null);
        assertThat(subscription.isActive()).isFalse();
    }
}
