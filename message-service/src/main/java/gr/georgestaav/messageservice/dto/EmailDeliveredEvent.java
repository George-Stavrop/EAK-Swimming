package gr.georgestaav.messageservice.dto;

import java.time.LocalDateTime;

public record EmailDeliveredEvent(Long membershipNumber,
                                   String emailStatus, LocalDateTime localDateTime) {
}
