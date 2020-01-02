package com.example.polls.validation;

import com.example.polls.model.User;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User> {

    private final UserService userService;

    private String message;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean emailIsNotUnique = userService.findByEmail(user.getEmail())
                .map(User::getId)
                .filter(userId -> !userId.equals(user.getId()))
                .isPresent();

        if (emailIsNotUnique) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
