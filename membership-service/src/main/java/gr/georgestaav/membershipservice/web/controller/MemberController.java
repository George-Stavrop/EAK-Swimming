package gr.georgestaav.membershipservice.web.controller;

import gr.georgestaav.membershipservice.service.MemberService;
import gr.georgestaav.membershipservice.web.dto.MemberDetailsDto;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<MemberDetailsDto> getCustomerDetails(@RequestParam
                                                               @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                   String mobileNumber) {
        log.debug("fetchCustomerDetails method start");
        MemberDetailsDto memberDetailsDto = memberService.getMemberDetails(mobileNumber);
        log.debug("fetchCustomerDetails method end");
        return ResponseEntity.status(HttpStatus.OK).body(memberDetailsDto);
    }
}
