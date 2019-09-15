package validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import validator.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatorClass = MaxStringLengthValidator.class)
public @interface MaxStringLength {
	int value();
	String errorMsg() default "the length of the string can not be greater than";
}
