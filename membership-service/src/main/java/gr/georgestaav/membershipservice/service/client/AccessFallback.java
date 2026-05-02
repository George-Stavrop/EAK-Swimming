package gr.georgestaav.membershipservice.service.client;

import gr.georgestaav.membershipservice.web.dto.AccessCardDto;
import gr.georgestaav.membershipservice.web.dto.SubscriptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccessFallback implements AccessFeignClient{

    @Override
    public AccessCardDto getAccessCard(String mobileNumber) {
        AccessCardDto fallback = new AccessCardDto();
        fallback.setMobileNumber(mobileNumber);
        fallback.setAccessCardType("UNAVAILABLE");
        return fallback;
    }
}
