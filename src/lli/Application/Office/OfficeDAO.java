package lli.Application.Office;

import inventory.InventoryService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

public class OfficeDAO {
    LocalLoopService localLoopService=ServiceDAOFactory.getService(LocalLoopService.class);
    InventoryService inventoryService=ServiceDAOFactory.getService(InventoryService.class);

    public List<Office> getOffice(long applicatioID) throws Exception{


        List<Office> offices= ModifiedSqlGenerator.getAllObjectList(Office.class,
                    new OfficeConditionBuilder()
                            .Where()
                            .applicationIdEquals(applicatioID)

                            .getCondition()
            );

        for (Office office:offices
             ) {

            List<LocalLoop>loops=localLoopService.getLocalLoopByOffice(office.getId());
            for (LocalLoop localLoop:loops
            ) {
                

                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());

            }
            office.setLoops(loops);

        }
        return offices;
    }

    public List<Office> getLastCnnectionOfficeByCon(long conID) throws Exception {


        List<Office> offices = ModifiedSqlGenerator.getAllObjectList(Office.class,
                new OfficeConditionBuilder()
                        .Where()
                        .connectionIDEquals(conID)

                        .getCondition()
        );

        for (Office office : offices) {

            List<LocalLoop> loops = localLoopService.getLocalLoopByOffice(office.getId());
            for (LocalLoop localLoop : loops) {
                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());
            }
            office.setLoops(loops);

        }
        return offices;
    }

    public List<Office> getOfficeByCon(long conID) throws Exception{


        List<Office> offices= ModifiedSqlGenerator.getAllObjectList(Office.class,
                new OfficeConditionBuilder()
                        .Where()
                        .connectionIDEquals(conID)
                        .getCondition()
        );

        for (Office office:offices) {

            List<LocalLoop>loops=localLoopService.getLocalLoopByOffice(office.getId());
//            for (LocalLoop localLoop:loops) {
//                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());
//            }
            office.setLoops(loops);

        }
        return offices;




    }


    public List<Office> getOfficeByConWithoutLoop(long conID) throws Exception{


        List<Office> offices= ModifiedSqlGenerator.getAllObjectList(Office.class,
                new OfficeConditionBuilder()
                        .Where()
                        .connectionIDEquals(conID)
                        .getCondition()
        );

        return offices;




    }
    public void insertOffice(Office office) throws Exception{
        insert(office);
    }
    public void updateOffice(Office office) throws Exception{
        updateEntity(office);
    }

    public List<Office> getOfficeById(long officeId) throws Exception {


        List<Office> offices = ModifiedSqlGenerator.getAllObjectList(Office.class,
                new OfficeConditionBuilder()
                        .Where()
                        .idEquals(officeId)

                        .getCondition()
        );

        for (Office office : offices
                ) {

            List<LocalLoop> loops = localLoopService.getLocalLoopByOffice(office.getId());
            for (LocalLoop localLoop:loops
            ) {

                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopID()).getName());

            }
            office.setLoops(loops);

        }
        return offices;


    }
}
