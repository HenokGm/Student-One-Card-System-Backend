package com.socs.oneCardIDManagement;

public class StudentIDAlreadyExistsException extends RuntimeException {
    public StudentIDAlreadyExistsException(String message) {
        super(message);
    }
}
