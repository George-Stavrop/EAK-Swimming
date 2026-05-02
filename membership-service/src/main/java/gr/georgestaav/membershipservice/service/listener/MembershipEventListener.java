package gr.georgestaav.membershipservice.service.listener;

import gr.georgestaav.membershipservice.web.dto.MembershipCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipEventListener {

    private final StreamBridge streamBridge;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMembershipCreated(MembershipCreatedEvent event) {
        log.info("Sending New Membership Event for :{}", event);
        var result = streamBridge.send("membershipCreated-out-0", event);
        log.info("Is the communication request successfully processed?:{}", result);
    }
}
