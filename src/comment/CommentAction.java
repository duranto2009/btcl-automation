package comment;

import com.google.gson.Gson;
import common.*;
import file.FileDTO;
import file.FileService;
import login.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import util.TimeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CommentAction extends Action {

	LoginDTO loginDTO = null;
	CommentService commentService = new CommentService();
	FileService fileDAO = new FileService();
	Calendar calendar = Calendar.getInstance();
	
	Logger logger= Logger.getLogger(CommentAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		if (request.getMethod().equalsIgnoreCase("get")) {
			 handleGet(mapping, form, request, response);
		} else if("add".equals(request.getParameter("mode"))) {
			return addComment(mapping, form, request, response);
		}
		return null;
	}

	private String handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		commentService = new CommentService();

		String start = request.getParameter("start");
		int iStart = Integer.parseInt(start) * 10;
		String entityTypeID = request.getParameter("entityTypeID");
		int iEntityTypeID = Integer.parseInt(entityTypeID);
		String entityID = request.getParameter("entityID");
		long iEntityID = Long.parseLong(entityID);

		ArrayList<CommentDTO> commentDTOs = commentService.getComment(iStart, iEntityTypeID, iEntityID, loginDTO);

		ArrayList<HashMap<String, String>> data = new ArrayList();

		for (CommentDTO commentDTO : commentDTOs) {
			HashMap<String, String> item = new HashMap<String, String>();
			System.out.println(commentDTO.getMemberID());
			String username = "";
			long userID = 0;
			if (commentDTO.getMemberID() > 0) {
				ClientDTO clientDTO = ClientRepository.getInstance().getClient(commentDTO.getMemberID());
				username = clientDTO.getLoginName();
				userID = commentDTO.getMemberID();
			} else {
				UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(-commentDTO.getMemberID());
				username = userDTO.getUsername();
				userID = -commentDTO.getMemberID();
			}
			ArrayList<FileDTO> fileDTOs = fileDAO.getFileByEntityTypeAndEntity(EntityTypeConstant.COMMENT, commentDTO.getID());
			FileDTO fileDTO = new FileDTO();
			if (fileDTOs.size() > 0)
				fileDTO = fileDTOs.get(0);

			calendar.setTimeInMillis(commentDTO.getLastModificationTime());
			
			item.put("id", commentDTO.getID() + "");
			item.put("description", commentDTO.getDescription());
			item.put("subject", commentDTO.getHeading());
			item.put("username", username);
			item.put("userID", userID + "");
			item.put("time", TimeConverter.getMeridiemTime(calendar.getTime().getTime()));
			if (fileDTOs.size() > 0) {
				item.put("filename", fileDTO.getDocActualFileName());
				item.put("et", fileDTO.getDocEntityTypeID() + "");
				item.put("e", fileDTO.getDocEntityID() + "");
			}

			data.add(item);
		}

		if (commentDTOs != null) {
			String list = new Gson().toJson(data);
			response.getWriter().write(list);
		}
		return null;
	}

	private String handlePost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String modeValue = request.getParameter("mode");
		if (modeValue == null) {
			throw new Exception("Invalid Request");
		}else if (modeValue.equalsIgnoreCase("update")) {
			return null;
		} else if (modeValue.equalsIgnoreCase("delete")) {
			return null;
		} else {
			throw new Exception("Invalid Request");
		}
	}

	private ActionRedirect addComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommentDTO commentDTO = (CommentDTO) form;
		int moduleID=Integer.parseInt(request.getParameter("moduleID"));
		long entityID=Long.parseLong(request.getParameter("entityID"));
		int entityTypeID=Integer.parseInt(request.getParameter("entityTypeID"));
		int currentTab=Integer.parseInt(request.getParameter("currentTab"));
		
		logger.debug(commentDTO);
		
		if(StringUtils.isBlank(commentDTO.getHeading())){
			commentDTO.setHeading("");
		}
		commentDTO.setLastModificationTime(System.currentTimeMillis());
		
		if(loginDTO.getIsAdmin()){
			commentService.addComment(commentDTO, loginDTO);
		}else{
			EntityDTO entityDTO=commentService.getEntityDTOByEntityIDAndEntityTypeID(entityTypeID, entityID);
			
			if((entityDTO!=null) && (entityDTO.getClientID()==loginDTO.getAccountID())){
				commentService.addComment(commentDTO, loginDTO);
			}else{
				new CommonActionStatusDTO().setErrorMessage("Permission Failure", false, request);
			}
		}
		
		
		if (null !=commentDTO.getDocuments() && commentDTO.getDocuments().length>0) {// Upload logic for document
			uploadDocuments(commentDTO, loginDTO,request);
		}
		
		ActionRedirect redirect =new ActionRedirect(mapping.findForward(BtclRedirectUtility.getCommentTarget(moduleID, entityTypeID)));
		BtclRedirectUtility.setForwardParameters(redirect, moduleID, entityID, entityTypeID, currentTab);
		
		new CommonActionStatusDTO().setSuccessMessage("Your comment is submitted successfully. ", false, request);
		return redirect;
	}
	
	private void uploadDocuments(CommentDTO commentDTO, LoginDTO loginDTO, HttpServletRequest request) throws Exception {
		FileService fileDAO = new FileService();
		for(int i=0; i<commentDTO.getDocuments().length;i++){
			FileDTO fileDTO = new FileDTO();
			fileDTO.setDocument(commentDTO.getDocument());
			if(loginDTO.getAccountID()>0){
				fileDTO.setDocOwner(loginDTO.getAccountID());
			}else{
				fileDTO.setDocOwner(-loginDTO.getUserID());
			}
			fileDTO.setDocEntityTypeID(EntityTypeConstant.COMMENT);
			fileDTO.setDocEntityID(commentDTO.getID());
			fileDTO.setLastModificationTime(System.currentTimeMillis());
	
			fileDTO.createLocalFileFromNames(commentDTO.getDocuments()[i]);
			fileDAO.insert(fileDTO);
		}

	}
}