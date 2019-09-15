package file;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import configuration.FileConfiguration;
import common.UniversalDTOService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import util.ServiceDAOFactory;

import java.io.*;
import java.util.UUID;

@TableName("at_document")
public class FileDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("docID")
	long docID;
	@ColumnName("docActualFileName")
	String docActualFileName;
	@ColumnName("docLocalFileName")
	String docLocalFileName;
	@ColumnName("docEntityTypeID")
	long docEntityTypeID;
	@ColumnName("docEntityID")
	long docEntityID;
	@ColumnName("docOwner")
	long docOwner;
	@ColumnName("docUploadTime")
	long lastModificationTime;
	@ColumnName("docTypeID")
	String docTypeID;
	@ColumnName("docContentType")
	String docContentType;
	@ColumnName("docSize")	
	int docSize;
	String  docSizeStr;
	@ColumnName("docDirectoryPath")	
	String docDirectoryPath;

	private  Logger logger=Logger.getLogger(getClass());
	FormFile document;
	

	public boolean createLocalFileUsingFormFile(int docTypeID ) throws Exception {
		if(document==null){
			throw new IOException("No form file is found in FileDTO ");
		}
		
		Parser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		

		parser.parse( document.getInputStream(), handler, metadata, new ParseContext());
		logger.info("File name: " + document.getFileName() + ",\nActual content-type: " + metadata.get("Content-Type"));
		
		boolean isAllowed = FileUploadUtils.getMimeTypeAllowed(metadata);
		boolean isExtensionAllowed=FileTypeConstants.allowedExtensions.contains(FileUploadUtils.getSuffix(document.getFileName()));

		FileConfiguration fileConfiguration = ServiceDAOFactory.getService(UniversalDTOService.class).get(FileConfiguration.class);


		boolean isFileSizeAllowed = (document.getFileSize()> fileConfiguration.getMAXIMUM_FILE_SIZE())?false:true;
		
		
		logger.info("isFileTypeAllowed: " + isAllowed +", isExtensionAllowed: "+ isExtensionAllowed +", isFileSizeAllowed: "+ isFileSizeAllowed);
		
		/*if (document. .canExecute()) {
			file.setExecutable(false);// forcefully
		}*/

		if (!isAllowed || !isExtensionAllowed || !isFileSizeAllowed) {
			logger.fatal("isFileTypeAllowed: " + isAllowed +", isExtensionAllowed: "+ isExtensionAllowed +", isFileSizeAllowed: "+ isFileSizeAllowed);
			try {
				document.destroy();
			} catch (Exception e) {
				logger.fatal(e);
			}
			logger.fatal("File upload is not successfull");
			return false;
		} 
		
		String docDirectoryPath=getDirectory();
		
		String finalFileName=UUID.randomUUID().toString()+"."+FilenameUtils.getExtension( document.getFileName());
		String finalPath = FileTypeConstants.BASE_PATH + FileTypeConstants.FINAL_UPLOAD_DIR + docDirectoryPath+finalFileName;
		File documentFile = new File(finalPath);
		{
			this.setDocTypeID(docTypeID+"");
			this.setDocActualFileName( document.getFileName());
			this.setDocLocalFileName(finalFileName);
			this.setDocSize(document.getFileSize());
			this.setDocDirectoryPath(docDirectoryPath);
			
			documentFile.getParentFile().mkdirs();
			
			FileOutputStream fos = new FileOutputStream(documentFile);
			fos.write(getDocument().getFileData());
			fos.flush();
			fos.close();

		}
		logger.debug("Form File upload: " + documentFile.getAbsolutePath());
		return true;
	}

	public String getDirectory() {
		String docDirectoryPath=(this.docOwner%FileTypeConstants.MAXIMUM_DIRECOTRY_NUMBER)+"/"+this.docOwner+"/";
		return docDirectoryPath;
	}
	
	public void createLocalFileFromNames(String fileName) throws IOException{
		String docDirectoryPath=getDirectory();
		
		String[] parts = fileName.split("_");
		String finalFileName=UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(fileName);
		
		String tempPath=FileTypeConstants.BASE_PATH+FileTypeConstants.TEMP_UPLOAD_DIR+fileName;
		String finalPath=FileTypeConstants.BASE_PATH+FileTypeConstants.FINAL_UPLOAD_DIR+docDirectoryPath+finalFileName;
		
		File tempFile= new File(tempPath);
		File finalFile= new File(finalPath);
		
		if(tempFile.isFile()&& tempFile.exists()){ 
			//either first time upload
			File docDirectory= new File(FileTypeConstants.BASE_PATH+FileTypeConstants.FINAL_UPLOAD_DIR+ docDirectoryPath);
	     	if (!docDirectory.exists()) {
	            if (docDirectory.mkdirs()) {
	            	logger.debug("dir: "+ docDirectoryPath +" is created successfully.");
	            } else {
	            	logger.debug("dir: "+ docDirectoryPath +" is not created.");
	            }
	        }
			this.setDocTypeID(parts[0]);
			this.setDocActualFileName(fileName.substring(StringUtils.ordinalIndexOf(fileName, "_", 3)+1));
			this.setDocLocalFileName(finalFileName);
			this.setDocSize((int)tempFile.length());
			this.setDocDirectoryPath(docDirectoryPath);
			
			FileUtils.copyFile(tempFile, finalFile);
			/*try{
				String command = "scp "+tempPath+ " root@123.49.12.143:"+finalPath;// /home/kawser-dev/Downloads/book/Java_Interview_Questions.pdf dhrubo@192.168.19.223:";"
				executeCommand(command);
			    //FileUtils.copyFile(tempFile, finalFile);
				tempFile.delete();
			}catch(Exception ex){
				logger.fatal("Exception in deleting temp file:", ex);
			}*/
			
			logger.debug("File upload completed: "+fileName);	
		}
	}
	public void deleteFileFromDirectory() throws IOException{
		String docDirectotyPath=getDirectory();
		String finalPath=FileTypeConstants.BASE_PATH+FileTypeConstants.FINAL_UPLOAD_DIR+docDirectotyPath+this.getDocLocalFileName();
		File finalFile= new File(finalPath);
		
		if(finalFile.isFile()&& finalFile.exists()){
			try{
				finalFile.delete();
			}catch(Exception ex){
				logger.fatal("Exception in deleting temp file:", ex);
			}
		}else{
			logger.fatal("error: File is not found for removal ");
		}
	}
	
	private String executeCommand(String command) throws Exception {

		StringBuffer output = new StringBuffer();

		Process p;
		//try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		//} catch (Exception e) {
		//	e.printStackTrace();
		//}

		return output.toString();

	}
	
	public long getDocID() {
		return docID;
	}
	public void setDocID(long docID) {
		this.docID = docID;
	}
	public String getDocActualFileName() {
		return docActualFileName;
	}
	public void setDocActualFileName(String docActualFileName) {
		this.docActualFileName = docActualFileName;
	}
	public String getDocLocalFileName() {
		return docLocalFileName;
	}
	public void setDocLocalFileName(String docLocalFileName) {
		this.docLocalFileName = docLocalFileName;
	}
	public long getDocEntityTypeID() {
		return docEntityTypeID;
	}
	public void setDocEntityTypeID(long docEntityTypeID) {
		this.docEntityTypeID = docEntityTypeID;
	}
	public long getDocEntityID() {
		return docEntityID;
	}
	public void setDocEntityID(long docEntityID) {
		this.docEntityID = docEntityID;
	}
	public long getDocOwner() {
		return docOwner;
	}
	public void setDocOwner(long docOwner) {
		this.docOwner = docOwner;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public FormFile getDocument() {
		return document;
	}
	public void setDocument(FormFile document) {
		this.document = document;
	}
	public String getDocTypeID() {
		return docTypeID;
	}
	public void setDocTypeID(String docTypeID) {
		this.docTypeID = docTypeID;
	}
	
	public String getDocContentType() {
		return docContentType;
	}
	public void setDocContentType(String docContentType) {
		this.docContentType = docContentType;
	}
	public int getDocSize() {
		return docSize;
	}
	public void setDocSize(int docSize) {
		this.docSize = docSize;
	}
	public String getDocDirectoryPath() {
		return docDirectoryPath;
	}

	public void setDocDirectoryPath(String docDirectoryPath) {
		this.docDirectoryPath = docDirectoryPath;
	}

	public String getDocSizeStr() {
		float size=0;
		
		if(docSize>=1000000){
			size=((float)docSize/1000000);
			docSizeStr=size+"MB";
			return docSizeStr;
		}
		if(docSize>=1000){
			size=((float)docSize/1000);
			docSizeStr=size+"KB";
			return docSizeStr;
		}
		return docSizeStr;
	}
	public void setDocSizeStr(String docSizeStr) {
		this.docSizeStr = docSizeStr;
	}
	@Override
	public String toString() {
		return "FileDTO [docID=" + docID + ", docActualFileName=" + docActualFileName + ", docLocalFileName="
				+ docLocalFileName + ", docEntityTypeID=" + docEntityTypeID + ", docEntityID=" + docEntityID
				+ ", docOwner=" + docOwner + ", lastModificationTime=" + lastModificationTime + ", docTypeID="
				+ docTypeID + ", docContentType=" + docContentType + ", docSize=" + docSize + ", docSizeStr="
				+ docSizeStr + ", logger=" + logger + ", document=" + document + "]";
	}
	
}
