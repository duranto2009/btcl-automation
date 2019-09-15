package nix.nixportconfig;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import inventory.InventoryConstants;
import util.TransactionType;

import java.util.List;

public class NIXPortConfigService {
    @DAO
    NIXPortConfigDAO nixPortConfigDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public NIXPortConfig getPortConfigByPortType(int portType)throws Exception{
        List<NIXPortConfig> nixPortConfigs =  nixPortConfigDAO.getPortConfigByPortType(portType);
        if(nixPortConfigs.size()>1) throw  new RequestFailureException("Multiple Configuration found for port type"+
                InventoryConstants.mapOfPortTypeToPortTypeString.get(portType));
        else if(nixPortConfigs.isEmpty()) throw  new RequestFailureException("No Configuration found for port type"+
                InventoryConstants.mapOfPortTypeToPortTypeString.get(portType));
        return nixPortConfigs.get(0);
    }

}
