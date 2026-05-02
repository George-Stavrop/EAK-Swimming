package gr.georgestaav.subscriptionservice.web.controller;

import gr.georgestaav.subscriptionservice.constants.SubscriptionConstants;
import gr.georgestaav.subscriptionservice.service.SubscriptionService;
import gr.georgestaav.subscriptionservice.web.dto.ResponseDto;
import gr.georgestaav.subscriptionservice.web.dto.SubscriptionDto;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createSubscription(@RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        subscriptionService.createSubscription(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(SubscriptionConstants.STATUS_201, SubscriptionConstants.MESSAGE_201));

    }

    @GetMapping("/fetch")
    public ResponseEntity<SubscriptionDto> getSubscription(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        log.info("Fetching subscription for {}", mobileNumber);
        SubscriptionDto loansDto = subscriptionService.getSubscription(mobileNumber);
        log.debug("Fetched Subscription");
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> renewSubscription(@RequestParam
                                                             @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                             String mobileNumber) {
        boolean isUpdated = subscriptionService.renewSubscription(mobileNumber);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(SubscriptionConstants.STATUS_200, SubscriptionConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(SubscriptionConstants.STATUS_417, SubscriptionConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteSubscription(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        boolean isDeleted = subscriptionService.deleteSubscription(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(SubscriptionConstants.STATUS_200, SubscriptionConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(SubscriptionConstants.STATUS_417, SubscriptionConstants.MESSAGE_417_DELETE));
        }
    }
}
