package vpn.td;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import util.NavigationService;
import util.TransactionType;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class VPNProbableTDService implements NavigationService {

    @DAO
    VPNProbableTDDAO vpnProbableTDDAO;

    @SuppressWarnings("rawtypes")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        List<VPNProbableTD> list = vpnProbableTDDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return vpnProbableTDDAO.getDTOListByIDList((List<Long>) recordIDs);
    }
    @Transactional
    public void updateTDDate(long clientID, long tdDate) throws Exception {
        VPNProbableTD existingVPNProbableTDClient = vpnProbableTDDAO.getVPNProbableTDClientByClientID(clientID);
        if(existingVPNProbableTDClient == null) {
            VPNProbableTD newVPNProbableTDClient = new VPNProbableTD(clientID,tdDate);
            vpnProbableTDDAO.insertVPNProbableTDClient(newVPNProbableTDClient);
        } else {
            existingVPNProbableTDClient.setTdDate(tdDate);
            vpnProbableTDDAO.updateVPNProbableTDClient(existingVPNProbableTDClient);
        }
    }

    @Transactional
    public VPNProbableTD getProbableTDByClientID(long clientId)throws Exception {
        List<VPNProbableTD> vpnProbableTDS = vpnProbableTDDAO.getProbableTDByClientID(clientId);
        if(vpnProbableTDS.isEmpty()) throw  new RequestFailureException("No Entry found for the client");
        return  vpnProbableTDS.get(0);
    }
}
