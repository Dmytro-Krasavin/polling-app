package com.example.polls.exception.model;

public abstract class ModelValidationException extends Exception {

    public ModelValidationException(String message) {
        super(message);
    }
}
