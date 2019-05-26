package com.infobelt.aadhaar.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when we have an issue with authorization
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "You are not authorized to perform this action")
public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String message) {
        super(message);
    }

}
