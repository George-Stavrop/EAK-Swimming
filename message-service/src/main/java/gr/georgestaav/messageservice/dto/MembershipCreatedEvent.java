package gr.georgestaav.messageservice.dto;

public record MembershipCreatedEvent(Long membershipNumber, String name,
                                     String email, String mobileNumber) {
}
