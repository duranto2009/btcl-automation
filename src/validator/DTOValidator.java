package validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class DTOValidator {
	
	public static void validate(Object object) throws Exception{
		validate(object,object.getClass());
	}
	public static void validate(Object object,Class<? extends Object> classObject) throws Exception{
		for(Field field : classObject.getDeclaredFields()){
			field.setAccessible(true);
			for( Annotation annotation : field.getDeclaredAnnotations()){
				Constraint constraint = (Constraint)annotation.annotationType().getDeclaredAnnotation(Constraint.class);
				if(constraint!=null){
					PojoValidator pojoValidator = (constraint.validatorClass().newInstance());
					pojoValidator.validate(field.getName(), field.get(object), annotation);
				}
			}
		}
	}
}
