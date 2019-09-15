package role.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CommonActionStatusDTO;
import login.LoginDTO;
import login.PermissionConstants;
import permission.ActionPermissionDTO;
import permission.ColumnPermissionDTO;
import permission.PermissionDTO;
import permission.PermissionRepository;
import request.RequestStateActionRepository;
import role.RoleDTO;
import role.RoleService;
import role.form.RoleForm;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.ServiceDAOFactory;

public class UpdateRoleAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		RoleForm form = (RoleForm) p_form;
		RoleDTO dto = new RoleDTO();
		RoleService service = ServiceDAOFactory.getService(RoleService.class);
		SessionManager sessionManager = new SessionManager();
		if (!sessionManager.isLoggedIn(p_request))
			return p_mapping.findForward("login");

		boolean hasPermission = false;
		LoginDTO loginDTO = (LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if (loginDTO.getUserID() > 0) {
			if ((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) != -1)

					&& (loginDTO.getMenuPermission(
							PermissionConstants.ROLE_ADD) >= PermissionConstants.PERMISSION_MODIFY)) {

				hasPermission = true;
			}
		}

		if (!hasPermission) {

			CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();

			commonActionStatusDTO.setErrorMessage("You don't have permission to see this page", false, p_request);
			return p_mapping.findForward("applicationRoot");
		}

		dto.setRoleID(form.getRoleID());
		dto.setRoleName(form.getRolename());
		dto.setRoleDescription(form.getRoleDesc());
		dto.setMaxClientPerDay(form.getMaxClientPerDay());
		dto.setRestrictedtoOwn(form.getRestrictedtoOwn());
		dto.setParentRoleID(form.getParentRoleID());
		ArrayList<PermissionDTO> menuPermissionList = new ArrayList<PermissionDTO>();
		String menus[] = p_request.getParameterValues("Menu");
		String full[] = p_request.getParameterValues("Full");
		String modify[] = p_request.getParameterValues("Modify");
		String read[] = p_request.getParameterValues("Read");
		String saID[] = p_request.getParameterValues("saID");
		logger.debug("menus " + menus);
		if (menus != null) {
			for (int i = 0; i < menus.length; i++) {
				PermissionDTO pdto = new PermissionDTO();
				pdto.setPermissionType(1);
				pdto.setMenuID(Integer.parseInt(menus[i]));
				if (read != null) {
					for (int j = 0; j < read.length; j++) {
						int id = Integer.parseInt(read[j]);
						if (id == pdto.getMenuID())
							pdto.setPermissionType(1);
					}

				}
				if (modify != null) {
					for (int j = 0; j < modify.length; j++) {
						int id = Integer.parseInt(modify[j]);
						if (id == pdto.getMenuID())
							pdto.setPermissionType(2);
					}

				}
				if (full != null) {
					for (int j = 0; j < full.length; j++) {
						int id = Integer.parseInt(full[j]);
						if (id == pdto.getMenuID())
							pdto.setPermissionType(3);
					}

				}
				pdto.setRoleID(form.getRoleID());
				menuPermissionList.add(pdto);
			}

		}
		dto.setPermissionList(menuPermissionList);

		ArrayList<ActionPermissionDTO> saIDList = new ArrayList<>();
		if (saID != null) {
			for (int i = 0; i < saID.length; i++) {
				String stateAction[] = saID[i].split("_");
				Integer saIDInt = RequestStateActionRepository.getInstance()
						.getStateActionIDByStateReqeustID(stateAction[0], stateAction[1]);

				if (saIDInt != null) {
					ActionPermissionDTO actionPermissionDTO = new ActionPermissionDTO();
					actionPermissionDTO.setStateActionTypeID(saIDInt);
					actionPermissionDTO.setRoleID(form.getRoleID());
					saIDList.add(actionPermissionDTO);
				}
			}
		}
		logger.debug("saIDList " + saIDList);
		dto.setActionPermissionList(saIDList);

		String columnIDs[] = p_request.getParameterValues("Column");
		ArrayList<ColumnPermissionDTO> columnPermissionList = new ArrayList<>();
		if (columnIDs != null) {
			for (int i = 0; i < columnIDs.length; i++) {
				ColumnPermissionDTO columnPermissionDTO = new ColumnPermissionDTO();
				columnPermissionDTO.setColumnID(Integer.parseInt(columnIDs[i]));
				columnPermissionDTO.setRoleID(form.getRoleID());
				columnPermissionList.add(columnPermissionDTO);
			}

		}
		dto.setColumnPermissionList(columnPermissionList);
		service.updateRole(dto);
		PermissionRepository.getInstance().reload(false);
		CommonActionStatusDTO comStatusDTO = new CommonActionStatusDTO();

		String successMsg = "Role information for " + dto.getRoleName() + " updated successfully.";
		p_request.getSession(true).setAttribute(util.ConformationMessage.ROLE_EDIT, successMsg);
		comStatusDTO.setSuccessMessage(successMsg, false, p_request);
		ActionForward af = new ActionForward();
		af.setPath("GetRole.do?id=" + dto.getRoleID());
		af.setRedirect(true);
		return af;

	}
}