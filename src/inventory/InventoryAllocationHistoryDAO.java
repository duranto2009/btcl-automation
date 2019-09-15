package inventory;
import static util.ModifiedSqlGenerator.*;
import java.util.*;


public class InventoryAllocationHistoryDAO {
	
	Class<InventoryAllocationHistory> classObject = InventoryAllocationHistory.class;
	
	public void insertItem(InventoryAllocationHistory inventoryAllocationHistory) throws Exception{
		insert(inventoryAllocationHistory);
	}

	public void updateItem(InventoryAllocationHistory inventoryAllocationHistory) throws Exception{
		updateEntity(inventoryAllocationHistory);
	}



	public List<InventoryAllocationHistory> getCurrentHistory(long itemID) throws Exception{

		return getAllObjectList(classObject, new InventoryAllocationHistoryConditionBuilder()
				.Where()
				.itemIdEquals(itemID)
				.allocatedToEquals(Long.MAX_VALUE)
				.getCondition());
	}

	List<InventoryAllocationHistory> getCurrentHistory(List<Long> itemIDs) throws Exception{

		if(itemIDs.isEmpty())return Collections.emptyList();
		return getAllObjectList(classObject, new InventoryAllocationHistoryConditionBuilder()
				.Where()
				.itemIdIn(itemIDs)
				.allocatedToEquals(Long.MAX_VALUE)
				.getCondition());
	}

    public List<InventoryAllocationHistory> getCurrentHistory(long itemID, int moduleId) throws Exception{

        return getAllObjectList(classObject, new InventoryAllocationHistoryConditionBuilder()
				.Where()
                .itemIdEquals(itemID)
                .moduleIdEquals(moduleId)
                .allocatedToEquals(Long.MAX_VALUE)
                .getCondition());
    }

    public InventoryAllocationHistory getCurrentHistory(long itemID, int moduleId, long clientId) throws Exception{

        List<InventoryAllocationHistory> list = getAllObjectList(classObject, new InventoryAllocationHistoryConditionBuilder()
				.Where()
                .itemIdEquals(itemID)
                .moduleIdEquals(moduleId)
                .clientIdEquals(clientId)
                .allocatedToEquals(Long.MAX_VALUE)
                .getCondition());

        return list.size()>0 ? list.get(0) : null;
    }
	
}
