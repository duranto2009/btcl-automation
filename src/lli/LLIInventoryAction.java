package lli;

import java.util.List;
import java.util.Map;

import inventory.*;
import lli.Options.LLIOptionsService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.ServiceDAOFactory;

@ActionRequestMapping("lli/inventory")
public class LLIInventoryAction extends AnnotatedRequestMappingAction {

	@Service private LLIOptionsService lliOptionsService;
	@Service private InventoryService inventoryService;
	
	@RequestMapping(mapping="/get-inventory-options", requestMethod=RequestMethod.All)
	public List<LLIDropdownPair> getInventoryOptions(@RequestParameter("query") String query, @RequestParameter("categoryID") int categoryID, @RequestParameter("parentID") Long parentID) throws Exception {
		return lliOptionsService.getInventoryItemListByNameAndCategoryIDAndParentID(query, categoryID, parentID);
	}
	
	@RequestMapping(mapping="/get-inventory-details-upto-pop", requestMethod=RequestMethod.All)
	public Map<Integer, InventoryItem> getInventoryDetailsUptoPop(@RequestParameter("inventoryID") long inventoryID) throws Exception {
		return inventoryService.getInventoryParentItemPathMapByCategoryTypeIDAndItemID(InventoryConstants.CATEGORY_ID_POP, inventoryID);
	}

	@RequestMapping(mapping = "/get-usage", requestMethod = RequestMethod.GET)
	public List <InventoryAllocationHistory > getUsage (@RequestParameter("itemId") long itemId) throws Exception {
		return ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).getUsageById(itemId);
	}

	@RequestMapping(mapping = "/get-item-name", requestMethod = RequestMethod.GET)
	public InventoryItem getInventoryName (@RequestParameter("itemId") long itemId) {
		return ServiceDAOFactory.getService(InventoryService.class).getInventoryItemByItemID(itemId);
	}
	
}
