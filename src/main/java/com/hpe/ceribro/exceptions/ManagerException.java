package com.hpe.ceribro.exceptions;

public class ManagerException extends RuntimeException{

    public ManagerException(String message) {

        super(message);
    }

    public ManagerException(String message, Throwable thrown) {

        super(message, thrown);
    }
}
