package profile;

import common.ClientDTO;
import common.CommonActionStatusDTO;
import common.repository.AllClientRepository;
import file.FileDTO;
import file.FileService;
import file.FileTypeConstants;
import login.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import permission.PermissionDAO;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.PasswordService;
import util.ServiceDAOFactory;
import vpn.client.ClientService;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

public class ClientInfoAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	PermissionDAO permissionDAO = new PermissionDAO();
	LoginDTO loginDTO = null;
	String actionForwardName = "profile";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception  {
		
		loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
//		logger.debug(loginDTO);
		if (request.getMethod().equalsIgnoreCase("get")) {
			return handleGet(mapping, form, request, response);
		}else if("updatePicture".equals(request.getParameter("type"))) {
			return handlePictureUpload(mapping, form, request, response); 
		}else if("updatePassword".equals(request.getParameter("type"))) {
			return handlePassword(mapping, form, request, response);
		}else if( "updatePasswordAdmin".equals( request.getParameter( "type" ) ) ){
			return handlePasswordChangeAdmin( mapping, form, request, response );
		}

		return mapping.findForward(actionForwardName);
	}

	private ActionForward handlePictureUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String []documents=request.getParameterValues("documents");
		
		if (documents != null && documents.length>0) {
			// Upload logic for document
			uploadDocument(documents, loginDTO, request);
		}
		CommonActionStatusDTO actionDTO= new CommonActionStatusDTO();
		actionDTO.setStatusCode(CommonActionStatusDTO.SUCCESS_STATUS_CODE);
		actionDTO.setMessage("Profile picture is changed successfully");
		actionDTO.storeInSession(request, response);
		return mapping.findForward(actionForwardName);
	}

	private ActionForward handlePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		CommonActionStatusDTO actionDTO= new CommonActionStatusDTO();
		String currentPassword=StringUtils.trimToEmpty(request.getParameter("currentPassword"));
		String password=StringUtils.trimToEmpty(request.getParameter("password"));
		String rePassword=StringUtils.trimToEmpty(request.getParameter("rePassword"));
		
		String validationMessage="";
		
		if(loginDTO.getAccountID()>0){
			ClientService clientService = new ClientService();
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(loginDTO.getAccountID());
			
			if(!clientDTO.getLoginPassword().equals(PasswordService.getInstance().encrypt(currentPassword))){
				validationMessage+="Wrong current password<br>";
			}
			if(password.isEmpty() || password.length()>50 || password.length()<4){
				validationMessage+="Password size should be at least 4 or at most 50<br>";
			}
			if(!password.equals(rePassword)){
				validationMessage+="New Password and  confirm new password are not same<br>";
			}
			
			if(validationMessage.length()>0 ){
				actionDTO.setErrorMessage(validationMessage, false, request);
				return mapping.findForward(actionForwardName);
			}
			String encryptedPassword = PasswordService.getInstance().encrypt(password);
			if(!clientDTO.getLoginPassword().equals(encryptedPassword))
			{
				clientDTO.setLoginPassword(encryptedPassword);
				clientDTO.setLastModificationTime(System.currentTimeMillis());
			}
			
			clientService.updateNewGeneralClient(clientDTO, loginDTO);
		
		} else if(loginDTO.getUserID()>0){
			UserService service = ServiceDAOFactory.getService(UserService.class);
			UserDTO userDTO = service.getUserDTOByUserID(loginDTO.getUserID());
			
			if(!userDTO.getPassword().equals(PasswordService.getInstance().encrypt(currentPassword))){
				validationMessage+="Wrong current password<br>";
			}
			if(password.isEmpty() || password.length()>50 || password.length()<4){
				validationMessage+="Password size should be at least 4 or at most 50<br>";
			}
			if(!password.equals(rePassword)){
				validationMessage+="New Password and  confirm new password are not same<br>";
			}
			
			if(validationMessage.length()>0 ){
				actionDTO.setErrorMessage(validationMessage, false, request);
				return mapping.findForward(actionForwardName);
			}
			
			String encryptedPassword = PasswordService.getInstance().encrypt(password);
			if(!userDTO.getPassword().equals(encryptedPassword))
			{
				userDTO.setPassword(encryptedPassword);
				userDTO.setLastModifyTime(System.currentTimeMillis());
				service.updateUser(userDTO, loginDTO);
				UserRepository.getInstance().reload(false);
			}
		}
			
		actionDTO.setSuccessMessage("Password is changed successfully", false, request);
		return mapping.findForward(actionForwardName);
	}

	private ActionForward handlePasswordChangeAdmin( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		String validationMessage="";
		String forward = "changePasswordAdmin";
		
		CommonActionStatusDTO actionDTO= new CommonActionStatusDTO();
		
		if( loginDTO.getIsAdmin() ){
						
			String userIDStr = StringUtils.trimToEmpty( request.getParameter( "clientID") );
			String clientUserName = StringUtils.trimToEmpty( request.getParameter( "clientUserName") );
			
			request.setAttribute("clientID", userIDStr);
			request.setAttribute("clientUserName", clientUserName);
			
			long userID = -1;
			//String currentPassword=StringUtils.trimToEmpty(request.getParameter("currentPassword"));
			String password=StringUtils.trimToEmpty(request.getParameter("password"));
			String rePassword=StringUtils.trimToEmpty(request.getParameter("rePassword"));
			
			if( userIDStr.trim().length() == 0 || userIDStr.trim().equals( "-1" ) ){
				validationMessage+="No user is found<br>";
				actionDTO.setErrorMessage(validationMessage, false, request);
				return mapping.findForward(forward);
			}
			else{
				userID = Long.parseLong( userIDStr );
			}

			ClientService clientService = new ClientService();
			
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( userID );
			
			/*if(!clientDTO.getLoginPassword().equals(PasswordService.getInstance().encrypt(currentPassword))){
				validationMessage+="Wrong current password<br>";
			}*/
			if(password.isEmpty() || password.length()>50 || password.length()<4){
				validationMessage+="Password size should be at least 4 or at most 50<br>";
			}
			if(!password.equals(rePassword)){
				validationMessage+="New Password and  confirm new password are not same<br>";
			}
			
			if(validationMessage.length()>0 ){
				actionDTO.setErrorMessage(validationMessage, false, request);
				return mapping.findForward(forward);
			}
			
			String encryptedPassword = PasswordService.getInstance().encrypt(password);
			
			if(!clientDTO.getLoginPassword().equals(encryptedPassword))
			{
				clientDTO.setLoginPassword(encryptedPassword);
				clientDTO.setLastModificationTime(System.currentTimeMillis());
			}
			
			clientService.updateNewGeneralClient(clientDTO, loginDTO);
		}
		else{
			
			actionDTO.setErrorMessage( "You don't have permission to perform this action", true, request );
			return mapping.findForward(forward );
		}
		
		actionDTO.setSuccessMessage("Password is changed successfully", false, request);
		return mapping.findForward(forward);
	}
	
	private ActionForward handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest p_request, HttpServletResponse p_response) {
		if("showPicture".equals(p_request.getParameter("type"))){
			try {
				long clientID;
				
				String fileName="";
				if (p_request.getParameter("clientID") !=null){
					clientID=Long.parseLong(p_request.getParameter("clientID"));
					fileName=AllClientRepository.getInstance().getClientByClientID(clientID).getProfilePicturePath();
				}else if(p_request.getParameter("userID") !=null){
					clientID=Long.parseLong(p_request.getParameter("userID"));
					fileName=UserRepository.getInstance().getUserDTOByUserID(clientID).getProfilePicturePath();
					//logger.debug(UserRepository.getInstance().getUserDTOByUserID(clientID));
				}else{
					fileName=loginDTO.getProfilePicturePath();
				}
				
				String finalPath = FileTypeConstants.BASE_PATH + FileTypeConstants.FINAL_UPLOAD_DIR+ fileName;

				File file = new File(finalPath);
				if (file.exists()) {
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
					String headerValue = String.format("attachment; filename=\"%s\"", loginDTO.getProfilePicturePath());
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
				} else {
					throw new Exception("No such file or directory");
				}
			} catch (Exception e) {

			}
			return null;
		}else{
			return mapping.findForward(actionForwardName);
		}

	}

	private void uploadDocument(String []documents, LoginDTO loginDTO, HttpServletRequest p_request) throws Exception {
		FileService fileDAO = new FileService();
		for(int i=0; i<documents.length;i++){
			FileDTO fileDTO = new FileDTO();
			//fileDTO.setBasePath(p_request.getSession(true).getServletContext().getRealPath("/"));
			if(loginDTO.getAccountID()>0){
				fileDTO.setDocOwner(loginDTO.getAccountID());
				fileDTO.setDocEntityID(loginDTO.getAccountID());
			}else{
				fileDTO.setDocOwner(-loginDTO.getUserID());
				fileDTO.setDocEntityID(loginDTO.getUserID());
			}
			fileDTO.setDocEntityTypeID(FileTypeConstants.ENTITY_TYPE_PROFILE_PICTURE);
			fileDTO.setLastModificationTime(System.currentTimeMillis());
			fileDTO.createLocalFileFromNames(documents[i]);
			fileDAO.insert(fileDTO);
			
			if(i==(documents.length-1)){
				if(loginDTO.getAccountID()>0){
					ClientService clientService= new ClientService();
					ClientDTO clientDTO=clientService.getClientDTO(loginDTO.getAccountID(), loginDTO);
					clientDTO.setProfilePicturePath(fileDTO.getDirectory()+fileDTO.getDocLocalFileName());
					clientDTO.setLastModificationTime(System.currentTimeMillis());
					clientService.updateNewGeneralClient(clientDTO, loginDTO);
					AllClientRepository.getInstance().reloadClientRepository(false);
					logger.debug("client profile pic updated");
				}else{
					UserService userService= ServiceDAOFactory.getService(UserService.class);
					UserDTO userDTO=userService.getUserDTOByUserID(loginDTO.getUserID());
					userDTO.setProfilePicturePath(fileDTO.getDirectory()+fileDTO.getDocLocalFileName());
					userDTO.setLastModifyTime(System.currentTimeMillis());
					userService.updateUser(userDTO, loginDTO);
					UserRepository.getInstance().reload(false);
				}
				loginDTO.setProfilePicturePath(fileDTO.getDirectory()+fileDTO.getDocLocalFileName());
			}
		}
		
	}
	
}