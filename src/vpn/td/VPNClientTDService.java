package vpn.td;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.TransactionType;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class VPNClientTDService implements NavigationService {

    @DAO
    VPNClientTDDAO vpnClientTDDAO;


    @SuppressWarnings("rawtypes")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {
        List<VPNClientTD> list = vpnClientTDDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return vpnClientTDDAO.getDTOListByIDList((List<Long>) recordIDs);
    }

    @Transactional
    public void tempDisconnectClientByClientID(long clientID, long tdActivationDate) throws Exception {

        // TODO: 3/3/2019 td by client id

        //nixConnectionService.tdNIXConnectionByClientID(clientID, tdActivationDate);

        VPNClientTD existingVPNClientTD = vpnClientTDDAO.getVPNClientTDByClientID(clientID);
        if (existingVPNClientTD != null) {
            throw new RequestFailureException("Client is already Temporarily Disconnected");
        }
        VPNClientTD newVPNClientTD = new VPNClientTD(clientID, tdActivationDate, Long.MAX_VALUE);
        vpnClientTDDAO.insertClientTD(newVPNClientTD);
    }

    @Transactional
    public void reconnectClientByClientID(long clientID, long reconnectionDate) throws Exception {

        // TODO: 3/3/2019 fetch links to reconnect by client id
        //nixConnectionService.reconnectionConnectionByClientID(clientID);

        VPNClientTD vpnClientTD = vpnClientTDDAO.getVPNClientTDByClientID(clientID);
        if (vpnClientTD == null) {
            throw new RequestFailureException("Client is not Temporarily Disconnected");
        }
        vpnClientTD.setTdTo(reconnectionDate);
        vpnClientTDDAO.updateClientTD(vpnClientTD);
    }

    @Transactional
    public boolean isClientTemporarilyDisconnected(long clientID) throws Exception {
        VPNClientTD existingVPNClientTD = vpnClientTDDAO.getVPNClientTDByClientID(clientID);
        if (existingVPNClientTD != null) {
            return true;
        }
        return false;
    }

    @Transactional
    public String getClientTDStatus(long clientID) throws Exception {
        return isClientTemporarilyDisconnected(clientID) ? "TD" : "Active";
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNClientTD> getTDHistoryByClientID(long clientID) throws Exception {
        return vpnClientTDDAO.getTDHistoryByClientID(clientID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public long getProbableTDDateByClientID(long clientID) throws Exception {
        List<VPNProbableTD> probableTDClient = ModifiedSqlGenerator.getAllObjectList(
                VPNProbableTD.class, " where clientID = " + clientID);
        return probableTDClient.size() > 0 ? probableTDClient.get(0).getTdDate() : 0;
    }
}
