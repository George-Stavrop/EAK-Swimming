package gr.georgestaav.membershipservice.web.dto;

import java.time.LocalDateTime;

public record EmailDeliveredEvent(Long membershipNumber,
                                  String emailStatus, LocalDateTime localDateTime) {
}
