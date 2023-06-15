package com.marktoledo.pccwexam.annotations;

import com.marktoledo.pccwexam.validators.UUIDValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = UUIDValidator.class)
public @interface ValidUUID {

    String message() default "Invalid UUID(s)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
