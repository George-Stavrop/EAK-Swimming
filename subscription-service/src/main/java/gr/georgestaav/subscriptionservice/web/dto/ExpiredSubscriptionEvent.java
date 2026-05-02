package gr.georgestaav.subscriptionservice.web.dto;

import lombok.Data;

import java.time.LocalDate;


public record ExpiredSubscriptionEvent(String mobileNumber,
                                        String name,
                                        String email,
                                        LocalDate endDate) {
}
