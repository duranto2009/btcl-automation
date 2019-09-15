package nix.common;

import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.LLIDropdownPair;
import nix.common.NIXOptionService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@ActionRequestMapping("nix/inventory")
public class NIXInventoryAction extends AnnotatedRequestMappingAction {
    @Service
    InventoryService inventoryService;
    @Service
    NIXOptionService nixOptionService;

    @RequestMapping(mapping="/get-inventory-options", requestMethod= RequestMethod.All)
    public List<LLIDropdownPair> getInventoryOptions(@RequestParameter("query") String query, @RequestParameter("categoryID") int categoryID, @RequestParameter("parentID") Long parentID) throws Exception {
        return nixOptionService.getInventoryItemListByNameAndCategoryIDAndParentID(query, categoryID, parentID);
    }

    @RequestMapping(mapping="/get-inventory-details-upto-pop", requestMethod=RequestMethod.All)
    public Map<Integer, InventoryItem> getInventoryDetailsUptoPop(@RequestParameter("inventoryID") long inventoryID) throws Exception {
        return inventoryService.getInventoryParentItemPathMapByCategoryTypeIDAndItemID(InventoryConstants.CATEGORY_ID_POP, inventoryID);
    }
}
