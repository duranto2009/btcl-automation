package util;
import java.lang.reflect.Field;
import java.util.*;
public class CollectionUtils {
	public static<T> List<T> intersection(Collection<T> list1,Collection<T> list2){
		
		Set<T> set1 = new HashSet<>(list1);
		List<T> result = new ArrayList<>();
		for(T element: result){
			if(set1.contains(element)){
				result.add(element);
			}
		}
		return result;
	}
	public static<T> List<T> difference(Collection<T> list1,Collection<T> list2){
		Set<T> set1 = new HashSet<>(list1);
		set1.removeAll(list2);
		return new ArrayList<>(set1);
	}
	public static<T> List<T> getProperyListByObjectListAndPropertyName(List<?> objectList,String propertyName,Class<T> classObject) throws Exception{
		if(objectList.isEmpty()){
			return Collections.emptyList();
		}
		List<T> resultList = new ArrayList<>();
		Class<?> classObjectOfObject = objectList.get(0).getClass();
		Field field = classObjectOfObject.getDeclaredField(propertyName);
		field.setAccessible(true);
		
		for(Object object:objectList){
			Object propertyObject = field.get(object);
			if(propertyName.contains(".")){
				String nextPropertyName = ""; // to be calculated
				if(propertyObject instanceof List){
					resultList.addAll(
							getProperyListByObjectListAndPropertyName((List)propertyObject, nextPropertyName, classObject)
							);
				}else{
					resultList.addAll(
							getProperyListByObjectListAndPropertyName(Arrays.asList(propertyObject), nextPropertyName, classObject)
							);
				}
			}else{

				if(propertyObject instanceof List){
					resultList.addAll((Collection<? extends T>) propertyObject);
				}else{
					resultList.add((T) field.get(object));
				}
				
			}
		}
		
		return resultList;
	}
	// map creation method
}
