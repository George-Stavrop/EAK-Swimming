package gr.georgestaav.membershipservice.service;

import gr.georgestaav.membershipservice.web.dto.MemberDto;

public interface MembershipService {

    void createMembership(MemberDto memberDto);

    MemberDto getMembership(String mobileNumber);

    boolean updateMembership(MemberDto memberDto);

    boolean deleteMembership(String mobileNumber);

    void updateEmailStatus(Long membershipNumber);
}
