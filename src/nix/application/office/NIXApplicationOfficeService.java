package nix.application.office;

import annotation.DAO;
import annotation.Transactional;
import inventory.InventoryConstants;
import inventory.InventoryService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.constants.NIXConstants;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;

public class NIXApplicationOfficeService {


    @DAO
    NIXApplicationOfficeDAO nixApplicationOfficeDAO;

    InventoryService inventoryService =  ServiceDAOFactory.getService(InventoryService.class);

    NIXApplicationLocalLoopService nixApplicationLocalLoopService = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);

    @Transactional
    public void insertOffices(List<NIXApplicationOffice> nixApplicationOffices) throws Exception {
        if(nixApplicationOffices!=null&& nixApplicationOffices.size()>0){
            for(NIXApplicationOffice nixApplicationOffice:nixApplicationOffices){
                nixApplicationOfficeDAO.insertOffice(nixApplicationOffice);
            }
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXApplicationOffice> getOfficesByApplicationId(long id)throws Exception {
        List<NIXApplicationOffice> list = nixApplicationOfficeDAO.getOfficesByApplicationId(id);

        for (NIXApplicationOffice office:list ) {
            List<NIXApplicationLocalLoop>loops=nixApplicationLocalLoopService.getLocalLoopByOffice(office.getId());
            for (NIXApplicationLocalLoop localLoop:loops ) {
                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopId()).getName());
            }
            office.setLoops(loops);
        }
        return list;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXApplicationOffice getOfficeById(long office) throws Exception{
        NIXApplicationOffice nixApplicationOffice = nixApplicationOfficeDAO.getOfficeById(office);
        List<NIXApplicationLocalLoop>loops=nixApplicationLocalLoopService.getLocalLoopByOffice(nixApplicationOffice.getId());
        if(loops!=null){
            for (NIXApplicationLocalLoop localLoop:loops ) {
                localLoop.setPopName(inventoryService.getInventoryItemByItemID(localLoop.getPopId()).getName());
                if(localLoop.getPortType()== InventoryConstants.PORT_FE){
                    localLoop.setPortTypeString("FE");
                }
                else if(localLoop.getPortType() == InventoryConstants.PORT_GE ){
                    localLoop.setPortTypeString("GE");
                }
                else if(localLoop.getPortType() == InventoryConstants.PORT_10GE ){
                    localLoop.setPortTypeString("TEN_GE");
                }
            }
            nixApplicationOffice.setLoops(loops);
        }
        return  nixApplicationOffice;
    }

    @Transactional
    public void insertOffice(NIXApplicationOffice nixApplicationOffice)throws Exception {
        nixApplicationOfficeDAO.insertOffice(nixApplicationOffice);
    }

    @Transactional
    public void update(NIXApplicationOffice nixApplicationOffice)throws Exception {
        nixApplicationOfficeDAO.update(nixApplicationOffice);
    }
}
