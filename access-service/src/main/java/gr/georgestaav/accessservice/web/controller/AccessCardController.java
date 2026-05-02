package gr.georgestaav.accessservice.web.controller;

import gr.georgestaav.accessservice.constants.AccessConstants;
import gr.georgestaav.accessservice.service.AccessCardService;
import gr.georgestaav.accessservice.web.dto.AccessCardDto;
import gr.georgestaav.accessservice.web.dto.AccessCheckDto;
import gr.georgestaav.accessservice.web.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccessCardController {

    private final AccessCardService accessCardService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccessCard(@Valid @RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        accessCardService.createAccessCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccessConstants.STATUS_201, AccessConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<AccessCardDto> getAccessCard(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        log.info("Fetching Access Card for {}", mobileNumber);
        AccessCardDto cardsDto = accessCardService.getAccessCard(mobileNumber);
        log.info("Access Card fetched");
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @PutMapping("/toggle")
    public ResponseEntity<ResponseDto> toggleAccessCard(@RequestParam
                                                            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        boolean isUpdated = accessCardService.toggleAccessCard(mobileNumber);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccessConstants.STATUS_200, AccessConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccessConstants.STATUS_417, AccessConstants.MESSAGE_417_UPDATE));
        }
    }

    @GetMapping("/can-enter/{accessCardNumber}")
    public ResponseEntity<AccessCheckDto> canEnter(
            @PathVariable
            @Pattern(regexp = "(^$|[0-9]{12})", message = "Access card number must be 12 digits")
            String accessCardNumber) {

        return ResponseEntity.ok(accessCardService.canEnter(accessCardNumber));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccessCardDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        boolean isDeleted = accessCardService.deleteAccessCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccessConstants.STATUS_200, AccessConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccessConstants.STATUS_417, AccessConstants.MESSAGE_417_DELETE));
        }
    }


}

