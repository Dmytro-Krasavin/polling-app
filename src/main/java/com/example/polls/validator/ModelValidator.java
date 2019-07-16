package com.example.polls.validator;

import com.example.polls.exception.model.ModelValidationException;

public interface ModelValidator<T> {

    void validate(T model) throws ModelValidationException;

}
