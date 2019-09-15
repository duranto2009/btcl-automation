package util;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import annotation.DAO;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import requestMapping.Service;

public class ServiceDAOFactory {
	private static Map<Class<?>,Object> mapOfServiceToClass = new ConcurrentHashMap<>();
	private static Map<Class<?>,Object> mapOfDAOToClass = new ConcurrentHashMap<>();
	private static MethodHandler transactionalMethodHandler = new TransactionalMethodHandler();
	private static MethodFilter methodFilter = new NonTransactionMethodFilter(); 
	private static ProxyFactory proxyFactory = new ProxyFactory();

	public synchronized static<T> T getService(Class<T> classObject){
		if(!mapOfServiceToClass.containsKey(classObject)){
			proxyFactory.setFilter(methodFilter);
			proxyFactory.setSuperclass(classObject);

			try{
				Object object = proxyFactory.create(new Class[0], new Object[0],transactionalMethodHandler);
				mapOfServiceToClass.put(classObject, object);

				for(Field field:classObject.getDeclaredFields()){
					field.setAccessible(true);
					if(field.getDeclaredAnnotation(Service.class)!=null){
						field.set(object, getService(field.getType()));
					}else if(field.getDeclaredAnnotation(DAO.class)!=null){
						field.set(object, getDAO(field.getType()));
					}
				}

			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return  classObject.cast(mapOfServiceToClass.get(classObject));
	}
	
	public static <T> T getDAO(Class<T> classObject) throws Exception{
		if(!mapOfDAOToClass.containsKey(classObject)){
			synchronized (mapOfDAOToClass) {
				if(!mapOfDAOToClass.containsKey(classObject)){
					Object object = classObject.newInstance();
					mapOfDAOToClass.put(classObject, object);
					for(Field field: classObject.getDeclaredFields()){
						field.setAccessible(true);
						if(field.getAnnotation(DAO.class)!=null){
							field.set(object, getDAO(field.getType()));
						}
					}
				}
			}
		}
		return classObject.cast(mapOfDAOToClass.get(classObject));
	}
	

}
