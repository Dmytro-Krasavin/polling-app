package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import com.example.polls.exception.model.validation.ModelValidationException;
import com.example.polls.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidationExceptionToResponseEntityConverter implements ModelConverter<ModelValidationException, ResponseEntity> {

    @Override
    public ResponseEntity convert(ModelValidationException exception) {
        String message = exception.getMessage();
        ApiResponse responseBody = new ApiResponse(false, message);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
