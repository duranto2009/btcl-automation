package api;

import java.io.File;

import file.FileTypeConstants;
import org.apache.log4j.Logger;
import util.TimeConverter;

public class FileAPI {
	private final Logger logger = Logger.getLogger(getClass());
	private static final FileAPI fileAPI= new FileAPI();
	
	private FileAPI() {}
	public static FileAPI getInstance() {
		return fileAPI;
	}
	
	public File createDirectory(String path) throws Exception {
		logger.info("Attempting to create directory: " + path);
		File dir = new File(path);
		if (!dir.exists()) {
            if (!dir.mkdirs()) {
            	throw new Exception( "Directory creation failed!" );
            }else {
            	logger.info("Directory created");
            }
     	}else {
     		logger.info("Directory exists");
     	}
		return dir;
	}

	public String getFilePath(String folderName, String proposedName, long generationTime) throws Exception {

		String sb = FileTypeConstants.BASE_PATH +
				folderName +
				File.separatorChar +
				TimeConverter.getYear(generationTime) +
				File.separatorChar +
				TimeConverter.getMonth(generationTime) +
				File.separatorChar;
		File file = FileAPI.getInstance().createDirectory(sb);
		return file.getPath() + File.separatorChar + proposedName;
	}
}
