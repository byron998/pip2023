package com.ibm.webdev.app.jersey.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.web.servlet.tags.Param;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckCaseValidator.class)
@Documented
public @interface NonEmptyNames {
	String message() default "{UserDefined NonEmptyNames}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}

class CheckCaseValidator implements ConstraintValidator<NonEmptyNames, Param> {
 
	public void initialize(NonEmptyNames constraintAnnotation) {
	}
	public boolean isValid(Param value, ConstraintValidatorContext context) {
		return false;
	}
}