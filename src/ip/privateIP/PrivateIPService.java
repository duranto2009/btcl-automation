package ip.privateIP;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.NavigationService;
import util.TransactionType;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class PrivateIPService implements NavigationService {

    @DAO
    PrivateIPDAO privateIPDAO;

    @Transactional
    public void addNewPrivateIP(PrivateIPBlock block) throws Exception {
        privateIPDAO.addNewPrivateIP(block);
    }

    @Override
    public List<Long> getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Override
    public List<Long> getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return privateIPDAO.getIDsWithSearchCriteria(searchCriteria);
    }

    @Override
    public List<PrivateIPBlock> getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return privateIPDAO.getDTOs((List<Long>)recordIDs);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<PrivateIPBlock> getPrivateIPsBySubRegionId( long subRegionId) throws Exception {
        return privateIPDAO.getPrivateIPsBySubRegionId(subRegionId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<PrivateIPBlock> getPrivateIPsByModuleId(int moduleId) throws Exception {
        return privateIPDAO.getPrivateIPsByModuleId(moduleId);
    }

    @Transactional
    public void deletePrivateIPsBySubRegionId(long subRegionId) throws Exception {
        privateIPDAO.deletePrivateIPsBySubRegionId(subRegionId);
    }

    @Transactional
    public void deletePrivateIPsByModuleId(int moduleId) throws Exception {
        privateIPDAO.deletePrivateIPsByModuleId(moduleId);
    }

    @Transactional
    public void deletePrivateIPsById(long id) throws Exception {
        privateIPDAO.deletePrivateIPsById(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<PrivateIPBlock> getPrivateIPsBySubRegionIdAndModuleId(long subRegionId, int moduleId) throws Exception {
        return privateIPDAO.getPrivateIPsBySubRegionIdAndModuleId(subRegionId, moduleId);
    }
}
