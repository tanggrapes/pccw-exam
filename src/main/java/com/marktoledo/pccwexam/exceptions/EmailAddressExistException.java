package com.marktoledo.pccwexam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAddressExistException extends RuntimeException {

    public EmailAddressExistException(String message) {
        super(message);
    }

}