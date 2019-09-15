package file;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.imgscalr.Scalr;

public class FileUploadUtils {
	private FileUploadUtils(){
		
	}
	public static String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
				mimetype = "image/jpg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
				mimetype = "image/jpeg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
				mimetype = "image/gif";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		return mimetype;
	}

	public static String getMimeType(Metadata metadata) {
		return metadata.get("Content-Type").toLowerCase();
	}

	public static boolean getMimeTypeAllowed(Metadata metadata) {
		if (FileTypeConstants.allowedContentTypes.contains(metadata.get("Content-Type").toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	public static File resizeProfileImage(String mimetype, File file) throws IOException{
		BufferedImage im = ImageIO.read(file);
		if (im != null) {
			BufferedImage thumb = Scalr.resize(im, 300);
			ByteArrayOutputStream tempBOS = new ByteArrayOutputStream();
			ImageIO.write(thumb, getSuffix(file.getName()).toUpperCase(), tempBOS);
			
			File resizedFile=new File(file.getAbsolutePath());
			try(FileOutputStream fout = new FileOutputStream(resizedFile);
				BufferedOutputStream bout = new BufferedOutputStream(fout);)
			{
				bout.write(tempBOS.toByteArray());
			}catch (Exception e) {
				// TODO: handle exception
			}
			return resizedFile;
		}
		return file;
	}
	public static boolean isImageType(String mimetype) {
		if( "image/png".equalsIgnoreCase(mimetype)){
			return true;
		}
		if( "image/jpg".equalsIgnoreCase(mimetype)){
			return true;
		}
		if( "image/jpeg".equalsIgnoreCase(mimetype)){
			return true;
		}
		if( "image/gif".equalsIgnoreCase(mimetype)){
			return true;
		}
		
		return false;
	}


	public static String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		return StringUtils.trimToEmpty(suffix).toLowerCase();
	}
}
