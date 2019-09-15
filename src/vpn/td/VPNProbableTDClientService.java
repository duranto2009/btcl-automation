package vpn.td;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.NavigationService;
import util.TransactionType;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class VPNProbableTDClientService implements NavigationService {

	@DAO
	VPNProbableTDClientDAO vpnProbableTDClientDAO;
	
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
		List <VPNProbableTDClient> list = vpnProbableTDClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return vpnProbableTDClientDAO.getDTOListByIDList((List<Long>) recordIDs);
	}
	@Transactional
	public void updateTDDate(long clientID, long tdDate) throws Exception {
		VPNProbableTDClient existingVpnProbableTDClient = vpnProbableTDClientDAO.getVPNProbableTDClientByClientID(clientID);
		if(existingVpnProbableTDClient == null) {
			VPNProbableTDClient newVpnProbableTDClient = new VPNProbableTDClient(clientID,tdDate);
			vpnProbableTDClientDAO.insertVPNProbableTDClient(newVpnProbableTDClient);
		} else {
			existingVpnProbableTDClient.setTdDate(tdDate);
			vpnProbableTDClientDAO.updateVPNProbableTDClient(existingVpnProbableTDClient);
		}
	}
}
