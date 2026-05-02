package gr.georgestaav.membershipservice.functions;

import gr.georgestaav.membershipservice.service.MembershipService;
import gr.georgestaav.membershipservice.web.dto.EmailDeliveredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class MembershipFunctions {

    @Bean
    public Consumer<EmailDeliveredEvent> updateEmailStatus(MembershipService membershipService) {

        return event -> {
            if ("SUCCESS".equals(event.emailStatus())) {
                log.info("Updating Membership status for membership number{}", event.membershipNumber());
                membershipService.updateEmailStatus(event.membershipNumber());
            } else {
                log.warn("Email failed for membership: {}", event.membershipNumber());
            }
        };

    }
}
