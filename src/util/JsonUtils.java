package util;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class JsonUtils {
	public static<T> List<T> getObjectListByJsonString(String jsonString,Class<T> classObject ){
		
		return getObjectListByJsonString(jsonString, classObject,new GsonBuilder().create());
	}
	public static<T> List<T> getObjectListByJsonString(String jsonString,Class<T> classObject ,Gson gson){
		
		List<T> list = new ArrayList<>();
		
		 for(JsonElement jsonElement: new JsonParser().parse(jsonString).getAsJsonArray()){
			  T object = gson.fromJson(jsonElement,classObject);
			  list.add(object);
		 }
		
		return list;
	}
	
	public static<T> T getObjectByJsonString(String jsonString,Class<T> classObject ){
		
		Gson gson = new GsonBuilder().create();
		JsonElement jsonElement = new JsonParser().parse(jsonString);
		T object = gson.fromJson(jsonElement,classObject);
		
		return object;
	}

	
	public static<T> String getJsonStringFromList(List<T> list ){
		
		Gson gson = new GsonBuilder().create();
		String jsonContent = gson.toJson(list, new TypeToken<ArrayList<T>>() {}.getType());
		
		return jsonContent;
	}
	
	public static<T> String getJsonStringFromObject(T object ){
		
		Gson gson = new GsonBuilder().create();
		String jsonContent = gson.toJson(object, new TypeToken<T>() {}.getType());
		
		return jsonContent;
	}
}
