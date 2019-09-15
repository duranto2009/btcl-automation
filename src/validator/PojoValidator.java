package validator;

import java.lang.annotation.Annotation;

public interface PojoValidator {
	public void validate(String fieldName, Object fieldValue, Annotation annotation);
}
