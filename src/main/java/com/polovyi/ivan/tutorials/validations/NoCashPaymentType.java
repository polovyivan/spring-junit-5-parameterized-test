package com.portoseg.validations.annotations;

import com.portoseg.validations.constraints.NotDerivationStatusValidator;

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
@Constraint(validatedBy = NotDerivationStatusValidator.class)
public @interface NotDerivationStatus {

    String message() default "400.001";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}