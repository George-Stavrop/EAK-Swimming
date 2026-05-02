package gr.georgestaav.membershipservice.service.client;

import gr.georgestaav.membershipservice.web.dto.AccessCardDto;
import gr.georgestaav.membershipservice.web.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "subscription-service", fallback = SubscriptionFallBack.class)
public interface SubscriptionFeignClient {

    @GetMapping(value = "/api/fetch")
    SubscriptionDto getSubscription(@RequestParam String mobileNumber);

}