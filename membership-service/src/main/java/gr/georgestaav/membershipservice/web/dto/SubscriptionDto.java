package gr.georgestaav.membershipservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SubscriptionDto {

    private String mobileNumber;
    private String subscriptionNumber;
    private String subscriptionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amountPaid;
    private boolean active;
}
