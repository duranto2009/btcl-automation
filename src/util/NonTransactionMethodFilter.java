package util;

import java.lang.reflect.Method;

import annotation.Transactional;
import javassist.util.proxy.MethodFilter;

public class NonTransactionMethodFilter implements MethodFilter {

	@Override
	public boolean isHandled(Method paramMethod) {
		return paramMethod.getAnnotation(Transactional.class)!=null;
	}

}
