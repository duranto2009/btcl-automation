package user;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import ip.MethodReferences;
import officialLetter.RecipientElement;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import annotation.ForwardedAction;
import common.CommonActionStatusDTO;
import common.StringUtils;
import login.LoginDTO;
import login.PermissionConstants;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import user.form.UserForm;
import util.KeyValuePair;
import util.RecordNavigationManager;
import util.ServiceDAOFactory;

@ActionRequestMapping("")
public class UserAction extends AnnotatedRequestMappingAction{
	private final String success = "success";
	private final String applicationRoot = "applicationRoot";
	@Service 
	UserService userService;
	
	@ForwardedAction
	@RequestMapping(mapping="AddUser", requestMethod=RequestMethod.POST)
	public String addUser(UserDTO userDTO, @RequestParameter("loginIPs") List<String> loginIPs
			, HttpServletRequest request) throws Exception{

		userDTO.setLoginIPs(getCommaSeparatedLoginIPs(loginIPs));
		return userAddHandler(userDTO, request);
	}
	@ForwardedAction
	@RequestMapping(mapping="GetUser", requestMethod=RequestMethod.GET)
	public String getUserByUserID(@RequestParameter("id") long userID, HttpServletRequest request
			, ActionMapping mapping) throws Exception {
		return userViewHandler(userID, request, mapping);
	}
	@ForwardedAction
	@RequestMapping(mapping="SearchUser", requestMethod=RequestMethod.All)
	public String searchUsers(HttpServletRequest request) throws Exception {
		return userSearchHandler(request);
	}
	@ForwardedAction
	@RequestMapping(mapping="ViewUser", requestMethod=RequestMethod.All)
	public String ViewUsers(HttpServletRequest request) throws Exception {
		return userSearchHandler(request);
	}
	
	@RequestMapping(mapping="UpdateUser", requestMethod=RequestMethod.POST) 
	public ActionForward updateUser(UserDTO userDTO, @RequestParameter("loginIPs") List<String> loginIPs
			, HttpServletRequest request, ActionMapping mapping) throws Exception {
			
		userDTO.setLoginIPs(getCommaSeparatedLoginIPs(loginIPs));
		return userUpdateHandler(request, userDTO, mapping);
	}
	@ForwardedAction
	@RequestMapping(mapping="DropUser", requestMethod = RequestMethod.POST) 
	public String dropUsers(HttpServletRequest request, @RequestParameter("deleteIDs") List<Long> deleteIDs) throws Exception {
		return userDropHandler(request, deleteIDs);
	}
	
	private String getCommaSeparatedLoginIPs(List<String> loginIPs) {
		String commaSeparatedIPs = "";
        for(int i=0;i<loginIPs.size(); i++) {
        	if(i==0) {
        		commaSeparatedIPs += loginIPs.get(i);
        	}else {
        		commaSeparatedIPs += "," +loginIPs.get(i);
        	}
        }
		return commaSeparatedIPs;
	}
	
	private String userDropHandler(HttpServletRequest request, List<Long> deleteIDs) throws Exception {
        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		
        LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        boolean hasPermission = false;
		if(loginDTO.getUserID()>0){
		    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
		      &&(loginDTO.getMenuPermission(PermissionConstants.USER_ADD) >= PermissionConstants.PERMISSION_FULL)){
		        hasPermission=true;
		    }
		}
		if( !hasPermission ){
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return  applicationRoot;
		}
        userService.dropUser(deleteIDs);
        UserRepository.getInstance().reload(false);
        String successMsg= "User deleted successfully.";
     	commonActionStatusDTO.setSuccessMessage(successMsg, false, request);
        
