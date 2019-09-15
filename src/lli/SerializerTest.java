package lli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class SerializerTest {

	public static void main(String[] args) {
		System.out.println(LLIDropdownMap.map.getClass());
		System.out.println(new Gson().toJson(LLIDropdownMap.map));
		
		
		
		Map map1 = new HashMap<>(new HashMap() {{
		    put("a", "1");
		    put("b", "2");
		    put("c", "3");
		}});

		String json1 = new Gson().toJson(map1);

		Map map2 = new HashMap();
		map2.put("a", new ArrayList<>(Arrays.asList(1,2,3)));
		map2.put("b", "2");
		map2.put("c", "3");

		String json2 = new Gson().toJson(map2);

		System.out.println("json1 = " + json1); // null
		System.out.println("json2 = " + json2); // {"a":"1","b":"2","c":"3"}
		System.out.println("Is Equals: " + map1.equals(map2)); // true

	}

}
