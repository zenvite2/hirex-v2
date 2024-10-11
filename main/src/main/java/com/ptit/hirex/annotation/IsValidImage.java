package com.ptit.hirex.annotation;

import com.ptit.hirex.validator.ImageFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageFileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidImage {
    String message() default "{validation.invalid.file.size}";

    long maxSize();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
