package com.shashankbhat.splitbill.exception;

public class KnownException extends Exception {

    private final String errorMessage;

    public KnownException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}