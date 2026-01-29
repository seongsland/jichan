package com.jichan.util.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "PASSWORD", "12345678", "!!@@##$$"})
    void invalidPasswords(String password) {
        assertFalse(passwordValidator.isValid(password, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password123", "Password!@#", "password123!", "PASSWORD123!", "pass1234", "PASS1234", "pass!!@@"})
    void validPasswords(String password) {
        assertTrue(passwordValidator.isValid(password, context));
    }
}
