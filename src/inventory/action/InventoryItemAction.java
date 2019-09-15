package inventory.action;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import common.CommonActionStatusDTO;
import inventory.InventoryItem;
import inventory.InventoryService;
import inventory.form.InventoryItemDetailsForm;
import login.LoginDTO;
import login.PermissionConstants;
import sessionmanager.SessionConstants;



public class InventoryItemAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;
	InventoryService inventoryService= new InventoryService();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		
		if (request.getMethod().equalsIgnoreCase("get")) {
			return handleGet(mapping, form, request, response);
		} else {
			return handlePost(mapping, form, request, response);
		}
	}
	private ActionForward handleGet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String modeValue = request.getParameter("mode");
		if("editItem".equals(modeValue)){
			if(loginDTO.getMenuPermission(PermissionConstants.VPN_INVENTORY_ADD) >= PermissionConstants.PERMISSION_FULL){
				return getInventoryItem(mapping, form, request, response);
			}else {
				new CommonActionStatusDTO().setErrorMessage("You do not have permission to edit Inventory", false, request);
				return mapping.getInputForward();
			}
			
		}else{
			return defaultActionForaward(mapping, form, request, response);
		}
	}
	private ActionForward handlePost(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String modeValue = request.getParameter("mode");
		if("deleteItem".equals(modeValue)){
			return deleteInventoryItems(mapping, form, request, response);
		}else if("updateItem".equals(modeValue)){
			return updateInventoryItemDetails(mapping, form, request, response);
		}
		else{
			return defaultActionForaward(mapping, form, request, response);
		}
	}
	private ActionForward getInventoryItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long inventoryItemID= Long.parseLong(request.getParameter("itemID"));
		InventoryItem inventoryItemDTO=inventoryService.getInventoryItemByItemID(inventoryItemID);
		logger.debug("inventoryItemDTO"+ inventoryItemDTO);
		request.setAttribute("inventoryItemDTO", inventoryItemDTO);
		return mapping.findForward("edit");
	}
	private ActionForward updateInventoryItemDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InventoryItemDetailsForm inventoryItemDetailsForm = (InventoryItemDetailsForm) form;
		long inventoryItemID= Long.parseLong(request.getParameter("itemID"));
		String itemName=request.getParameter("itemName");
		
		Boolean isUsed = false;
		Long occupierEntityID = null;
		Integer occupierEntityTypeID = null;
		//Long parentItemID = (request.getParameter("parentItemID") != null) ? Long.parseLong(request.getParameter("parentItemID")) : 0;
		
		logger.debug("isUsed: "+request.getParameter("isUsed"));
		
		if(request.getParameter("isUsed") != null && request.getParameter("isUsed").trim().length() > 0) {
			isUsed = (Integer.parseInt(request.getParameter("isUsed")) == 1) ? true : false;
			if(!isUsed) {
				occupierEntityID = null;
				occupierEntityTypeID = null;
			}else {
				occupierEntityID = Long.parseLong(request.getParameter("occupierEntityID"));
				occupierEntityTypeID = Integer.parseInt(request.getParameter("occupierEntityTypeID"));
			}
		}
		
		logger.debug(inventoryItemDetailsForm);
		
		inventoryService.updateInventoryItem(inventoryItemID, itemName, isUsed, occupierEntityID, occupierEntityTypeID, inventoryItemDetailsForm.getAttributeValues(), inventoryItemDetailsForm.getAttributeNameIDs());
		
		String categoryID="";
		if(request.getParameter("inventoryCatagoryTypeID")!=null){
			categoryID=request.getParameter("inventoryCatagoryTypeID");
		}
		
		new CommonActionStatusDTO().setSuccessMessage("Inventory Item has been updated Successfully.", false, request);
		
		ActionRedirect forward = new ActionRedirect();
		forward.setPath("/SearchInventoryItemAll.do?categoryID="+categoryID);	
		forward.setRedirect(true);	
		return forward;
		
		//return mapping.findForward("success");
	}
	
	private ActionForward deleteInventoryItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String currentTab = request.getParameter("categoryID");
		try {
			
			String [] inventoryItemIDs=request.getParameterValues("deleteIDs");
			
			InventoryService inventoryService = new InventoryService();
			if(inventoryItemIDs!=null && inventoryItemIDs.length>0){
				List<String> inventoryItemList=Arrays.asList(inventoryItemIDs);
				for(String itemID: inventoryItemList){
					inventoryService.deleteInventoryItemByItemID(Long.parseLong(itemID), request);
				}
			}
			
			new CommonActionStatusDTO().setSuccessMessage("Items are deleted successfully.", false, request);
		} catch (Exception e) {
			logger.fatal("Error in deleting inventory item "+e);
		}
		
		ActionRedirect forward = new ActionRedirect();
		forward.setPath("/SearchInventoryItemAll.do?categoryID="+currentTab);
		forward.setRedirect(true);
		return forward;
	
	}
	private ActionForward defaultActionForaward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionRedirect forward = new ActionRedirect();
		forward.setPath("/SearchInventoryItemAll.do?default");
		forward.setRedirect(true);
		return forward;
	}
	
}
