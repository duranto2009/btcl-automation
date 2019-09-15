package file.upload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

public class FileServletConstants {

	public static final int TABLE_LLI_APPLICATION = 1;
	
	public static Map<Integer, String> tableIDNameMap = new HashMap<Integer, String>();
	
	static {
		tableIDNameMap.put(TABLE_LLI_APPLICATION, "at_lli_application");
	}
	
	
	
	
	
	/**
	 * Same as old method
	 */
	
	public static final String BASE_PATH =SystemUtils.IS_OS_LINUX?"/srv/btclautomation/resources/":"C:/btclautomation/resources/";
	public static final String TEMP_UPLOAD_DIR = "temp-uploads/";
	public static final String FINAL_UPLOAD_DIR = "uploads/";
	public static final int MAXIMUM_DIRECOTRY_NUMBER=1000;
	
	public static final HashSet<String> allowedContentTypes = new HashSet<String>();
	public static HashSet<String> allowedExtensions=new HashSet<String>();
	
	static {
		allowedContentTypes.add("image/png");
		allowedContentTypes.add("image/gif");
		allowedContentTypes.add("image/jpg");
		allowedContentTypes.add("image/jpeg");
		allowedContentTypes.add("application/pdf");
		
		allowedExtensions.add("png");
		allowedExtensions.add("gif");
        allowedExtensions.add("jpg");
        allowedExtensions.add("jpeg");
        allowedExtensions.add("pdf");
	}
	
	/**
	 * Same as old method
	 */
	
}
