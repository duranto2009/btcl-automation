package ip.subRegion;

import annotation.DAO;
import annotation.Transactional;
import common.ObjectPair;
import util.CurrentTimeFactory;
import util.TransactionType;

import java.util.List;

public class IPSubRegionService {

    @DAO
    IPSubRegionDAO ipSubRegionDAO;

    @Transactional
    public void insertNewSubRegion(long regionId, String subRegionName) throws Exception{

        CurrentTimeFactory.initializeCurrentTimeFactory();

        IPSubRegion ipSubRegion = new IPSubRegion();
        ipSubRegion.setRegionId(regionId);
        ipSubRegion.setSubRegionName(subRegionName);
        ipSubRegion.setCreationTime(System.currentTimeMillis());

        ipSubRegionDAO.insert(ipSubRegion);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPSubRegion> getAllSubRegions() throws Exception {
        return ipSubRegionDAO.getAllSubRegions();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPSubRegion> getAllSubRegionsByRegionId(long regionId) throws Exception {
        return ipSubRegionDAO.getAllSubRegionsByRegionId(regionId);
    }


    public List<ObjectPair<String, String>> getAllSubRegionsWithParentRegion() throws Exception {
        return ipSubRegionDAO.getAllSubRegionsWithParentRegion();
    }
}
