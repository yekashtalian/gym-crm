package org.example.gymcrm.exception;

public class UserServiceException extends RuntimeException{
    public UserServiceException(String message) {
        super(message);
    }
}
