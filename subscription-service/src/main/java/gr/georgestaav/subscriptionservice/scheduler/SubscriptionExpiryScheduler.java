package gr.georgestaav.subscriptionservice.scheduler;

import gr.georgestaav.subscriptionservice.entity.Subscription;
import gr.georgestaav.subscriptionservice.repository.SubscriptionRepository;
import gr.georgestaav.subscriptionservice.service.client.MembershipFeignClient;
import gr.georgestaav.subscriptionservice.web.dto.ExpiredSubscriptionEvent;
import gr.georgestaav.subscriptionservice.web.dto.ExpiringSubscriptionEvent;
import gr.georgestaav.subscriptionservice.web.dto.MemberContactDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiryScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final MembershipFeignClient membershipFeignClient;
    private final StreamBridge streamBridge;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpiryWarnings() {
        LocalDate warningDate = LocalDate.now().plusDays(5);
        List<Subscription> expiring = subscriptionRepository.findByEndDate(warningDate);

        expiring.forEach(subscription -> {

            MemberContactDto memberContactDto = membershipFeignClient
                    .getMembership(subscription.getMobileNumber());

            ExpiringSubscriptionEvent event = new ExpiringSubscriptionEvent(
                    subscription.getMobileNumber(),
                    memberContactDto.name(),
                    memberContactDto.email(),
                    subscription.getEndDate()
            );

            log.info("Sending expiry warning for mobile: {}", subscription.getMobileNumber());
            streamBridge.send("expiringSubscription-out-0", event);
        });
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpiredMessage() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Subscription> expired = subscriptionRepository.findByEndDate(yesterday);

        expired.forEach(subscription -> {

            MemberContactDto memberContactDto = membershipFeignClient
                    .getMembership(subscription.getMobileNumber());

            ExpiredSubscriptionEvent event = new ExpiredSubscriptionEvent(
                    subscription.getMobileNumber(),
                    memberContactDto.name(),
                    memberContactDto.email(),
                    subscription.getEndDate()
            );

            log.info("Sending expired message for mobile: {}", subscription.getMobileNumber());
            streamBridge.send("expiredSubscription-out-0", event);
        });
    }
}
