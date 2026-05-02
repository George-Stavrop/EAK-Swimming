package gr.georgestaav.membershipservice.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberDetailsDto {
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "Name should be 5-30 characters")
    private String name;

    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Should be valid Email")
    private String email;

    @NotEmpty(message = "Mobile number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private LocalDate cardioCertExpiry;

    private LocalDate dermaCertExpiry;

    private MembershipDto membershipDto;

    private SubscriptionDto subscriptionDto;

    private AccessCardDto accessCardDto;
}
