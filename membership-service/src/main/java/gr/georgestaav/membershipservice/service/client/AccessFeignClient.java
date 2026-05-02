package gr.georgestaav.membershipservice.service.client;

import gr.georgestaav.membershipservice.web.dto.AccessCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "access-service", fallback = AccessFallback.class)
public interface AccessFeignClient {

    @GetMapping(value = "/api/fetch")
    AccessCardDto getAccessCard(@RequestParam String mobileNumber);

}