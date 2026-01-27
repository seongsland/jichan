package com.jichan.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
            return true; // @NotBlank에서 처리
        }

        context.disableDefaultConstraintViolation();

        // 1. 허용 문자 검사 (한글, 영문, 숫자)
        if (!name.matches("^[a-zA-Z0-9가-힣]*$")) {
            context.buildConstraintViolationWithTemplate("이름은 한글, 영문, 숫자만 사용할 수 있습니다.").addConstraintViolation();
            return false;
        }

        // 2. 숫자만 있는지 검사
        if (name.matches("^[0-9]+$")) {
            context.buildConstraintViolationWithTemplate("이름을 숫자로만 구성할 수 없습니다.").addConstraintViolation();
            return false;
        }

        boolean hasKorean = name.matches(".*[가-힣].*");
        boolean hasEnglish = name.matches(".*[a-zA-Z].*");
        int len = name.length();

        // 3. 조합 및 길이 검사
        if (hasKorean && hasEnglish) { // 한글 + 영문 조합
            if (len < 3 || len > 8) {
                context.buildConstraintViolationWithTemplate("이름(한글+영문 조합)은 3자 이상 8자 이하여야 합니다.")
                       .addConstraintViolation();
                return false;
            }
        } else if (hasKorean) { // 한글 또는 한글+숫자 조합
            if (len < 2 || len > 8) {
                context.buildConstraintViolationWithTemplate("한글 이름은 2자 이상 8자 이하여야 합니다.").addConstraintViolation();
                return false;
            }
        } else if (hasEnglish) { // 영문 또는 영문+숫자 조합
            if (len < 3 || len > 13) {
                context.buildConstraintViolationWithTemplate("영문 이름은 3자 이상 13자 이하여야 합니다.").addConstraintViolation();
                return false;
            }
        } else {
            // 이 경우는 숫자만 있는 경우인데, 위에서 이미 필터링됨
            context.buildConstraintViolationWithTemplate("유효하지 않은 이름 형식입니다.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
