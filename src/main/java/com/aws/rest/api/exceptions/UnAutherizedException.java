package com.aws.rest.api.exceptions;

public class UnAutherizedException extends Exception {
    public UnAutherizedException(final String message,Exception e){
        super(message, e);
    }
}
