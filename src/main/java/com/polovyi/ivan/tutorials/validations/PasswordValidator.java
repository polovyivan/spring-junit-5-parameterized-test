package com.polovyi.ivan.tutorials.validations;

import com.polovyi.ivan.tutorials.enm.PaymentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoCashPaymentTypeValidator implements ConstraintValidator<NoCashPaymentType, PaymentType> {

    @Override
    public boolean isValid(PaymentType paymentType, ConstraintValidatorContext context) {
        return !PaymentType.CASH.equals(paymentType);
    }

}