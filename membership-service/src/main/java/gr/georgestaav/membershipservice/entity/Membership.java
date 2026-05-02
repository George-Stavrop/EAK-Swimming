package gr.georgestaav.membershipservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Membership extends BaseEntity{

    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "membership_number")
    private Long membershipNumber;

    private String membershipType;

    private String facilityName;

    private Boolean emailSent;

}
