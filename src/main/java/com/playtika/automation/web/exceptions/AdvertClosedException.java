package com.playtika.automation.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AdvertClosedException extends RuntimeException {
    public AdvertClosedException(String message) {
        super(message);
    }
}
