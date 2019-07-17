package com.example.polls.exception.model.validation;

public abstract class ModelValidationException extends Exception {

    public ModelValidationException(String message) {
        super(message);
    }
}
