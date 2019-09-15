package scheduler.entity;

import common.ModuleConstants;
import global.GlobalService;
import inventory.InventoryAllocationHistoryService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIConnectionConditionBuilder;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopConditionBuilder;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeConditionBuilder;
import lli.connection.LLIConnectionConstants;
import util.ServiceDAOFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class InventoryTasks {

    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
    InventoryAllocationHistoryService inventoryAllocationHistoryService=ServiceDAOFactory.getService(InventoryAllocationHistoryService.class);


    public void print(){
        System.out.println("kj=agjnd");
    }
    public void deAllocateInventoryForLLI() throws Exception {
        long currentTime = System.currentTimeMillis();
        long durationInMillis = 24 * 60 * 60 * 1000;
        long dayBefore = currentTime - durationInMillis;
        Map<Long, List<LLIConnection>> lliConnectionList = globalService.getAllObjectListByCondition(LLIConnection.class,
                new LLIConnectionConditionBuilder()
                        .Where()
                        .activeToGreaterThan(dayBefore)
                        .activeToLessThan(System.currentTimeMillis())
                        .connectionTypeEquals(LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY)
                        .getCondition())
                .stream()
                .collect(Collectors.groupingBy(LLIConnection::getID));


        List<Long> mappedLLIConnectionIds = lliConnectionList
                .entrySet()
                .stream()
                .map(t -> {
                            List<LLIConnection> lliConnections = t.getValue();
                            return lliConnections
                                    .stream()
                                    .max(Comparator.comparing(LLIConnection::getHistoryID))
                                    .orElse(null);
                        }
                )
                .filter(Objects::nonNull)
                .map(LLIConnection::getHistoryID)
                .collect(Collectors.toList());

        List<Long> mappedClientIds = lliConnectionList
                .entrySet()
                .stream()
                .map(t -> {
                            List<LLIConnection> lliConnections = t.getValue();
                            return lliConnections
                                    .stream()
                                    .max(Comparator.comparing(LLIConnection::getHistoryID))
                                    .orElse(null);
                        }
                )
                .filter(Objects::nonNull)
                .map(LLIConnection::getClientID)
                .collect(Collectors.toList());

        List<Long> officeIds = globalService.getAllObjectListByCondition(Office.class,
                new OfficeConditionBuilder()
                        .Where()
                        .connectionIDIn(mappedLLIConnectionIds)
                        .getCondition())
                .stream()
                .map(Office::getId)
                .collect(Collectors.toList());

       Map<Long, Integer> inventoryDeallocationCount = new HashMap<>();
        globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                .Where()
                .officeIDIn(officeIds)
                .getCondition())
                .forEach(t->{

                    if(t.getPortID()>0) {
                        int countP = inventoryDeallocationCount.getOrDefault(t.getPortID(), 0);
                        inventoryDeallocationCount.put(t.getPortID(), countP + 1);
                    }

                    if(t.getRouter_switchID()>0) {

                        int countRS = inventoryDeallocationCount.getOrDefault(t.getRouter_switchID(), 0);
                        inventoryDeallocationCount.put(t.getRouter_switchID(), countRS + 1);
                    }

                    if(t.getVLANID()>0) {
                        int countVlan = inventoryDeallocationCount.getOrDefault(t.getVLANID(), 0);
                        inventoryDeallocationCount.put(t.getVLANID(), countVlan + 1);
                    }
                });




        inventoryAllocationHistoryService.deallocationInventoryItemFromScheduler(inventoryDeallocationCount,mappedClientIds, ModuleConstants.Module_ID_LLI);

        try
        {
            String filename= "allocationHistory.txt";
            File yourFile = new File(filename);
            yourFile.createNewFile(); // if file already exists will do nothing
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write("add a line\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
        System.out.println("Done Inventory Deallocation");


    }


}
