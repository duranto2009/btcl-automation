package crm.inventory.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.google.gson.Gson;

import crm.inventory.CRMInventoryCatagoryType;
import crm.inventory.CRMInventoryItem;
import crm.inventory.CRMInventoryService;

import org.apache.log4j.Logger;

class ResponseDTO{
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
			CRMInventoryService inventoryService = new CRMInventoryService();
			String categoryIDString = request.getParameter("categoryID");
			int categoryID = 0;
			if (categoryIDString != null)
				categoryID = Integer.parseInt(categoryIDString);
			String parentItemIdStr = request.getParameter("parentItemID");
			Long parentItemID = (StringUtils.isNotBlank(parentItemIdStr) ? Long.parseLong(parentItemIdStr) : null);
			String partialName = StringUtils.trim(request.getParameter("partialName"));

			logger.debug(parentItemIdStr + " " + partialName + " " + categoryID);
			ArrayList list = null;
			if (StringUtils.isBlank(partialName)) {
				list = new ArrayList<CRMInventoryCatagoryType>();
				logger.debug(parentItemID + " " + categoryID);
				HashMap<String, List<Long>> catIdMap = inventoryService
						.getAvailableChildItemsByAncestoryIDAndCategoryID(categoryID, parentItemID);
				logger.debug("catIdMap " + catIdMap);
				List<ResponseDTO> responseList = new ArrayList<ResponseDTO>();
				for(String name: catIdMap.keySet()){
					List<Long> IDList = catIdMap.get(name);
					ResponseDTO responseDTO = new ResponseDTO();
					responseDTO.name = name;
					responseDTO.IDList = IDList;
					responseList.add(responseDTO);
				}

				/*
				 * for (Entry<Integer, List<Long>> entry : catIdMap.entrySet())
				 * { InventoryCatagoryType
				 * cType=InventoryRepository.getInstance().
				 * getInventoryCatgoryTypeByCatagoryID(entry.getKey());
				 * InventoryCatagoryType newType= new InventoryCatagoryType();
				 * if(cType!=null && catIdMap.get(entry.getKey()).size()>0){
				 * logger.debug(cType);
				 * newType.setID(catIdMap.get(entry.getKey()).get(0).intValue())
				 * ; list.add(newType); } }
				 */
				response.getWriter().write(new Gson().toJson(responseList));
			} else {
				list = (ArrayList<CRMInventoryItem>) inventoryService.getCRMInventoryItemForAutoComplete(categoryID,
						parentItemID, partialName);
				response.getWriter().write(new Gson().toJson(list));
			}

		} catch (IOException e) {
			logger.fatal("Inventory auto complete Action: ", e);
		}

		return null;
	}
}
