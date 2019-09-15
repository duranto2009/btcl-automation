package inventory;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import global.GlobalService;
import util.DateUtils;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InventoryAllocationHistoryService {
    @DAO
    private InventoryAllocationHistoryDAO inventoryAllocationHistoryDAO;

    @Transactional
    public void allocateInventoryItem(Long inventoryItemId, int moduleId, Long clientId) throws Exception {

        InventoryAllocationHistory inventoryAllocationHistory = inventoryAllocationHistoryDAO.getCurrentHistory(
                inventoryItemId,
                moduleId,
                clientId);

        if (inventoryAllocationHistory == null) {
            inventoryAllocationHistory = InventoryAllocationHistory.builder()
                    .itemId(inventoryItemId)
                    .clientId(clientId)
                    .moduleId(moduleId)
                    .build();

            inventoryAllocationHistoryDAO.insertItem(inventoryAllocationHistory);
        } else {
            inventoryAllocationHistory.setCount(inventoryAllocationHistory.getCount() + 1);
            inventoryAllocationHistoryDAO.updateItem(inventoryAllocationHistory);
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    List<InventoryAllocationHistory> getCurrentHistoryIdByInventoryIds(List<Long> ids) throws Exception {
        return inventoryAllocationHistoryDAO.getCurrentHistory(ids);
    }

    @Transactional
    public void deallocationInventoryItem(long inventoryItemId, int moduleId, long clientId) throws Exception {
        InventoryAllocationHistory inventoryAllocationHistory = inventoryAllocationHistoryDAO.getCurrentHistory(inventoryItemId, moduleId, clientId);
        if (inventoryAllocationHistory != null) {

            int count = inventoryAllocationHistory.getCount();
            inventoryAllocationHistory.setCount(--count);

            if (count == 0)
                inventoryAllocationHistory.setAllocatedTo(DateUtils.getCurrentTime());

            inventoryAllocationHistoryDAO.updateItem(inventoryAllocationHistory);
        }
    }

    @Transactional
    public void deallocationInventoryItemFromScheduler(Map<Long, Integer> allocationMap, List<Long> clientList, long moduleId) throws Exception {


        List<Long> itemIds = new ArrayList<>(allocationMap.keySet());

        List<InventoryAllocationHistory> inventoryAllocationHistories = inventoryAllocationHistoryDAO.getCurrentHistory(itemIds);
        inventoryAllocationHistories = inventoryAllocationHistories
                .stream()
                .filter(t -> clientList.contains(t.getClientId()))
                .collect(Collectors.toList());

        inventoryAllocationHistories = inventoryAllocationHistories
                .stream()
                .filter(t -> t.getModuleId() == moduleId)
                .collect(Collectors.toList());


        String appendedItemsAndCount = "";
        for (InventoryAllocationHistory inventoryAllocationHistory : inventoryAllocationHistories)
        {


            if (inventoryAllocationHistory == null) {
                throw new RequestFailureException(inventoryAllocationHistory.getItemId() + " is not allocated. So it can not be de allocated.");
            }
            int count = inventoryAllocationHistory.getCount();
            appendedItemsAndCount += inventoryAllocationHistory.getItemId() + " Previous Count :" + inventoryAllocationHistory.getCount();
            int deallocationAmount = allocationMap.getOrDefault(inventoryAllocationHistory.getItemId(), 0);

            inventoryAllocationHistory.setCount(count - deallocationAmount);

            if (inventoryAllocationHistory.getCount() <= 0)
                inventoryAllocationHistory.setAllocatedTo(DateUtils.getCurrentTime());

            try {
                inventoryAllocationHistoryDAO.updateItem(inventoryAllocationHistory);
                appendedItemsAndCount+= " Current Count : "+inventoryAllocationHistory.getCount() + " \n ";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        try
        {
            String filename= "allocationHistory.txt";
            File yourFile = new File(filename);
            yourFile.createNewFile(); // if file already exists will do nothing
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            if(appendedItemsAndCount.length()>0) {
                fw.write(appendedItemsAndCount);//appends the string to the file
            }else{
                fw.write("No inventory find to deallocate : \n ");
            }
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }


    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getUsageCountByInventoryItemByModuleId(long inventoryItemId, int moduleId) throws Exception {

        return inventoryAllocationHistoryDAO.getCurrentHistory(inventoryItemId, moduleId)
                .stream()
                .map(InventoryAllocationHistory::getCount)
                .mapToInt(Integer::intValue)
                .sum();

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getUsageCountByInventoryItem(long inventoryItemId) throws Exception {

        return inventoryAllocationHistoryDAO.getCurrentHistory(inventoryItemId)
                .stream()
                .map(InventoryAllocationHistory::getCount)
                .mapToInt(Integer::intValue)
                .sum();

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryAllocationHistory> getUsageById(long itemId) throws Exception {
        return ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                InventoryAllocationHistory.class, new InventoryAllocationHistoryConditionBuilder()
                .Where()
                .itemIdEquals(itemId)
                .getCondition()
        );
    }


}
