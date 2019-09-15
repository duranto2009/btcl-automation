package util;

import com.google.gson.JsonObject;

public interface JsonProcessorHelper {
	public Object deserialize(JsonObject jsonObject);
	public Object serialize(Object object);
}
