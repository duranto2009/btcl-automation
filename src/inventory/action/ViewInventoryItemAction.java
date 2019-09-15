package inventory.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import inventory.InventoryItem;
import inventory.InventoryService;

public class ViewInventoryItemAction extends Action{
	InventoryService inventoryService = new InventoryService();
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long inventoryItemID = Long.parseLong(request.getParameter("id"));
		InventoryItem inventoryItem = inventoryService.getInventoryItemByItemID(inventoryItemID);
		if(inventoryItem != null) {
			request.setAttribute("inventoryItem", inventoryItem);
			return mapping.findForward("success");
		}else {
			return mapping.getInputForward();
		}
		
	}

}
