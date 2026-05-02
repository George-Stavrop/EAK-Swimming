package gr.georgestaav.membershipservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AccessCardDto {

    private String mobileNumber;
    private String accessCardNumber;
    private String accessCardType;
    Boolean isActive;
}
