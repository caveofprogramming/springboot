package com.caveofprogramming.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.caveofprogramming.model.SiteUser;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SiteUser> {

	@Override
	public void initialize(PasswordMatch p) {
		
	}

	@Override
	public boolean isValid(SiteUser arg0, ConstraintValidatorContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
