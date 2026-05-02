package gr.georgestaav.membershipservice.web.controller;

import gr.georgestaav.membershipservice.constants.MembershipConstants;
import gr.georgestaav.membershipservice.web.dto.MemberDto;
import gr.georgestaav.membershipservice.web.dto.ResponseDto;
import gr.georgestaav.membershipservice.service.MembershipService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
@Validated
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createMembership(@Valid @RequestBody MemberDto memberDto) {

        membershipService.createMembership(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(MembershipConstants.STATUS_201, MembershipConstants.MESSAGE_201));

    }

    @GetMapping("/fetch")
    public ResponseEntity<MemberDto> getMembership(@RequestParam
                                                       @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                       String mobileNumber) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(membershipService.getMembership(mobileNumber));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateMembership(@Valid @RequestBody MemberDto memberDto) {
        boolean isUpdated = membershipService.updateMembership(memberDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(MembershipConstants.STATUS_200, MembershipConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(MembershipConstants.STATUS_417, MembershipConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteMembership(@RequestParam
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        boolean isDeleted = membershipService.deleteMembership(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(MembershipConstants.STATUS_200, MembershipConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(MembershipConstants.STATUS_417, MembershipConstants.MESSAGE_417_DELETE));
        }
    }
}
