package util;
import java.util.*;

public class PrimitiveClassChecker {
	static private Class[] primitiveClasses = {Integer.class,Integer.TYPE,Double.class,Double.TYPE,Boolean.TYPE,
			Boolean.class,Float.class,Float.TYPE,Short.class,Short.TYPE,Long.class,Long.TYPE};
	static private Set<Class> primitiveClassSet = new HashSet<>();
	static{
		for(Class classObject: primitiveClasses){
			primitiveClassSet.add(classObject);
		}
		
	}
}
