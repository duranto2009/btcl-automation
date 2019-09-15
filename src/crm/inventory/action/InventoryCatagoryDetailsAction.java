package crm.inventory.action;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import org.apache.log4j.Logger;

public class InventoryCatagoryDetailsAction extends Action{
	Logger logger = Logger.getLogger(getClass());
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String actionForwardName = "";
		if(request.getMethod().equalsIgnoreCase("get")){
			actionForwardName = handleGet(mapping, form, request, response);
		}else{
			actionForwardName = handlePost(mapping, form, request, response);
		}
		return mapping.findForward(actionForwardName);
	}
	private String handleGet(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		return getInventoryCatagoryDetails(mapping, form, request, response);
	}
	private String handlePost(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String mode = request.getParameter("mode");
		if(mode == null){
			logger.debug("Invalid request. (No mode parameter found)");
			throw new Exception("Invalid request. (No mode parameter found)");
		}else if(mode.equalsIgnoreCase("add")){
			return addAttributeNameList(mapping, form, request, response);
		}else if(mode.equals("update")){
			return updateAttributeNameList(mapping, form, request, response);
		}else{
			logger.debug("Invalid request. (Invalid mode parameter found where mode = "+mode+ ")");
			throw new Exception("Invalid request. (Invalid mode parameter found where mode = "+mode+ ")");
		}
	}
	private String addAttributeNameList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("addAttributeNameList()");
		return null;
	}
	private String updateAttributeNameList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("updateAttributeNameList()");
		return null;
	}
	private String getInventoryCatagoryDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("getInventoryCatagoryDetails()");
		return null;
	}
}
