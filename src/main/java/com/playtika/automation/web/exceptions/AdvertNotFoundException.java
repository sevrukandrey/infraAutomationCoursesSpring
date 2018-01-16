package com.playtika.automation.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AdvertNotFoundException extends RuntimeException {
    public AdvertNotFoundException(String message) {
        super(message);
    }
}
