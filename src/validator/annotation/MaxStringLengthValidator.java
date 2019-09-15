package validator.annotation;

import java.lang.annotation.Annotation;

import common.RequestFailureException;
import validator.PojoValidator;

public class MaxStringLengthValidator implements PojoValidator{

	@Override
	public void validate(String fieldName, Object fieldValue, Annotation annotation) {
		MaxStringLength maxStringLength = (MaxStringLength)annotation;
		String stringValue = (String)fieldValue;
		if(stringValue.length() > maxStringLength.value()){
			throw new RequestFailureException(maxStringLength.errorMsg() + " " + maxStringLength.value());
		}
	}

}
