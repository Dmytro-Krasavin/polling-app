package com.example.polls.validator;

import com.example.polls.exception.model.validation.ModelValidationException;

public interface ModelValidator<T> {

    void validate(T model) throws ModelValidationException;

}
