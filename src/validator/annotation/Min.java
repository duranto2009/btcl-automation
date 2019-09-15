package validator.annotation;
import java.lang.annotation.*;
import validator.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatorClass = MinValidator.class)
public @interface Min {
	double value() default 0.0;
	String errorMsg() default "%s can not be less than %d";
	String fieldName();
}
