package com.jichan.util.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameValidatorTest {

    private NameValidator nameValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        nameValidator = new NameValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito.mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
    }

    @Test
    void nullOrEmptyIsValid() {
        assertTrue(nameValidator.isValid(null, context));
        assertTrue(nameValidator.isValid("", context));
        assertTrue(nameValidator.isValid(" ", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"!@#", "test!", "테스트_"})
    void invalidCharacters(String name) {
        assertFalse(nameValidator.isValid(name, context));
    }

    @Test
    void numbersOnlyIsInvalid() {
        assertFalse(nameValidator.isValid("12345", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"한", "한글이름이너무길어요"})
        // 1, 9
    void invalidKoreanNameLength(String name) {
        assertFalse(nameValidator.isValid(name, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "englishnameistoolong"})
        // 2, 20
    void invalidEnglishNameLength(String name) {
        assertFalse(nameValidator.isValid(name, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"한a", "한글english너무길어"})
        // 2, 11
    void invalidKoreanEnglishNameLength(String name) {
        assertFalse(nameValidator.isValid(name, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"한글", "한글이름", "한글123"})
    void validKoreanNames(String name) {
        assertTrue(nameValidator.isValid(name, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "englishname", "english123"})
    void validEnglishNames(String name) {
        assertTrue(nameValidator.isValid(name, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"한글a", "한글eng", "한글eng123"})
    void validKoreanEnglishNames(String name) {
        assertTrue(nameValidator.isValid(name, context));
    }
}
