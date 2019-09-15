package crm.inventory.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import crm.inventory.CRMInventoryService;
import crm.inventory.form.CRMInventoryItemDetailsForm;
import inventory.InventoryService;



public class AddInventoryItemAction extends Action {
	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("Add Inventory Action()");
		String target = "success";
		
		CRMInventoryItemDetailsForm inventoryItemForm = (CRMInventoryItemDetailsForm) form;
		//try {
		String[] values = inventoryItemForm.getAttributeValues();
		long [] valueIDs = inventoryItemForm.getAttributeNameIDs();
			
		int categoryTypeId = new Integer(request.getParameter("categoryID"));
		String itemName = request.getParameter("itemName");
		logger.debug("itemName: "+itemName+" parentItemID: "+request.getParameter("parentItemID"));
		logger.debug(inventoryItemForm);
		logger.debug(categoryTypeId);	
		String parentItemIDString = request.getParameter("parentItemID");
		Long parentItemID = (parentItemIDString==null|| parentItemIDString.trim().equals("") )?null:Long.parseLong(parentItemIDString);
		logger.debug("parentItemID " + parentItemID);
		InventoryService inventoryService = new InventoryService();
		inventoryService.addInventoryItem(categoryTypeId, parentItemID, inventoryItemForm.getAttributeValues(), inventoryItemForm.getAttributeNameIDs(),itemName);
		/*
		} catch (Exception e) {
			target = "failure";
			logger.fatal("Error inside Add vpn common charge config action "+e);
		}*/
		return mapping.findForward(target);
	}
}
