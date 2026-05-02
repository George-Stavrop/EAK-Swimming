package gr.georgestaav.subscriptionservice.web.dto;


import java.time.LocalDate;


public record ExpiringSubscriptionEvent(String mobileNumber,
                                        String name,
                                        String email,
                                        LocalDate endDate) {
}
