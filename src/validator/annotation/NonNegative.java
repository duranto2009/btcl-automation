package validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import validator.Constraint;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatorClass=NonNegativeValidator.class)
public @interface NonNegative{
	String value() default "";
}
