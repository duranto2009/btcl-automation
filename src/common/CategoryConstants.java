
package common;

import java.util.HashMap;

/**
 * @author $h@r!f
 *
 */
public class CategoryConstants {
	public static final int CATEGORY_ID_DISTRICT = 1;
	public static final int CATEGORY_ID_UPAZILA = 2;
	public static final int CATEGORY_ID_UNION = 3;
	public static final int CATEGORY_ID_POP= 4;
	public static final int CATEGORY_ID_ROUTER= 5;
	public static final int CATEGORY_ID_PORT = 99;
	public static final int CATEGORY_ID_VLAN= 100;
	
	
	
	public static HashMap<Integer, String> CATEGORY_TYPE_MAP = new HashMap<Integer, String>();
	
	static{
		CATEGORY_TYPE_MAP.put(CATEGORY_ID_DISTRICT, "District");
	}
}
