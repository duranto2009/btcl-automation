package nix.localloop;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import inventory.InventoryService;
import lli.Application.LocalLoop.LocalLoopStringProjection;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.localloop.NIXLocalLoopDAO;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.localloop.NIXLocalLoop;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

public class NIXLocalLoopService {
    @DAO
    NIXLocalLoopDAO nixLocalLoopDAO;

    NIXApplicationLocalLoopService nixApplicationLocalLoopService=ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class);

    @Transactional
    public void insertLocalLoop(NIXLocalLoop nixLocalLoop)throws Exception {
        nixLocalLoopDAO.insertLocalLoop(nixLocalLoop);
    }

    @Transactional
    public List<NIXLocalLoop> getLocalLoopsByOfficeID(long officeId)throws Exception {
        return  nixLocalLoopDAO.getLocalLoopsByOfficeID(officeId)
                .stream()
                .map(t->{
                    try {
                        t.setNixApplicationLocalLoop(nixApplicationLocalLoopService.getLocalLoopById(t.getApplicationLocalLoop()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<NIXLocalLoop>getLocalLoopsByConID(long connectionID)throws Exception{
        return nixLocalLoopDAO.getLocalLoopsByConID(connectionID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXApplicationLocalLoop>getNIXApplicationLocalLoopsByConID(long connectionID)throws Exception{
        return nixLocalLoopDAO.getApplicationLocalLoopsByConID(connectionID);
    }

    public static LocalLoopStringProjection getLocalLoopProjection(NIXApplicationLocalLoop localLoop) {
        InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
        String popName = localLoop.getPopId() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getPopId()).getName();
        String router_switch_name = localLoop.getRouterSwitchId() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getRouterSwitchId()).getName();
        String portName = localLoop.getPortId() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getPortId()).getName();
        String vlanName = localLoop.getVlanId() == 0? "N/A":inventoryService.getInventoryItemByItemID(localLoop.getVlanId()).getName();
        String ocName = localLoop.getVendor() == 0 ? "N/A": UserRepository.getInstance().getUserDTOByUserID(localLoop.getVendor()).getUsername();
        long btclDistance = localLoop.getBtclDistance();
        long ocDistance = localLoop.getOcdDistance();
        long clientDistance = localLoop.getClientDistance();
        return LocalLoopStringProjection.builder()
                .popName(popName)
                .portName(portName)
                .switchOrRouterName(router_switch_name)
                .vlanName(vlanName)
                .ocName(ocName)
                .btclDistance(btclDistance)
                .clientDistance(clientDistance)
                .ocDistance(ocDistance)
                .build();

    }

    @Transactional
    public NIXLocalLoop getLocalLoopByApplicationID(long id)throws Exception {
        List<NIXLocalLoop>nixLocalLoops=  nixLocalLoopDAO.getLocalLoopByApplicationID(id);
        if(nixLocalLoops!=null&&nixLocalLoops.size()>1){
            new RequestFailureException("local loop is not unique");
        }
        return nixLocalLoops.get(0);
    }
}
