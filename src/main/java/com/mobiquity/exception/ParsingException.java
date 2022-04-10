package com.mobiquity.exception;

public class ParsingException extends RuntimeException {

    public ParsingException(String message, Exception e) {
        super(message, e);
    }

    public ParsingException(String message) {
        super(message);
    }

}
