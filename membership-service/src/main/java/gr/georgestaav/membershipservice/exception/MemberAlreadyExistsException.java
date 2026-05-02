package gr.georgestaav.membershipservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MemberAlreadyExistsException extends RuntimeException{

    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