        return success;
	}
	
	private ActionForward userUpdateHandler(HttpServletRequest request, UserDTO userDTO, ActionMapping mapping) throws Exception {
		LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        
        boolean hasPermission = false;
        if(loginDTO.getUserID()>0){
    	    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
    	      &&(loginDTO.getMenuPermission(PermissionConstants.USER_ADD) >= PermissionConstants.PERMISSION_MODIFY)){
    	        hasPermission=true;
    	    }
    	}
		if( !hasPermission ){
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return mapping.findForward( "applicationRoot" );
		}
        userService.updateUser(userDTO, loginDTO);
        UserRepository.getInstance().reload(false);
        
       
        String successMsg="User information for " + userDTO.getUsername() + " updated successfully.";
    	request.getSession(true).setAttribute(util.ConformationMessage.USER_EDIT, successMsg);
      	commonActionStatusDTO.setSuccessMessage(successMsg, false, request);
      	
      	
      	String actionPath = "/GetUser.do?id=" + userDTO.getUserID();
      	request.getSession(true).setAttribute("LoginIPList", StringUtils.trim(userDTO.getLoginIPs()).split(",",-1));
      	ActionForward actionForward = new ActionForward();
      	actionForward.setPath(actionPath);
      	actionForward.setRedirect(true);
      	return actionForward;
	}
	
	private String userViewHandler(long id, HttpServletRequest request, ActionMapping mapping) throws Exception {
		LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        
        boolean hasPermission = false;
		if(loginDTO.getUserID()>0){
		    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
		      &&(loginDTO.getMenuPermission(PermissionConstants.USER_ADD) >= PermissionConstants.PERMISSION_MODIFY)){
		        hasPermission=true;
		    }
		}
		if( !hasPermission ){
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return applicationRoot;
		}
        UserDTO userDTO = userService.getUserDTOByUserID(id);
        UserForm userForm = userService.getUserFormByUserDTO(userDTO);
        request.getSession(true).setAttribute("LoginIPList", StringUtils.trim(userDTO.getLoginIPs()).split(",",-1));
        request.setAttribute(mapping.getAttribute(), userForm);
        return success;
    }
	
	private String userAddHandler(UserDTO userDTO, HttpServletRequest request) throws Exception {
        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        
        LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        boolean hasPermission = false;
        if(loginDTO.getUserID()>0){
    	    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
    	      &&(loginDTO.getMenuPermission(PermissionConstants.USER_ADD) >= PermissionConstants.PERMISSION_FULL)){
    	        hasPermission=true;
    	    }
    	}
		if( !hasPermission ){
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return  applicationRoot ;
		}
		userService.addUser(userDTO, loginDTO);
		
		String successMsg= "User  " + userDTO.getUsername() + " added successfully.";
    	request.getSession(true).setAttribute(util.ConformationMessage.USER_ADD,successMsg );
     	commonActionStatusDTO.setSuccessMessage(successMsg, false, request);
		
        return success;
    }
	private String userSearchHandler(HttpServletRequest request) throws Exception {
        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		
        LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        boolean hasPermission = false;
		if(loginDTO.getUserID()>0){
		    if((loginDTO.getMenuPermission(PermissionConstants.USER_MANAGEMENT) !=-1)
		      &&(loginDTO.getMenuPermission(PermissionConstants.USER_SEARCH) >= PermissionConstants.PERMISSION_READ))
		        hasPermission=true;
		}
		if( !hasPermission ){
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
			return applicationRoot;
		}
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_USER, request, userService, SessionConstants.VIEW_USER, SessionConstants.SEARCH_USER);
        rnManager.doJob(loginDTO);
        return success;
	}

	//get user by partialName
	@RequestMapping(mapping="get-user", requestMethod=RequestMethod.All)
	public List<RecipientElement> getRecipient(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
		return userService.getUserDTOsByPartialName(partialName);
	}

	@RequestMapping(mapping = "get-user-by-role", requestMethod = RequestMethod.GET)
	public List<KeyValuePair<Long, String>> getUsersByRoleId(@RequestParameter("id") long roleId) {
		return UserRepository.getInstance()
				.getUsersByRoleID(roleId)
				.stream()
				.map(t->MethodReferences.newKeyValue_Long_String.apply(t.getUserID(), t.getUsername()))
				.collect(Collectors.toList());
	}

}
