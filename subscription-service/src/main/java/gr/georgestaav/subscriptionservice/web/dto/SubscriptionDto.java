package gr.georgestaav.subscriptionservice.web.dto;

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
