package validator.annotation;

import java.lang.annotation.Annotation;

import common.RequestFailureException;
import validator.PojoValidator;


public class MinValidator implements PojoValidator{

	@Override
	public void validate(String fieldName, Object fieldValue, Annotation annotation) {
		Min min = (Min)annotation;
		double value = (double)fieldValue;
		if(Double.compare(value, min.value()) < 0 ) {
			throw new RequestFailureException(String.format(min.errorMsg(),min.fieldName(), min.value()));
		}
	}
}
