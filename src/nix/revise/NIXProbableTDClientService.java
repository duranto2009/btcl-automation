package nix.revise;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.NavigationService;
import util.TransactionType;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class NIXProbableTDClientService implements NavigationService {

    @DAO
    NIXProbableTDClientDAO nixProbableTDClientDAO;

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
        List<NIXProbableTDClient> list = nixProbableTDClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return nixProbableTDClientDAO.getDTOListByIDList((List<Long>) recordIDs);
    }
    @Transactional
    public void updateTDDate(long clientID, long tdDate) throws Exception {
        NIXProbableTDClient existingNIXProbableTDClient = nixProbableTDClientDAO.getNIXProbableTDClientByClientID(clientID);
        if(existingNIXProbableTDClient == null) {
            NIXProbableTDClient newNIXProbableTDClient = new NIXProbableTDClient(clientID,tdDate);
            nixProbableTDClientDAO.insertNIXProbableTDClient(newNIXProbableTDClient);
        } else {
            existingNIXProbableTDClient.setTdDate(tdDate);
            nixProbableTDClientDAO.updateNIXProbableTDClient(existingNIXProbableTDClient);
        }
    }
}
