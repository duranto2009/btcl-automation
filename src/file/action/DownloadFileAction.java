package file.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.LoginDTO;
import login.PermissionConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;
import file.FileService;
import file.FileDTO;
import file.FileTypeConstants;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;

public class DownloadFileAction extends Action {
	static Logger logger = Logger.getLogger(DownloadFileAction.class);
	// size of byte buffer to send file

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)  {

		FileService fileDAO=new FileService();
		
		SessionManager sessionManager = new SessionManager();
		if (!sessionManager.isLoggedIn(p_request)) {
			return (p_mapping.findForward("login"));
		}

		try {
			
			int documentID=Integer.parseInt(p_request.getParameter("documentID"));
			FileDTO fileDTO=fileDAO.getFilebyDocID(documentID);
			String finalPath=FileTypeConstants.BASE_PATH+ FileTypeConstants.FINAL_UPLOAD_DIR+fileDTO.getDocDirectoryPath()+fileDTO.getDocLocalFileName();
			
			logger.debug("Final path for download: "+ finalPath);
			//String filePath = getServlet().getInitParameter("downloadDir") + p_request.getParameter("download");

			File file = new File(finalPath);
			if(!file.exists()){
				String command = "";
				command = "scp root@123.49.12.143:"+finalPath+" "+finalPath;
				executeCommand(command);
				//fileDownload(fileDTO,file,p_response);
			}
			
			fileDownload(fileDTO,file,p_response);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.fatal("ex",e);
		}

		return null;
	}
	
	private void fileDownload(FileDTO fileDTO,File file,HttpServletResponse p_response) throws Exception{
		FileInputStream inputStream = new FileInputStream(file);
		int fileLength = inputStream.available();

		ServletContext context = this.getServlet().getServletContext();
		MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		// sets MIME type for the file download
		String mimeType = context.getMimeType(fileTypeMap.getContentType(file));
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		// set content properties and header attributes for the response
		p_response.setContentType(mimeType);
		p_response.setContentLength(fileLength);
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileDTO.getDocActualFileName());
		p_response.setHeader(headerKey, headerValue);

		// writes the file to the client
		ServletOutputStream outputStream = p_response.getOutputStream();

		byte[] outputByte = new byte[FileTypeConstants.BUFFER_SIZE];
		// copy binary content to output stream
		while (inputStream.read(outputByte, 0, FileTypeConstants.BUFFER_SIZE) != -1) {
			outputStream.write(outputByte, 0, FileTypeConstants.BUFFER_SIZE);
		}
		inputStream.close();
		outputStream.flush();
		outputStream.close();
	}
	
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
}
