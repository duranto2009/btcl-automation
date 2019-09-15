package complain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.LoginDTO;
import login.PermissionConstants;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;

import com.google.gson.Gson;

import common.CommonActionStatusDTO;
import common.EntityTypeConstant;
import common.RequestFailureException;
import common.RequestFailureExceptionForwardToSamePage;
import file.FileService;
import file.FileDTO;

public class ComplainAction extends Action {
	
	Logger logger = Logger.getLogger(ComplainAction.class);
	LoginDTO loginDTO = null;
	ComplainService complainService = new ComplainService();
	ComplainDAO complainDAO = new ComplainDAO();
	FileService fileDAO = new FileService();
	DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	Calendar calendar = Calendar.getInstance();
	long forwardID = 0;
	ActionRedirect redirect;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String actionForwardName = "";
		response.setContentType("application/json");
		loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
		boolean hasPermission = false;
		
		if(loginDTO.getUserID()>0)
		{
		    if((loginDTO.getMenuPermission(PermissionConstants.COMPLAIN) !=-1)
		    		
		      &&(loginDTO.getMenuPermission(PermissionConstants.COMPLAIN_SEARCH) >= PermissionConstants.PERMISSION_READ))
		    	
		        hasPermission=true;
		    
		}
		else if( loginDTO.getAccountID() > 0 ){
			
			hasPermission = true;
		}

		if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward( "applicationRoot" );
		}
		
		logger.debug(loginDTO);
		
		if (request.getMethod().equalsIgnoreCase("get")) {
			
			actionForwardName = handleGet(mapping, form, request, response);
		} else {
			
			actionForwardName = handlePost(mapping, form, request, response);
			ArrayList<HashMap<String, String>> data = null;
			String list = new Gson().toJson(data);
			if (actionForwardName.contentEquals("success")) {

				response.getWriter().write(list);
			}
		}
		if (forwardID > 0) {
			ActionForward actionForward = new ActionForward();
			actionForward.setPath("Complain.do?id=" + forwardID);
			forwardID = 0;
			actionForward.setRedirect(true);
			return actionForward;
		}
		
		if(actionForwardName.equalsIgnoreCase("redirect")){
			return redirect;
		}

		return mapping.findForward(actionForwardName);
	}

	private String handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String parameterValue = request.getParameter("id");
		String actionForwardName = "failure";
		if(parameterValue == null ){
			return handleSearch(mapping, form, request, response);
		} 

		if (parameterValue.trim().equals("next") ||parameterValue.trim().equals("previous") || parameterValue.trim().equals("first") || parameterValue.trim().equals("last")) {
			return handleSearch(mapping, form, request, response);
		} else {
			actionForwardName = viewComplain(mapping, form, request, response);
		}
		logger.debug(actionForwardName);
		return actionForwardName;
	}

	private String handlePost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String modeValue = request.getParameter("mode");
		if (modeValue == null) {
			return handleSearch(mapping, form, request, response);	
		} else if (modeValue.equalsIgnoreCase("add")) {
			return addComplain(mapping, form, request, response);
		} else if (modeValue.equalsIgnoreCase("history")) {
			return addComplainHistory(mapping, form, request, response);
		} else if (modeValue.equalsIgnoreCase("update")) {
			return null;
		} else if (modeValue.equalsIgnoreCase("delete")) {
			return null;
		} else if (modeValue.equals("search")) {
			return handleSearch(mapping, form, request, response);	
		}else {
			throw new RequestFailureException("Invalid Request");
		}
	}
	
	private String handleSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String actionForwardName="failure";
		//LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
		ComplainService complainService = new ComplainService();
		complainService.setModuleID(moduleID);
		complainService.setLoginDTO(loginDTO);
		
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_COMPLAIN, request, complainService, SessionConstants.VIEW_COMPLAIN, SessionConstants.SEARCH_COMPLAIN);

		logger.debug(loginDTO);
		rnManager.doJob(loginDTO);
		actionForwardName = "success";
		return actionForwardName;
	}

	private String addComplainHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ComplainHistoryDTO complainHistoryDTO = (ComplainHistoryDTO) form;
		if(!validateCompaintHistoryDTO(complainHistoryDTO)){
			throw new RequestFailureException("Please fill up all required fields.");
		}
		forwardID = complainHistoryDTO.getComplainID();
		return complainService.addComplainHistory(complainHistoryDTO, loginDTO);
	}

	private boolean validateCompaintHistoryDTO(ComplainHistoryDTO complainHistoryDTO) {
		if(complainHistoryDTO == null)
			return false;
		if(complainHistoryDTO.getComplainID() <= 0){
			return false;
		}
		if(complainHistoryDTO.getMessage().trim().equals("")){
			return false;
		}
		if(loginDTO.getIsAdmin()){
			if(complainHistoryDTO.getStatus() < 0){
				return false;
			}
			if(complainHistoryDTO.getNote().trim().equals("")){
				return false;
			}
		}
		return true;
		
	}

	private String addComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ComplainDTO complainDTO = (ComplainDTO) form;
		complainDTO.setCreationTime(System.currentTimeMillis());
		if(!loginDTO.getIsAdmin()){
			complainDTO.setClientID(loginDTO.getAccountID());
		}else{
			throw new Exception("Admin can not add a complain");
		}

		String _return = complainService.addComplain(complainDTO, loginDTO);
		forwardID = complainDTO.getID();
		return _return;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String viewComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long complainID = Integer.parseInt(request.getParameter("id"));

		ArrayList<ComplainHistoryDTO> complainHistoryDTOs = (ArrayList<ComplainHistoryDTO>) complainDAO.getComplainHistoryByComplainID(complainID, loginDTO);
		HashMap complainHistoryMap = (HashMap)complainDAO.getComplainHistoryMap(complainHistoryDTOs);
		ArrayList<FileDTO> fileDTOs = (ArrayList<FileDTO>) fileDAO.getFileByEntityType(EntityTypeConstant.COMPLAIN);
		HashMap fileMapByComplainHistoryID = new HashMap<Long, FileDTO>();
		
		for (FileDTO fileDTO : fileDTOs) {
			
			if(complainHistoryMap.get(fileDTO.getDocEntityID()) != null){
				fileMapByComplainHistoryID.put(fileDTO.getDocEntityID(), fileDTO);
			}
			
		}
		

		ComplainDTO complain = (ComplainDTO) complainDAO.getAllComplainMap().get(complainID);
		
		//Permission checked. Permission for admin checked previously
		if( loginDTO.getAccountID() > 0 && complain.getClientID() != loginDTO.getAccountID() ){
		
			throw new RequestFailureException( "You don't have permission to see this page" );
		}
		
		if (complain.getSubjectID() > 0) {
			String complainSubject = complainDAO.getAllComplainSubjectMap().get(complain.getSubjectID()).getCsName();
			request.setAttribute("complainSubject", complainSubject);
		}
		request.setAttribute("complainHistoryAll", complainHistoryDTOs);
		request.setAttribute("complain", complain);
		request.setAttribute("histoyFile", fileMapByComplainHistoryID);

		return "view";
	}

}
