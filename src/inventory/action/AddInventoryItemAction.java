package inventory.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CommonActionStatusDTO;
import inventory.InventoryItem;
import inventory.InventoryService;
import inventory.form.InventoryItemDetailsForm;



public class AddInventoryItemAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String target = "success";
		
		InventoryService inventoryService = new InventoryService();
		InventoryItemDetailsForm inventoryItemForm = (InventoryItemDetailsForm) form;
		
		int categoryTypeId = new Integer(request.getParameter("categoryID"));
		String itemName = request.getParameter("itemName");
		
		String parentItemIDString = request.getParameter("parentItemID");
		Long parentItemID = (parentItemIDString==null
				|| parentItemIDString.equals("null")
				|| parentItemIDString.trim().equals("") )?null:Long.parseLong(parentItemIDString);
		logger.debug("parentItemID " + parentItemID);
	
		InventoryItem inventoryItem = inventoryService.addInventoryItem(categoryTypeId, parentItemID, inventoryItemForm.getAttributeValues(), inventoryItemForm.getAttributeNameIDs(),itemName).get(0);
		
		request.getSession(true).setAttribute("inventoryItem", inventoryItem);
			
		request.getSession().setAttribute("currentItemTab", request.getParameter("currentItemTab"));
		
		if( request.getParameter("moduleID") != null )
			request.getSession().setAttribute( "moduleID", request.getParameter("moduleID") );
		
		new CommonActionStatusDTO().setSuccessMessage("Item is added successfully.", false, request);
		
		return mapping.findForward(target);
	}
}
