package client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ModuleConstants;

public class ClientCategoryConstants {
	
	public static final int Category_1 = 1;
	public static final int Category_2 = 2;
	public static final int Category_3 = 3;
	public static final int Category_4 = 4;
	
	public static Map<Integer, String> CategoryName = new HashMap<Integer, String>();
	
	static {
		CategoryName.put(Category_1, "Category 1");
		CategoryName.put(Category_2, "Category 2");
		CategoryName.put(Category_3, "Category 3");
		CategoryName.put(Category_4, "Category 4");
	}
	
	public static Map<Integer, List<Integer>> CategoryListToModuleID = new HashMap<>();
	
	private static void populateCategoryConstants(int moduleID, Integer...integers) {
		List<Integer> tempList = new ArrayList<>();
		for(Integer integer : integers) {
			tempList.add(integer);
		}
		CategoryListToModuleID.put(moduleID, tempList);
	}
	
	
	static {
		populateCategoryConstants(ModuleConstants.Module_ID_VPN, Category_1);
		populateCategoryConstants(ModuleConstants.Module_ID_LLI, Category_1, Category_2, Category_3, Category_4);
	}
	
}
