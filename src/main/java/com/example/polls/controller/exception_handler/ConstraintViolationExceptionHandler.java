package com.example.polls.controller.exception_handler;

import com.example.polls.util.converter.impl.ConstraintViolationExceptionToErrorMapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class ConstraintViolationExceptionHandler {

    private final ConstraintViolationExceptionToErrorMapConverter errorMapConverter;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handle(ConstraintViolationException ex) {
        Map<String, String> errorMap = errorMapConverter.convert(ex);
        return new ResponseEntity<>(errorMap, HttpStatus.CONFLICT);
    }
}
