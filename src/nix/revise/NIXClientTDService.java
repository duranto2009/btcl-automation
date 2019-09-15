package nix.revise;

import annotation.DAO;
import annotation.Transactional;
import common.ClientDTO;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import lli.LLIDropdownPair;
import login.LoginDTO;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import requestMapping.Service;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.TransactionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class NIXClientTDService implements NavigationService {

    @DAO
    NIXClientTDDAO nixClientTDDAO;

    @Service
    NIXConnectionService nixConnectionService;

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
        List<NIXClientTD> list = nixClientTDDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return nixClientTDDAO.getDTOListByIDList((List<Long>) recordIDs);
    }

    private int getCountByStatus(List<NIXConnection> nixConnections, int status) {

        return (int) nixConnections.stream().filter(a -> a.getStatus() == status).count();

    }


    @Transactional
    public void validateConnectionTD(long clientID, long activationTime) throws Exception {
        List<NIXConnection> currentLLIConnectionList = nixConnectionService.getConnectionByClientID(clientID);
        int activeConnectionCount = getCountByStatus(currentLLIConnectionList, NIXConstants.STATUS_ACTIVE);
        int tdConnectionCount = getCountByStatus(currentLLIConnectionList, NIXConstants.STATUS_TD);

        if (activeConnectionCount * tdConnectionCount != 0) {
            throw new RequestFailureException("Client( " + clientID + ") has active and TD connection at the same time");
        }
    }

    @Transactional
    public void tempDisconnectClientByClientID(long clientID, long tdActivationDate) throws Exception {

        nixConnectionService.tdNIXConnectionByClientID(clientID, tdActivationDate);

        NIXClientTD existingLliClientTD = nixClientTDDAO.getNIXClientTDByClientID(clientID);
        if (existingLliClientTD != null) {
            throw new RequestFailureException("Client is already Temporarily Disconnected");
        }
        NIXClientTD newNIXClientTD = new NIXClientTD(clientID, tdActivationDate, Long.MAX_VALUE);
        nixClientTDDAO.insertClientTD(newNIXClientTD);
    }

    @Transactional
    public void reconnectClientByClientID(long clientID, long reconnectionDate) throws Exception {

        nixConnectionService.reconnectionConnectionByClientID(clientID);

        NIXClientTD nixClientTD = nixClientTDDAO.getNIXClientTDByClientID(clientID);
        if (nixClientTD == null) {
            throw new RequestFailureException("Client is not Temporarily Disconnected");
        }
        nixClientTD.setTdTo(reconnectionDate);
        nixClientTDDAO.updateClientTD(nixClientTD);
    }

    @Transactional
    public boolean isClientTemporarilyDisconnected(long clientID) throws Exception {
        NIXClientTD existingLliClientTD = nixClientTDDAO.getNIXClientTDByClientID(clientID);
        if (existingLliClientTD != null) {
            return true;
        }
        return false;
    }

    @Transactional
    public String getClientTDStatus(long clientID) throws Exception {
        return isClientTemporarilyDisconnected(clientID) ? "TD" : "Active";
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXClientTD> getTDHistoryByClientID(long clientID) throws Exception {
        return nixClientTDDAO.getTDHistoryByClientID(clientID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public long getProbableTDDateByClientID(long clientID) throws Exception {
        List<NIXProbableTDClient> probableTDClient = ModifiedSqlGenerator.getAllObjectList(NIXProbableTDClient.class, " where clientID = " + clientID);
        return probableTDClient.size() > 0 ? probableTDClient.get(0).getTdDate() : 0;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getallTd()throws Exception {
        List<NIXClientTD>nixClientTDS = nixClientTDDAO.getAllTD();
        List<LLIDropdownPair>clientDetailsDTOS =new ArrayList<>();
        nixClientTDS.forEach(s->{
            ClientDTO clientDetailsDTO = AllClientRepository.getInstance().getClientByClientID(s.getClientID());
            clientDetailsDTOS.add(new LLIDropdownPair(clientDetailsDTO.getClientID(),clientDetailsDTO.getLoginName()));

        });
        return clientDetailsDTOS;
    }

}
