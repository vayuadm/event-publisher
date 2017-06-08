package com.hpe.ceribro.exceptions;

public class SearchException extends RuntimeException {

    public SearchException(String message) {

        super(message);
    }

    public SearchException(String message, Throwable thrown) {

        super(message, thrown);
    }
}
