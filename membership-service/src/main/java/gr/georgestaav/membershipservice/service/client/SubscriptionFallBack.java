package gr.georgestaav.membershipservice.service.client;

import gr.georgestaav.membershipservice.web.dto.SubscriptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionFallBack implements SubscriptionFeignClient{
    @Override
    public SubscriptionDto getSubscription(String mobileNumber) {
        SubscriptionDto fallback = new SubscriptionDto();
        fallback.setMobileNumber(mobileNumber);
        fallback.setSubscriptionType("UNAVAILABLE");
        fallback.setActive(false);
        return fallback;
    }
}
