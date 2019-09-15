package crm.inventory.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import crm.inventory.CRMInventoryService;

import org.apache.log4j.Logger;

public class InventoryItemDetailsAction extends Action{
	Logger logger = Logger.getLogger(getClass());
	CRMInventoryService inventoryService = new CRMInventoryService();
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
		String mode = request.getParameter("mode");
		if(mode==null){
			throw new Exception("Invalid Request. (No mode parameter found)");
		}else if(mode.equalsIgnoreCase("viewPage")){
			return getInventoryViewPage(mapping, form, request, response);
		}else if(mode.equalsIgnoreCase("view")){
			return getInventoryItem(mapping, form, request, response);
		}
		throw new Exception("Invalid Request. (No valid mode parameter set)");
	}
	private String handlePost(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String mode = request.getParameter("mode");
		if(mode==null){
			throw new Exception("Invalid Request. (No mode parameter found)");
		}else if(mode.equalsIgnoreCase("add")){
			return addInventoryItem(mapping, form, request, response);
		}else if(mode.equalsIgnoreCase("update")){
			return updateInventoryItem(mapping, form, request, response);
		}else{
			throw new Exception("Invalid Request. (Invalid mode parameter found)");
		}
	}
	private String addInventoryItem(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("addInventoryItem()");
		//inventoryService.addInventoryItemDetails((InventoryItemDetailsForm)form);
		return null;
	}
	private String updateInventoryItem(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("updateInventoryItem()");
		//inventoryService.updateInventoryItemDetails((InventoryItemDetailsForm)form);
		return null;
	}
	private String deleteInventoryItem(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("deleteInventoryItem()");
		return null;
	}
	private String getInventoryItem(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("getInventoryItem()");
		long itemID = 0;
		//InventoryItemDetailsForm inventoryItemDetailsForm = InventoryRepository.getInstance().getInventoryItemDetailsByItemID(itemID);
		return null;
	}
	private String getInventoryViewPage(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("getInventoryViewPage()");
		return null;
	}
}
