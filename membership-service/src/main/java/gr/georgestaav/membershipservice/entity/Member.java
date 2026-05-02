package gr.georgestaav.membershipservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private String name;

    private String email;

    private String mobileNumber;

    private LocalDate cardioCertExpiry;

    private LocalDate dermaCertExpiry;

}
