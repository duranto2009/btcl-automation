package client;

import java.util.HashMap;
import java.util.Map;

public class RegistrantTypeConstants {
	
	public static final int GOVT = 1;
	public static final int MILITARY = 2;
	public static final int PRIVATE = 3;
	public static final int FOREIGN = 4;
	public static final int OTHERS = 5;
	
	public static final int ANY = -1; //Extremely sensible; Handle with Care.
	
	public static Map<Integer, String> RegistrantTypeName = new HashMap<>();
	
	static {
		RegistrantTypeName.put(GOVT, "Govt. /Semi-Govt. /Autonomous");
		RegistrantTypeName.put(MILITARY, "Military");
		RegistrantTypeName.put(PRIVATE, "Private");
		RegistrantTypeName.put(FOREIGN, "Foreign");
		RegistrantTypeName.put(OTHERS, "Others");
	}
}
