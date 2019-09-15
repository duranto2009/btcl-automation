package role.action;

import java.util.ArrayList;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import login.PermissionConstants;
import permission.ActionPermissionDTO;
import permission.PermissionDTO;
import request.RequestStateActionRepository;
import role.*;
import role.form.RoleForm;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.DAOResult;

public class AddRoleAction extends Action
{

    public AddRoleAction()
    {
    }

    Logger logger = Logger.getLogger(getClass());
    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws Exception
    {
        String target = "success";
        RoleForm form = (RoleForm)p_form;
        RoleDTO dto = new RoleDTO();
        RoleService service = util.ServiceDAOFactory.getService(RoleService.class);
        SessionManager sessionManager = new SessionManager();
        if(!sessionManager.isLoggedIn(p_request))
            return p_mapping.findForward("login");
        
        boolean hasPermission = false;
        
        LoginDTO loginDTO = (LoginDTO)p_request.getSession(true).getAttribute( SessionConstants.USER_LOGIN );
        
        if( loginDTO.getUserID()>0 )
        {
            if(loginDTO.getMenuPermission( PermissionConstants.ROLE_MANAGEMENT) != -1 
            		
              &&( loginDTO.getMenuPermission( PermissionConstants.ROLE_ADD )>PermissionConstants.PERMISSION_READ ) ){
            	
                hasPermission=true;
            }
        }
        
        if( !hasPermission ){
			
			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
			
			commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, p_request );
			return p_mapping.findForward( "applicationRoot" );
		}
        
        logger.debug("form: " + form);
        dto.setRoleDescription(form.getRoleDesc());
        dto.setRoleName(form.getRolename());
        dto.setMaxClientPerDay(form.getMaxClientPerDay());
        dto.setRestrictedtoOwn(form.getRestrictedtoOwn());
        
        
        ArrayList menuPermissionList = new ArrayList();
        String menus[] = p_request.getParameterValues("Menu");
        String full[] = p_request.getParameterValues("Full");
        String modify[] = p_request.getParameterValues("Modify");
        String read[] = p_request.getParameterValues("Read");
        String saID[] = p_request.getParameterValues("saID");

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
        dto.setPermissionList(menuPermissionList);
        
        ArrayList<ActionPermissionDTO> saIDList = new ArrayList<>();
        if(saID != null)
        {
        	for(int i = 0; i < saID.length; i++)
        	{
        		String stateAction[] = saID[i].split("_");
        		int saIDInt = RequestStateActionRepository.getInstance().getStateActionIDByStateReqeustID(stateAction[0], stateAction[1]);
        		ActionPermissionDTO actionPermissionDTO = new ActionPermissionDTO();
        		actionPermissionDTO.setStateActionTypeID(saIDInt);
        		actionPermissionDTO.setRoleID(form.getRoleID());
        		saIDList.add(actionPermissionDTO);
        	}
        }
        logger.debug("saIDList " + saIDList);
        dto.setActionPermissionList(saIDList);
        
        
        String columnIDs[] = p_request.getParameterValues("Column");
        ArrayList columnList = new ArrayList();
        if(columnIDs != null)
        {
            for(int i = 0; i < columnIDs.length; i++)
                columnList.add(columnIDs[i]);

        }
        dto.setColumnPermissionList(columnList);
        service.addRole(dto);
        CommonActionStatusDTO comStatusDTO= new CommonActionStatusDTO();
        
    
    	String successMsg= "Role " + form.getRolename() + " added successfully.";
    	comStatusDTO.setSuccessMessage(successMsg, false, p_request);
    	
    	p_request.getSession(true).setAttribute(util.ConformationMessage.ROLE_ADD, successMsg);
        
        return p_mapping.findForward(target);
    }
}