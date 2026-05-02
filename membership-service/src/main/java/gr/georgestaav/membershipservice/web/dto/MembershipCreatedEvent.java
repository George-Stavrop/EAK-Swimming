package gr.georgestaav.membershipservice.web.dto;

public record MembershipCreatedEvent(Long membershipNumber, String name,
                                     String email, String mobileNumber) {
}
