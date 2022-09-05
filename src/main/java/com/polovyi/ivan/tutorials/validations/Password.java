package com.polovyi.ivan.tutorials.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoCashPaymentTypeValidator.class)
public @interface NoCashPaymentType {

    String message() default "Error!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}