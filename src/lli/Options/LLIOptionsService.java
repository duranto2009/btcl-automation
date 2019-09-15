package lli.Options;

import java.util.ArrayList;
import java.util.List;

import inventory.InventoryItem;
import inventory.InventoryService;
import lli.LLIDropdownPair;
import lli.LLILongTermContract;
import lli.LLILongTermService;
import requestMapping.Service;
import util.TimeConverter;

public class LLIOptionsService {

    @Service
    LLILongTermService lliLongTermService;

    @Service
    InventoryService inventoryService;

    public List<LLIDropdownPair> getLongTermContractListClientID(long clientID) throws Exception {
        List<LLIDropdownPair> list = new ArrayList<>();
        List<LLILongTermContract> longTermContractList = lliLongTermService.getActiveLLILongTermContractListByClientID(clientID);

        LLIDropdownPair LLIDropdownPair;
        String label, startDate, endDate, dateTimeFormat = "dd/MM/yyyy";
        for (LLILongTermContract lliLongTermContract : longTermContractList) {
            startDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliLongTermContract.getContractStartDate(), dateTimeFormat);
            endDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliLongTermContract.getContractEndDate(), dateTimeFormat);
            label = lliLongTermContract.getBandwidth() + " Mbps (" + startDate + " - " + endDate + ")";

            LLIDropdownPair = new LLIDropdownPair(lliLongTermContract.getID(), label);
            list.add(LLIDropdownPair);
        }

        return list;
    }

    public List<LLIDropdownPair> getInventoryItemListByNameAndCategoryIDAndParentID(String query, int categoryID, Long parentID) throws Exception {
        List<LLIDropdownPair> list = new ArrayList<>();
        List<InventoryItem> inventoryItemList = inventoryService.getInventoryItemListByItemNameCategoryIDParentID(query, categoryID, parentID);

        for (InventoryItem inventoryItem : inventoryItemList) {
            list.add(new LLIDropdownPair(inventoryItem.getID(), inventoryItem.getName()));
        }

        return list;
    }
}
