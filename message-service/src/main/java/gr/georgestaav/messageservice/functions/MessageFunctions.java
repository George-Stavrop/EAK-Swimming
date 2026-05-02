package gr.georgestaav.messageservice.functions;

import gr.georgestaav.messageservice.dto.EmailDeliveredEvent;
import gr.georgestaav.messageservice.dto.ExpiredSubscriptionEvent;
import gr.georgestaav.messageservice.dto.ExpiringSubscriptionEvent;
import gr.georgestaav.messageservice.dto.MembershipCreatedEvent;
import gr.georgestaav.messageservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessageFunctions {

    private final EmailService emailService;

    @Bean
    public Function<MembershipCreatedEvent, EmailDeliveredEvent> sendWelcomeEmail() {

        return event -> {
            log.info("Sending email with the details{}", event.toString());

           boolean success =  emailService.sendWelcomeEmail(event.email(), event.membershipNumber());

            return new EmailDeliveredEvent(
                    event.membershipNumber(),
                    success ? "SUCCESS" : "FAILED",
                    LocalDateTime.now());
        };
    }


    @Bean
    public Consumer<ExpiringSubscriptionEvent> expiringSubscription() {
        return event -> {
            log.info("Received expiring subscription event for: {}", event.mobileNumber());
            emailService.sendExpiryWarning(event.email(), event.name(), event.endDate());
        };
    }

    @Bean
    public Consumer<ExpiredSubscriptionEvent> expiredSubscription() {
        return event -> {
            log.info("Received expired subscription event for: {}", event.mobileNumber());
            emailService.sendExpiredMessage(event.email(), event.name(), event.endDate());
        };
    }
}
