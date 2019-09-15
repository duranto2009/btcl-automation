package util;

import java.lang.reflect.*;

import javax.servlet.http.HttpServletRequest;


public class ClassConvertor {
	static private Object convert(String str, Class<?> typeClass){
		if(typeClass == Integer.class){
			return Integer.parseInt(str);
		}else if(typeClass == Long.class){
			return Long.parseLong(str);
		}else if(typeClass == Double.class){
			return Double.class;
		}else if(typeClass == Float.class){
			return Float.parseFloat(str);
		}else{
			return str;
		}
		
	}
	static public void populateFromRequest(HttpServletRequest request, Object object){
		if(request==null || object == null){
			return;
		}
		Class objectClass = object.getClass();
		Field[] fileds = objectClass.getFields();
		for(Field field: fileds){
			Class typeClass = field.getType();
			String propertyName = field.getName();
			try{
				Object value = convert(request.getParameter(propertyName), typeClass);
				field.set(object, value);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	static public void populateFormObject(Object form,Object dto){
		if(form==null||dto==null){
			return;
		}
		Field[] fields = dto.getClass().getFields();
		for(Field dtoField: fields){
			Field formField;
			try {
				formField = form.getClass().getField(dtoField.getName());
				Object dtoValue = dtoField.get(dto);
				formField.set(form, dtoValue.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
