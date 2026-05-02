package gr.georgestaav.accessservice.service.client;

import gr.georgestaav.accessservice.web.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "subscription-service", fallback = SubscriptionFallback.class)
public interface SubscriptionFeignClient {

    @GetMapping("/api/fetch")
    SubscriptionDto getSubscription(@RequestParam String mobileNumber);
}
