package gr.georgestaav.subscriptionservice.service.client;

import gr.georgestaav.subscriptionservice.web.dto.MemberContactDto;
import org.springframework.stereotype.Component;

@Component
public class MembershipClientFallback implements MembershipFeignClient{
    @Override
    public MemberContactDto getMembership(String mobileNumber) {
        return new MemberContactDto("Unknown", "N/A");
    }
}
