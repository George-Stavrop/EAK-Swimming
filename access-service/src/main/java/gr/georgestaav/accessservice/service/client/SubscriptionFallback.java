package gr.georgestaav.accessservice.service.client;

import gr.georgestaav.accessservice.web.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriptionFallback implements SubscriptionFeignClient{
    @Override
    public SubscriptionDto getSubscription(String mobileNumber) {
       return null;
    }
}
