package com.example.polls.validation;

import com.example.polls.model.User;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, User> {

    private final UserService userService;

    private String message;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean usernameIsNotUnique = userService.findByUsername(user.getUsername())
                .map(User::getId)
                .filter(userId -> !userId.equals(user.getId()))
                .isPresent();

        if (usernameIsNotUnique) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
