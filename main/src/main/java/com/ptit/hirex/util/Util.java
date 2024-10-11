package com.ptit.hirex.util;

import com.ptit.hirex.constants.FileConstant;
import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class Util {
    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public String generateFileDirectory(String... arg) {
        return String.join(FileConstant.DIRECTORY_DIVIDE, arg);
    }

//    public String generateRedisKey(String... arg) {
//        return String.join(RedisConstant.SEPARATOR, arg);
//    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public void setCustomValidateMessage(ConstraintValidatorContext constraintValidatorContext, String message) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
