package gr.georgestaav.subscriptionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    private String mobileNumber;

    private String subscriptionNumber;

    private String subscriptionType;

    private BigDecimal amountPaid;

    private LocalDate startDate;

    private LocalDate endDate;

    @Transient
    public boolean isActive() {
        return endDate != null && !LocalDate.now().isAfter(endDate);
    }
}
