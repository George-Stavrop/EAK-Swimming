package gr.georgestaav.messageservice;

import gr.georgestaav.messageservice.dto.EmailDeliveredEvent;
import gr.georgestaav.messageservice.dto.ExpiredSubscriptionEvent;
import gr.georgestaav.messageservice.dto.ExpiringSubscriptionEvent;
import gr.georgestaav.messageservice.dto.MembershipCreatedEvent;
import gr.georgestaav.messageservice.functions.MessageFunctions;
import gr.georgestaav.messageservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MessageFunctionsTest {

    @Mock
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MessageFunctions messageFunctions;

    @Test
    void givenSuccessfulEmail_whenSendWelcomeEmail_thenReturnSuccess() {
        MembershipCreatedEvent event = new MembershipCreatedEvent(
                123456789L, "Γιώργης", "george@test.com", "6912345678");

        given(emailService.sendWelcomeEmail("george@test.com", 123456789L))
                .willReturn(true);

        EmailDeliveredEvent result = messageFunctions.sendWelcomeEmail().apply(event);

        assertThat(result.membershipNumber()).isEqualTo(123456789L);
        assertThat(result.emailStatus()).isEqualTo("SUCCESS");
    }

    @Test
    void givenFailedEmail_whenSendWelcomeEmail_thenReturnFailed() {
        MembershipCreatedEvent event = new MembershipCreatedEvent(
                123456789L, "Γιώργης", "george@test.com", "6912345678");

        given(emailService.sendWelcomeEmail("george@test.com", 123456789L))
                .willReturn(false);

        EmailDeliveredEvent result = messageFunctions.sendWelcomeEmail().apply(event);

        assertThat(result.emailStatus()).isEqualTo("FAILED");
    }

    @Test
    void givenExpiringEvent_whenExpiringSubscription_thenCallEmailService() {
        ExpiringSubscriptionEvent event = new ExpiringSubscriptionEvent(
                "6912345678", "Γιώργης", "george@test.com", LocalDate.now().plusDays(5));

        messageFunctions.expiringSubscription().accept(event);

        verify(emailService).sendExpiryWarning("george@test.com", "Γιώργης", event.endDate());
    }

    @Test
    void givenExpiredEvent_whenExpiredSubscription_thenCallEmailService() {
        ExpiredSubscriptionEvent event = new ExpiredSubscriptionEvent(
                "6912345678", "Γιώργης", "george@test.com", LocalDate.now().minusDays(1));

        messageFunctions.expiredSubscription().accept(event);

        verify(emailService).sendExpiredMessage("george@test.com", "Γιώργης", event.endDate());
    }
}
