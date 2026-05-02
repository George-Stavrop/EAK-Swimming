package gr.georgestaav.accessservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AccessCardDto {

    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    private String mobileNumber;

    @NotEmpty(message = "Card Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "Access Card Number must be 12 digits")
    private String accessCardNumber;

    @NotEmpty(message = "CardType can not be a null or empty")
    private String accessCardType;

    private Boolean isActive;
}
