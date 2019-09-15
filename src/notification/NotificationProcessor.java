package notification;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import report.ColumnConvertor;
import report.Display;
public class NotificationProcessor {
	static Logger logger = Logger.getLogger(NotificationProcessor.class);
	static private final String TEMPLATE_PATTERN = "[{](.*?)[}]";
	static private final Pattern pattern = Pattern.compile(TEMPLATE_PATTERN);
	
	public static String getNotification(String templateString,Object... notificationDTOs) throws Exception{
		
		Map<String,Object> nameValueMap = new HashMap<String, Object>();
		for(Object notificationDTO: notificationDTOs){
			populateNameValueMap(nameValueMap,notificationDTO,notificationDTO.getClass());
		}
		String notificiationString = getNotification(templateString, nameValueMap);
		return notificiationString;
	}
	public static void populateNameValueMap(Map<String,Object> nameValueMap, Object object,Class classObject){
		if(classObject.equals(Object.class)){
			return;
		}
		nameValueMap.putAll(  getNameValueMap(object, classObject) );
		populateNameValueMap(nameValueMap, object, classObject.getSuperclass());
	}
	public static Map<String,Object> getNameValueMap(Object notificationDTO,Class<?> classObject) {
		Map<String,Object> nameValueMap = new HashMap<String, Object>();
		try{
			//Class classObject = notificationDTO.getClass();
			for(Field field:classObject.getDeclaredFields()){
				field.setAccessible(true);
				Object value;
				if(field.getAnnotation(Display.class)!=null){
					Display display = (Display)field.getAnnotation(Display.class);
					value = display.value().newInstance().convert(field.get(notificationDTO));
				}else{
					value = field.get(notificationDTO);
				}
				
				nameValueMap.put(field.getName(), value);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		logger.debug("nameValueMap: "+nameValueMap);
		return nameValueMap;
	}
	
	
	
	
	public static String getNotification(String templateString,Map<String,Object> nameValueMap) throws Exception{
		if(templateString==null||templateString.trim().length()==0){
			return "";
		}
		Matcher matcher = pattern.matcher(templateString);
		
		StringBuffer stringBuffer = new StringBuffer();
		
		
		while(matcher.find()){
			matcher.appendReplacement(stringBuffer, String.valueOf( nameValueMap.getOrDefault(matcher.group(1), matcher.group())));
		}
		if(stringBuffer.toString().isEmpty()) {
			return templateString;
		}
		return stringBuffer.toString();
	}
}
