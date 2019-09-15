package role.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import login.PermissionConstants;
import permission.ActionPermissionDTO;
import permission.ActionPermissionRepository;
import permission.ColumnPermissionDTO;
import permission.PermissionDTO;
import permission.PermissionRepository;
import request.RequestStateActionRepository;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import role.RoleDTO;
import role.RoleService;
import role.form.RoleForm;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
@ActionRequestMapping("")
public class RoleAction extends AnnotatedRequestMappingAction {
	Logger logger = Logger.getLogger(getClass());
	
	@Service
    RoleService roleService;
	
	@RequestMapping(mapping="getAddRole", requestMethod = RequestMethod.GET)
	public ActionForward getAddRolePage(HttpServletRequest request, ActionMapping mapping) {
		request.setAttribute("id", "-1");
		return mapping.findForward("getAddRole");
	}
	
	@RequestMapping(mapping="AddRole", requestMethod = RequestMethod.POST)
	public ActionForward insertRole(HttpServletRequest p_request, ActionMapping p_mapping, ActionForm p_form, HttpServletResponse p_response) throws Exception {
		RoleForm roleForm = (RoleForm)p_form;
        RoleDTO roleDto = new RoleDTO();
        
        checkUserLoggedIn(p_request, p_mapping);
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute( SessionConstants.USER_LOGIN );
        
        
        boolean hasPermission = getUserPermission(loginDTO);
        if( !hasPermission ){
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}

        populateRoleDto(p_request, roleForm, roleDto);
        
        roleService.addRole(roleDto);
        CommonActionStatusDTO comStatusDTO= new CommonActionStatusDTO();
        
   
    	PermissionRepository.getInstance().reload(false);
    	ActionPermissionRepository.getInstance().reload(false);
    	String successMsg= "Role " + roleForm.getRolename() + " added successfully.";
		comStatusDTO.setSuccessMessage(successMsg, false, p_request);
		p_request.getSession(true).setAttribute(util.ConformationMessage.ROLE_ADD, successMsg);
		ActionForward actionForward = new ActionForward();
		actionForward.setPath("/GetRole.do?id=" + roleDto.getRoleID());
		actionForward.setRedirect(true);
		return actionForward;
        
	}



	private void populateRoleDto(HttpServletRequest p_request, RoleForm roleForm, RoleDTO roleDto) {
		roleDto.setRoleDescription(roleForm.getRoleDesc());
        roleDto.setRoleName(roleForm.getRolename());
        roleDto.setMaxClientPerDay(roleForm.getMaxClientPerDay());
        roleDto.setRestrictedtoOwn(roleForm.getRestrictedtoOwn());
        roleDto.setParentRoleID(1L);
        
        
        
        String menus[] = p_request.getParameterValues("Menu");
        String full[] = p_request.getParameterValues("Full");
        String modify[] = p_request.getParameterValues("Modify");
        String read[] = p_request.getParameterValues("Read");
        String saID[] = p_request.getParameterValues("saID");
        String columnIDs[] = p_request.getParameterValues("Column");
        
        
        populateRoleDtoWithMenu(roleDto, menus, full,modify, read);
        populateRoleDtoWithsaIDList(roleDto, saID);
        populateRoleDtoWithColumnIDs(roleDto, columnIDs);
        
        
        
        
        
        
	}

	private void populateRoleDtoWithColumnIDs(RoleDTO roleDto, String[] columnIDs) {
		ArrayList<ColumnPermissionDTO> columnList = new ArrayList<>();
        if(columnIDs != null)
        {
            for(int i = 0; i < columnIDs.length; i++) {
            	ColumnPermissionDTO columnPermissionDTO = new ColumnPermissionDTO();
            	columnPermissionDTO.setColumnID(Integer.parseInt(columnIDs[i]));
            	columnPermissionDTO.setRoleID(roleDto.getRoleID());
            	columnPermissionDTO.setDeleted(false);
            	columnList.add(columnPermissionDTO);
            }
        }
        roleDto.setColumnPermissionList(columnList);
	}

	private void populateRoleDtoWithsaIDList(RoleDTO roleDto, String[] saID) {
		ArrayList<ActionPermissionDTO> saIDList = new ArrayList<>();
        if(saID != null)
        {
        	for(int i = 0; i < saID.length; i++)
        	{
        		String stateAction[] = saID[i].split("_");
        		Integer saIDInt = RequestStateActionRepository.getInstance().getStateActionIDByStateReqeustID(stateAction[0], stateAction[1]);
	        	if(saIDInt!=null){
        			ActionPermissionDTO actionPermissionDTO = new ActionPermissionDTO();
	        		actionPermissionDTO.setStateActionTypeID(saIDInt);
	        		actionPermissionDTO.setRoleID(roleDto.getRoleID());
	        		saIDList.add(actionPermissionDTO);
	        	}
        	}
        }
        logger.debug("saIDList " + saIDList);
        roleDto.setActionPermissionList(saIDList);
	}

	private void populateRoleDtoWithMenu(RoleDTO roleDto, String[] menus, String[] full, String[] modify, String[] read) {
		ArrayList menuPermissionList = new ArrayList();
		if(menus != null)
        {
            for(int i = 0; i < menus.length; i++)
            {
                PermissionDTO pdto = new PermissionDTO();
                pdto.setMenuID(Integer.parseInt(menus[i]));
                pdto.setPermissionType(1);
                if(read != null)
                {
                    for(int j = 0; j < read.length; j++)
                    {
                        int id = Integer.parseInt(read[j]);
                        if(id == pdto.getMenuID())
                            pdto.setPermissionType(1);
                    }

                }
                if(modify != null)
                {
                    for(int j = 0; j < modify.length; j++)
                    {
                        int id = Integer.parseInt(modify[j]);
                        if(id == pdto.getMenuID())
                            pdto.setPermissionType(2);
                    }

                }
                if(full != null)
                {
                    for(int j = 0; j < full.length; j++)
                    {
                        int id = Integer.parseInt(full[j]);
                        if(id == pdto.getMenuID())
                            pdto.setPermissionType(3);
                    }

                }
                menuPermissionList.add(pdto);
            }

        }
		roleDto.setPermissionList(menuPermissionList);
	}

	private boolean getUserPermission(LoginDTO loginDTO) {
		if( loginDTO.getUserID()>0 ){
			if(
					loginDTO.getMenuPermission( PermissionConstants.ROLE_MANAGEMENT) != -1 
            		
              &&	( loginDTO.getMenuPermission( PermissionConstants.ROLE_ADD )>PermissionConstants.PERMISSION_READ ) 
              ){
            	
                return true;
            }
        }
		return false;
	}

	private void checkUserLoggedIn(HttpServletRequest p_request, ActionMapping p_mapping) {
		SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request)) {
        	p_mapping.findForward("login");
        }
	}
}
