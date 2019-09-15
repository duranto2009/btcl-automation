package vpn.ownerchange;

import annotation.Transactional;
import exception.NoDataFoundException;
import global.GlobalService;
import requestMapping.Service;
import util.TransactionType;

import java.util.List;

public class VPNOnProcessLinkService {

    @Service private GlobalService globalService;

    @Transactional(transactionType = TransactionType.READONLY)
    public VPNOnProcessLink getOnProcessConnectionByLink(long linkId) throws Exception {
        return globalService.getAllObjectListByCondition(VPNOnProcessLink.class,
                new VPNOnProcessLinkConditionBuilder()
                .Where()
                .linkEquals(linkId)
                .getCondition()
        )
                .stream()
                .findFirst()
                .orElseThrow(()->new NoDataFoundException("No On Process Link Found with link Id " + linkId));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNOnProcessLink> getOnProcessLinksByApplication(long vpnApplicationId) throws Exception{
        return globalService.getAllObjectListByCondition(
                VPNOnProcessLink.class, new VPNOnProcessLinkConditionBuilder()
                        .Where()
                        .applicationEquals(vpnApplicationId)
                        .getCondition()
        );
    }
}
