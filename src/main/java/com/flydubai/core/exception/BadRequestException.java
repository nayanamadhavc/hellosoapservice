package com.flydubai.core.exception;

import javax.xml.ws.WebFault;

@WebFault(name = "BadRequestException")
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
