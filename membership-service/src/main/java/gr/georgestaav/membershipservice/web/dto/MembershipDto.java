package gr.georgestaav.membershipservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class MembershipDto {

    private Long membershipNumber;
    private String membershipType;
    private String facilityName;
}
