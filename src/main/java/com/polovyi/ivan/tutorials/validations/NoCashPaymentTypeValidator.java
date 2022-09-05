package com.portoseg.validations.constraints;

import com.portoseg.enumeration.ProposalDecisionType;
import com.portoseg.validations.annotations.NotDerivationStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotDerivationStatusValidator implements ConstraintValidator<NotDerivationStatus, ProposalDecisionType> {

    @Override
    public boolean isValid(ProposalDecisionType proposalDecisionType, ConstraintValidatorContext context) {
        return !ProposalDecisionType.DERIVACAO.equals(proposalDecisionType);
    }

}