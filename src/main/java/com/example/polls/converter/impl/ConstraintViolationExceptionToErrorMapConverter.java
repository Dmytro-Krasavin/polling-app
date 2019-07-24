package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConstraintViolationExceptionToErrorMapConverter implements ModelConverter<ConstraintViolationException, Map<String, String>> {

    @Override
    public Map<String, String> convert(ConstraintViolationException ex) {
        Assert.notNull(ex, "ConstraintViolationException must not be null!");
        return ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
    }
}
