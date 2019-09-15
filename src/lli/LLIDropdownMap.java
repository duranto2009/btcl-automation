package lli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLIDropdownMap {
		
	@SuppressWarnings("serial")
	public static final Map<String, List<LLIDropdownPair>> map = new HashMap<>(new HashMap<String, List<LLIDropdownPair>>() {{

		put("bandwidthType", Arrays.asList(
			new LLIDropdownPair(1, "MB"),
			new LLIDropdownPair(2, "GB")
		));
		
		put("connectionType", Arrays.asList(
			new LLIDropdownPair(1, "Regular"),
			new LLIDropdownPair(2, "Google")
		));
		
		put("incidentType", getDropDownPairListFromMap(LLIConnectionInstance.incidentMap));
	}});
	
	public static LLIDropdownPair getOptionByID(int ID, List<LLIDropdownPair> list) {
		for(LLIDropdownPair option : list) {
			if(ID == option.getID()) {
				return option;
			}
		}
		return null;
	}
	
	public static List<LLIDropdownPair> getDropDownPairListFromMap(Map<Integer, String> map){
		List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			list.add(new LLIDropdownPair(entry.getKey(), entry.getValue()));
		}
		return list;
	}
}
