package nix.common;

import annotation.Transactional;
import inventory.InventoryItem;
import inventory.InventoryService;
import lli.LLIDropdownPair;
import requestMapping.Service;

import java.util.ArrayList;
import java.util.List;

public class NIXOptionService {


    @Service
    InventoryService inventoryService;


    @Transactional
    public List<LLIDropdownPair> getInventoryItemListByNameAndCategoryIDAndParentID(String query, int categoryID, Long parentID) throws Exception {
        List<LLIDropdownPair> list = new ArrayList<>();
        List<InventoryItem> inventoryItemList = inventoryService.getInventoryItemListByItemNameCategoryIDParentID(query, categoryID, parentID);

        for (InventoryItem inventoryItem : inventoryItemList) {
            list.add(new LLIDropdownPair(inventoryItem.getID(), inventoryItem.getName()));
        }

        return list;
    }
}
