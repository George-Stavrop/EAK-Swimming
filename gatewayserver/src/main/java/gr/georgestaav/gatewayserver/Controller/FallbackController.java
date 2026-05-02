package gr.georgestaav.gatewayserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


    @RestController
    @RequestMapping("/fallback")
    public class FallbackController {

        @GetMapping("/membership")
        public Mono<ResponseEntity<String>> membershipFallback() {
            return Mono.just(ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Membership Service is currently unavailable. Please try again later."));
        }

        @GetMapping("/subscription")
        public Mono<ResponseEntity<String>> subscriptionFallback() {
            return Mono.just(ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Subscription Service is currently unavailable. Please try again later."));
        }

        @GetMapping("/access")
        public Mono<ResponseEntity<String>> accessFallback() {
            return Mono.just(ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Access Service is currently unavailable. Please try again later."));
        }
    }

