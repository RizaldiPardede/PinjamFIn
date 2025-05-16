package com.pinjemFin.PinjemFin.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // supaya otomatis return 400
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
