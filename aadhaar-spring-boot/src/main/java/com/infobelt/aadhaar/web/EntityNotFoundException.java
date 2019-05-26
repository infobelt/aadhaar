package com.infobelt.aadhaar.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource not found
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource not found, check your URL")
public class EntityNotFoundException extends RuntimeException {

}
