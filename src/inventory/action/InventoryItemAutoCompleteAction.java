package inventory.action;

import com.google.gson.Gson;
import inventory.InventoryItem;
import inventory.InventoryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.SQLInjectionEscaper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ResponseDTO {
	public String name;
	public List<Long> IDList = new ArrayList<Long>();
}

public class InventoryItemAutoCompleteAction extends Action {

	Logger logger = Logger.getLogger(getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("AutoInventoryAction() called");

		response.setContentType("application/json");
		try {
			InventoryService inventoryService = new InventoryService();

			Integer categoryID = (StringUtils.isNotBlank(request.getParameter("categoryID"))?Integer.parseInt(request.getParameter("categoryID")):null);
			Long parentItemID = (StringUtils.isNotBlank(request.getParameter("parentItemID")) ? Long.parseLong(request.getParameter("parentItemID")) : null);
			String partialName =  SQLInjectionEscaper.escapeString(request.getParameter("partialName"), true).trim() ;

			String attributeName = (StringUtils.isNotBlank(request.getParameter("attributeName"))?request.getParameter("attributeName"):null);
			String attributeValue = (StringUtils.isNotBlank(request.getParameter("attributeValue"))?request.getParameter("attributeValue"):null);
			
			Boolean onlyUnused = (StringUtils.isNotBlank(request.getParameter("onlyUnused")) ? ( request.getParameter("onlyUnused").equals("false") ? false : true ) : false);
			Boolean isParentNeeded = (StringUtils.isNotBlank(request.getParameter("isParentNeeded")) ? ( request.getParameter("isParentNeeded").equals("false") ? false : true ) : true);
			List<InventoryItem> inventoryItemList = new ArrayList<InventoryItem>();
			
			inventoryItemList = inventoryService.getInventoryItemListForAutocomplete(categoryID, parentItemID, partialName, attributeName, attributeValue, onlyUnused, isParentNeeded);
			
			/*
			if (categoryID.equals(new Integer(CategoryConstants.CATEGORY_ID_PORT))) {
				//Gets Port currently used by VPN Link
				Long vpnLinkID = (StringUtils.isNotBlank(request.getParameter("vpnLinkID"))?Long.parseLong(request.getParameter("vpnLinkID")):null);
				if(vpnLinkID != null) {
					VpnLinkService vpnLinkService = new VpnLinkService();
					VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID(vpnLinkID);
					VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
					VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
					
					Long nearEndPortID = vpnNearEndDTO.getPortID();
					Long farEndPortID = vpnFarEndDTO.getPortID();
					
					InventoryItem nearEndPort = inventoryService.getInventoryItemByItemID(nearEndPortID);
					InventoryItem farEndPort = inventoryService.getInventoryItemByItemID(farEndPortID);
					
					InventoryItemDetails nearEndPortDetails = inventoryService.getInventoryItemDetailsByItemID(nearEndPortID);
					InventoryItemDetails farEndPortDetails = inventoryService.getInventoryItemDetailsByItemID(farEndPortID);
					
					if(nearEndPort.getParentID()!=null && nearEndPort.getParentID().equals(parentItemID) && nearEndPortDetails.getValueByAttributeName(attributeName).equals(attributeValue)) {
						nearEndPort.setName(nearEndPort.getName() + " (currently used in Near End)");
						inventoryItemList.add(nearEndPort);
					}else if(farEndPort.getParentID()!=null && farEndPort.getParentID().equals(parentItemID) && farEndPortDetails.getValueByAttributeName(attributeName).equals(attributeValue)) {
						farEndPort.setName(farEndPort.getName() + " (currently used in Far End)");
						inventoryItemList.add(farEndPort);
					}
					
					inventoryItemList.addAll(inventoryService.getPortsUsedByClient(vpnLinkDTO.getClientID(), parentItemID, attributeValue));
				}
				
				//Gets Port currently used by LLI Link
				Long lliLinkID = (StringUtils.isNotBlank(request.getParameter("lliLinkID"))?Long.parseLong(request.getParameter("lliLinkID")):null);
				if(lliLinkID != null) {
					LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
					LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID(lliLinkID);
					LliFarEndDTO lliFarEndDTO = lliLinkService.getFarEndByFarEndID(lliLinkDTO.getFarEndID());
					
					Long farEndPortID = lliFarEndDTO.getPortID();
					
					InventoryItem farEndPort = inventoryService.getInventoryItemByItemID(farEndPortID);
					InventoryItemDetails farEndPortDetails = inventoryService.getInventoryItemDetailsByItemID(farEndPortID);
					
					if(farEndPort.getParentID()!=null && farEndPort.getParentID().equals(parentItemID) && farEndPortDetails.getValueByAttributeName(attributeName).equals(attributeValue)) {
						farEndPort.setName(farEndPort.getName() + " (currently used)");
						inventoryItemList.add(farEndPort);
					}
					
					inventoryItemList.addAll(inventoryService.getPortsUsedByClient(lliLinkDTO.getClientID(), parentItemID, attributeValue));
				}
			}
			*/
			
			response.getWriter().write(new Gson().toJson(inventoryItemList));
		} catch (IOException e) {
			logger.fatal("Inventory auto complete Action: ", e);
		}
		return null;
	}
}
