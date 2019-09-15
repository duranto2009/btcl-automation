package entity.office;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import entity.localloop.LocalLoop;
import entity.localloop.LocalLoopConditionBuilder;
import entity.localloop.LocalLoopService;
import global.GlobalService;
import inventory.InventoryService;
import requestMapping.Service;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeService {

    @DAO
    OfficeDAO OfficeDAO;
    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);

    @Service
    GlobalService globalService;

    @Transactional
    public void insertOffices(List<Office> Offices) throws Exception {
        if (Offices != null && Offices.size() > 0) {
            for (Office Office : Offices) {
                OfficeDAO.insertOffice(Office);
            }
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getLocalLoopsByOfficeId(long officeId) throws Exception {

        List<LocalLoop> localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(officeId)
                        .getCondition());

        if (localLoops.isEmpty()) {
            throw new RequestFailureException("No local loops found with office id " + officeId);
        }
        return localLoops;
    }


//    @Transactional(transactionType = TransactionType.READONLY)
//    public List<Office> getOfficesByApplicationId(long vpnApplicationId)throws Exception {
//        List<Office> list = OfficeDAO.getOfficesByApplicationId(vpnApplicationId);
//
//        for (Office office:list ) {
//            List<LocalLoop> loops=ApplicationLocalLoopService.getLocalLoopByOffice(office.getId());
//            for (LocalLoop localLoop:loops ) {
//                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopId()).getName());
//            }
//            office.setLocalLoops(loops);
//        }
//        return list;
//    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<Office> getOfficesByClientId(long clientId) throws Exception {
        List<Office> list = OfficeDAO.getOfficesByClientId(clientId);
//        List<LocalLoop> localLoops=globalService.getAllObjectListByCondition(LocalLoop.class);
//        localLoops=localLoops
//                .stream()
//                .

        for (Office office : list) {
            List<LocalLoop> loops = new ArrayList<>();
            loops = localLoopService.getLocalLoopByOffice(office.getId());
            loops=loops
                    .stream()
                    .filter( t -> t.isCompleted()==true)
                    .collect(Collectors.toList());


            if(loops.size()>0){
                localLoopService.setVolatileProperties(loops);
                office.setLocalLoops(loops);
            }

        }
        list=list
                .stream()
                .filter(o ->o.getLocalLoops()!=null)
                .collect(Collectors.toList());
        return list;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Office getOfficeById(long office) throws Exception {
        Office Office = OfficeDAO.getOfficeById(office);
        List<LocalLoop> loops = localLoopService.getLocalLoopByOffice(Office.getId());
        if (loops != null) {
            localLoopService.setVolatileProperties(loops);
//            Office.setLoops(loops);
        }
        return Office;
    }

    @Transactional
    public void insertOffice(Office Office) throws Exception {
        OfficeDAO.insertOffice(Office);
    }

    @Transactional
    public void update(Office Office) throws Exception {
        OfficeDAO.update(Office);
    }
}
