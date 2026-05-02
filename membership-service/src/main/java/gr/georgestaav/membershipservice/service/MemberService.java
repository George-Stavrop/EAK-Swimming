package gr.georgestaav.membershipservice.service;

import gr.georgestaav.membershipservice.web.dto.MemberDetailsDto;

public interface MemberService {
    MemberDetailsDto getMemberDetails(String mobileNumber);
}
