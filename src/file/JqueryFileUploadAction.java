package file;

import client.IdentityTypeConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import configuration.FileConfiguration;
import common.UniversalDTOService;
import login.LoginDTO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.imgscalr.Scalr;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import sessionmanager.SessionConstants;
import util.ServiceDAOFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class JqueryFileUploadAction extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(JqueryFileUploadAction.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (request.getParameter("getfile") != null && !request.getParameter("getfile").isEmpty()) {
			File file = new File( FileTypeConstants.BASE_PATH + FileTypeConstants.TEMP_UPLOAD_DIR + request.getParameter("getfile"));
			final LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
			if (file.exists()) {
				logger.debug("file exists");
				int bytes = 0;
				ServletOutputStream op = response.getOutputStream();
				String headerKey = "Content-Disposition";
				String headerVal = "inline; filename=\"" + file.getName().replace(loginDTO.getAccountID() + "_" + request.getSession(true).getId() + "_", "") + "\"";
				
				response.setContentType(FileUploadUtils.getMimeType(file));
				response.setContentLength((int) file.length());
				response.setHeader(headerKey, headerVal);

				byte[] bbuf = new byte[4096];
				DataInputStream in = new DataInputStream(new FileInputStream(file));

				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}

				in.close();
				op.flush();
				op.close();
			}
			return ;
		} else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
			PrintWriter writer = response.getWriter();
			try {
				response.setContentType("application/json");
				File file = new File( FileTypeConstants.BASE_PATH + FileTypeConstants.TEMP_UPLOAD_DIR + request.getParameter("delfile"));
				if (file.exists()) {
					file.delete(); // TODO:check and report success
					writer.write("File is deleted successfully");
				}
			}catch (Exception e) {
				writer.write("File can be deleted. please check this:  " + e.toString());
			}
			writer.close();
		} else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
			File file = new File(FileTypeConstants.BASE_PATH + FileTypeConstants.TEMP_UPLOAD_DIR + request.getParameter("getthumb"));
			if (file.exists()) {
				String mimetype = FileUploadUtils.getMimeType(file);
				if (FileUploadUtils.isImageType(mimetype)) {
					BufferedImage im = ImageIO.read(file);
					if (im != null) {
						BufferedImage thumb = Scalr.resize(im, 75);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						
						ImageIO.write(thumb, FileUploadUtils.getSuffix(file.getName()).toUpperCase(), os);
						response.setContentType(mimetype);
						
						ServletOutputStream srvos = response.getOutputStream();
						response.setContentLength(os.size());
						response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
						os.writeTo(srvos);
						srvos.flush();
						srvos.close();
					}
				}
			} // TODO: check and report success
		}else{
			PrintWriter writer = response.getWriter();
			writer.write("default response");
			writer.close();
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(sessionmanager.SessionConstants.USER_LOGIN);
		
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		JsonArray filesJSON = new JsonArray();
		JsonObject finalJSON = new JsonObject();
		
		if(loginDTO==null){
			JsonObject fileJSON = new JsonObject();
			fileJSON.addProperty("name","Please login");
			fileJSON.addProperty("size", "0");
			fileJSON.addProperty("error", "You can not upload file without login");
			filesJSON.add(fileJSON);
			finalJSON.add("files", filesJSON);
			response.getWriter().write(finalJSON.toString());
			return;
		}
		
		String basePath=FileTypeConstants.BASE_PATH + FileTypeConstants.TEMP_UPLOAD_DIR;
		File folder = new File(basePath);
		if (!folder.exists()) {
			if(!folder.mkdirs()){
				logger.fatal("File upload directory is not ready. please check this folder. ("+ basePath +")");
				new Exception("Please allow permission in destination folder (" + basePath +")");
				return ;
			}
		}
		
		if(!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}else{
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().startsWith(loginDTO.getAccountID() + "") && !fileEntry.getName().contains(request.getSession().getId())) {
					try {
						fileEntry.delete();
					} catch (Exception e) {
						this.logger.debug(e);
					}
				}
			}
		}

		ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());

		
		String prefix = null;
		if (loginDTO.getAccountID() > 0) {
			prefix = loginDTO.getAccountID() + "_" + request.getSession().getId() + "_";
		} else {
			prefix = loginDTO.getUserID() + "_" + request.getSession().getId() + "_";
		}

		String contextPath = request.getSession(true).getServletContext().getContextPath();

		try {
			FileConfiguration fileConfiguration = ServiceDAOFactory.getService(UniversalDTOService.class).get(FileConfiguration.class);
			List<FileItem> items = uploadHandler.parseRequest(request);
			for (FileItem item : items) {
				JsonObject fileJSON = new JsonObject();


				if(item.getSize()> fileConfiguration.getMAXIMUM_FILE_SIZE()){
					fileJSON.addProperty("name", item.getName());
					fileJSON.addProperty("size", item.getSize());
					fileJSON.addProperty("error", "File size is too large");
					filesJSON.add(fileJSON);
				}else if (!item.isFormField()) {
					String fieldName = item.getFieldName();
					prefix = fieldName + "_" + prefix;

					File file = new File(FileTypeConstants.BASE_PATH + FileTypeConstants.TEMP_UPLOAD_DIR, prefix + item.getName());
					logger.info("Jquery File Upload Path: " + FileTypeConstants.BASE_PATH);
					try (FileOutputStream fout = new FileOutputStream(file);
						BufferedOutputStream bout = new BufferedOutputStream(fout);
						BufferedInputStream bin = new BufferedInputStream(item.getInputStream());) {

						byte buf[] = new byte[2048];
						while ((bin.read(buf)) != -1) {
							bout.write(buf);
						}
					} catch (Exception e) {
						logger.fatal("", e);
					}
					
					/*check file size item.getSize()
					option 2 for writing file
				 	byte[] data = IOUtils.toByteArray(bin); bout.write(data);
					option 3 for writing file
				 	item.write(file);*/
				
					Parser parser = new AutoDetectParser();
					ContentHandler handler = new BodyContentHandler();
					Metadata metadata = new Metadata();
					

					parser.parse(new FileInputStream(file), handler, metadata, new ParseContext());
					logger.info("File name: " + file.getName() + ",\ncan execute: " + file.canExecute() + ",\nActual content-type: " + metadata.get("Content-Type"));
					
					boolean isAllowed = FileUploadUtils.getMimeTypeAllowed(metadata);
					boolean isExtensionAllowed=FileTypeConstants.allowedExtensions.contains(FileUploadUtils.getSuffix(file.getName()));
					String mimetype = FileUploadUtils.getMimeType(metadata);

					if((Integer.parseInt(fieldName)==FileTypeConstants.CLIENT.CLIENT_PROFILE_PICTURE) && FileUploadUtils.isImageType(mimetype)){
						logger.info("Client profile is resized");
						FileUploadUtils.resizeProfileImage(mimetype, file);
					}
					
					logger.info("isFileTypeAllowed: " + isAllowed +", isExtensionAllowed: "+ isExtensionAllowed);
					if (file.canExecute()) {
						file.setExecutable(false);// forcefully
					}

					if (!isAllowed || !isExtensionAllowed) {
						logger.fatal("isFileTypeAllowed: " + isAllowed +", isExtensionAllowed: "+ isExtensionAllowed);
						try {
							file.delete();
						} catch (Exception e) {
							logger.fatal(e);
						}
						fileJSON.addProperty("name", item.getName());
						fileJSON.addProperty("size", item.getSize());
						fileJSON.addProperty("error", "This file is not allowed to upload");
						filesJSON.add(fileJSON);
					} else {
						// document.setExecutable(false);
						
						fileJSON.addProperty("prefix", prefix);
						fileJSON.addProperty("name", item.getName());
						fileJSON.addProperty("modified_name", prefix + item.getName());
						fileJSON.addProperty("document_type",  IdentityTypeConstants.IdentityTypeName.get(Integer.parseInt(fieldName)));
						fileJSON.addProperty("size", item.getSize());
						fileJSON.addProperty("type", mimetype);
						fileJSON.addProperty("url", contextPath + "/JqueryFileUpload?getfile=" + prefix + item.getName());
						
						fileJSON.addProperty("identity_type", IdentityTypeConstants.IdentityTypeName.get(Integer.parseInt(fieldName)));
						
						fileJSON.addProperty("thumbnail_url", "");
						if (mimetype.endsWith("png") || mimetype.endsWith("jpeg") || mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
							fileJSON.addProperty("thumbnail_url", contextPath + "/JqueryFileUpload?getthumb=" + prefix + item.getName());
						}
						fileJSON.addProperty("delete_url", contextPath + "/JqueryFileUpload?delfile=" + prefix + item.getName());
						fileJSON.addProperty("delete_type", "GET");

						filesJSON.add(fileJSON);
					}
					logger.debug(fileJSON.toString());
				}
			}
			finalJSON.add("files", filesJSON);

		}catch (FileUploadException e) {
			logger.fatal( e.getMessage());
		} catch (FileNotFoundException e) {
			logger.fatal("", e);
		} catch (IOException e) {
			logger.fatal("", e);
		} catch (SAXException e) {
			logger.fatal("", e);
		} catch (TikaException e) {
			logger.fatal("", e);
		} catch (Exception e) {
			logger.fatal("", e);
		} finally {
			writer.write(finalJSON.toString());
			try{
				writer.close();
			}catch(Exception ee){
				logger.fatal("Stream closing problem");
			}
		}

	}

	
}