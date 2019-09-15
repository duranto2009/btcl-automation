package lli.Application.newOffice;

import inventory.InventoryService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.Office.OfficeConditionBuilder;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;
import static util.ModifiedSqlGenerator.updateEntity;

public class NewOfficeDAO {
    NewLocalLoopService localLoopService=ServiceDAOFactory.getService(NewLocalLoopService.class);
    InventoryService inventoryService=ServiceDAOFactory.getService(InventoryService.class);

    public List<NewOffice> getOffice(long applicatioID) throws Exception{


        List<NewOffice> offices= ModifiedSqlGenerator.getAllObjectList(NewOffice.class,
                    new NewOfficeConditionBuilder()
                            .Where()
                            .applicationIdEquals(applicatioID)
                            .getCondition()
            );

        for (NewOffice office:offices) {
            List<NewLocalLoop>loops=localLoopService.getLocalLoopByOfficeAndApplication(office.getId(),applicatioID);
            for (NewLocalLoop newlocalLoop:loops) {


                newlocalLoop.setPopName(inventoryService.getInventoryItemByItemID(newlocalLoop.getPopID()).getName());

            }
            office.setLoops(loops);

        }
        return offices;
    }

    public List<NewOffice> getOfficeByCon(long conID) throws Exception{


        List<NewOffice> offices= ModifiedSqlGenerator.getAllObjectList(NewOffice.class,
                new NewOfficeConditionBuilder()
                        .Where()
                        .connectionIDEquals(conID)
                        .getCondition()
        );

        for (NewOffice office:offices
                ) {

            List<NewLocalLoop>loops=localLoopService.getLocalLoopByOffice(office.getId());
            for (NewLocalLoop newlocalLoop:loops
                 ) {

                newlocalLoop.setPopName(inventoryService.getInventoryItemByItemID(newlocalLoop.getPopID()).getName());

            }
            office.setLoops(loops);

        }
        return offices;




    }
    public void insertOffice(NewOffice office) throws Exception{
        insert(office);
    }
    public void updateOffice(NewOffice office) throws Exception{
        updateEntity(office);
    }

    public List<NewOffice> getOfficeById(long officeId) throws Exception {


        List<NewOffice> offices = ModifiedSqlGenerator.getAllObjectList(NewOffice.class,
                new NewOfficeConditionBuilder()
                        .Where()
                        .idEquals(officeId)

                        .getCondition()
        );

        for (NewOffice office : offices
                ) {

            List<NewLocalLoop> loops = localLoopService.getLocalLoopByOffice(office.getId());
            for (NewLocalLoop newlocalLoop:loops
            ) {

                newlocalLoop.setPopName(inventoryService.getInventoryItemByItemID(newlocalLoop.getPopID()).getName());

            }
            office.setLoops(loops);

        }
        return offices;


    }


    public NewOffice getOneOfficeById(long officeId)throws Exception {

        NewOffice newOffice = ModifiedSqlGenerator.getObjectByID(NewOffice.class,officeId);
        return newOffice;
    }

    public List<NewOffice> getOfficeByApplication(long applicationID) throws Exception {
        List<NewOffice> offices = ModifiedSqlGenerator.getAllObjectList(NewOffice.class,
                new NewOfficeConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationID)

                        .getCondition()
        );
        return  offices;
    }
}
