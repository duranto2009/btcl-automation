package validator.annotation;

import java.lang.annotation.Annotation;

import common.RequestFailureException;
import validator.PojoValidator;

public class NonNegativeValidator implements PojoValidator {

	@Override
	public void validate(String fieldName, Object fieldValue, Annotation annotation) {
		if( (Double.compare((double)fieldValue, 0.0) < 0 )) {
			throw new RequestFailureException(fieldName + " must be a non negative number");
		}
	}
}
