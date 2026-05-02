package gr.georgestaav.accessservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccessCard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_card_id")
    private Long accessCardId;

    private String mobileNumber;

    private String accessCardNumber;

    private String accessCardType;

    private Boolean isActive;
}
