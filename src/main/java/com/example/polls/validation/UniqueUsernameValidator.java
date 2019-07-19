package com.example.polls.validation;

import com.example.polls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext cxt) {
        return !userService.existsByUsername(userName);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
