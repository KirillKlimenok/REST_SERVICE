package com.inst.testprogectinst.validation;

import com.inst.testprogectinst.annotation.PasswordMatches;
import com.inst.testprogectinst.payload.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PassMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        SignUpRequest signUpRequest = (SignUpRequest) value;

        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }
}
