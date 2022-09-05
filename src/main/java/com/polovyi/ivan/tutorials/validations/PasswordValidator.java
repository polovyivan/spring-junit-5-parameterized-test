package com.polovyi.ivan.tutorials.validations;

import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, CreateCustomerRequest> {

    @Override
    public boolean isValid(CreateCustomerRequest createCustomerRequest, ConstraintValidatorContext context) {
        return !StringUtils.isAnyBlank(createCustomerRequest.getPassword(),
                createCustomerRequest.getPasswordConfirmation()) &&
                createCustomerRequest.getPassword().equals(createCustomerRequest.getPasswordConfirmation());
    }

}