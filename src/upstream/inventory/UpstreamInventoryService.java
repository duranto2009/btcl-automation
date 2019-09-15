package upstream.inventory;

import annotation.Transactional;
import common.ObjectPair;
import common.RequestFailureException;
import global.GlobalService;
import inventory.InventoryConstants;
import requestMapping.Service;
import upstream.UpstreamConstants;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpstreamInventoryService {

    @Service
    GlobalService globalService;


    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamInventoryItem> getAllUpstreamItemsByItemType(String itemType) throws Exception {
        List<UpstreamInventoryItem> items = globalService
                .getAllObjectListByCondition(UpstreamInventoryItem.class,
                        new UpstreamInventoryItemConditionBuilder()
                                .Where()
//                                .itemTypeBothLike(itemType)
                                .itemTypeLike(itemType)
                                .getCondition());


        if (items.isEmpty()) {
            throw new RequestFailureException("No items found in inventory of type " + itemType);
        }

        return items;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<UpstreamConstants.INVENTORY_ITEM_TYPE, List<UpstreamInventoryItem>> getAllUpstreamItems() throws Exception{
        return globalService
                .getAllObjectListByCondition(UpstreamInventoryItem.class,
                    new UpstreamInventoryItemConditionBuilder()
                        .Where()
                        .getCondition())
                .stream()
                .collect(Collectors.groupingBy(UpstreamInventoryItem::getItemType, Collectors.mapping(Function.identity(),Collectors.toList())));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, UpstreamInventoryItem> getAllUpstreamItemsMapById() throws Exception{
        return globalService
                .getAllObjectListByCondition(UpstreamInventoryItem.class,
                        new UpstreamInventoryItemConditionBuilder()
                                .Where()
                                .getCondition())
                .stream()
                .collect(Collectors.toMap(UpstreamInventoryItem::getId, Function.identity()));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public UpstreamInventoryItem getUpstreamInventoryItemsByIdAndItemType(long id,String itemType) throws Exception {
        List<UpstreamInventoryItem> items = globalService
                .getAllObjectListByCondition(UpstreamInventoryItem.class,
                        new UpstreamInventoryItemConditionBuilder()
                                .Where()
                                .itemTypeBothLike(itemType)
                                .idEquals(id)
                                .getCondition());


        if (items.isEmpty()) {
//            throw new RequestFailureException("No items found in inventory of type " + itemType);
            return null;
        }

        return items.get(0);
    }


    @Transactional
    public void addNewItem(String itemType, String itemName) throws Exception {
        UpstreamInventoryItem item = new UpstreamInventoryItem();

        item.setItemType(UpstreamConstants.INVENTORY_ITEM_TYPE.valueOf(itemType));
        item.setItemName(itemName);

        globalService.save(item);
    }


    //method for UI calling
    public List<ObjectPair<Long, String>> getAllUpstreamItemsInPairByItemType(String itemType) throws Exception {
        List<UpstreamInventoryItem> items = getAllUpstreamItemsByItemType(itemType);

        List<ObjectPair<Long, String>> objectPairs = new ArrayList<>();

        for (UpstreamInventoryItem item : items) {
            objectPairs.add(new ObjectPair<>(item.getId(), item.getItemName()));
        }

        return objectPairs;
    }


//    public static void main(String[] args) throws Exception {
//        System.out.println(ServiceDAOFactory.getService(UpstreamInventoryService.class).getAllUpstreamItemsByItemType("BTCL_SERVICE_LOCATION"));
//    }
}
