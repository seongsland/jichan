package com.jichan.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return false; // null이나 공백은 @NotBlank에서 처리하지만, 안전을 위해 추가
        }

        int typesCount = 0;
        if (password.matches(".*[A-Z].*")) {
            typesCount++;
        }
        if (password.matches(".*[a-z].*")) {
            typesCount++;
        }
        if (password.matches(".*[0-9].*")) {
            typesCount++;
        }
        if (password.matches(".*[ !@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            typesCount++;
        }

        return typesCount >= 2;
    }
}
