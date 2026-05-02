package gr.georgestaav.subscriptionservice.service.client;

import gr.georgestaav.subscriptionservice.web.dto.MemberContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "membership-service", fallback = MembershipClientFallback.class)
public interface MembershipFeignClient {

    @GetMapping("/api/fetch")
    MemberContactDto getMembership(@RequestParam String mobileNumber);
}
