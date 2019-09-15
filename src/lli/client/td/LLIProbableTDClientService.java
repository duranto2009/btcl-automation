package lli.client.td;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.NavigationService;
import util.TransactionType;

public class LLIProbableTDClientService implements NavigationService {

	@DAO private LLIProbableTDClientDAO lliProbableTDClientDAO;
	
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
		return (List<Long>) lliProbableTDClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return lliProbableTDClientDAO.getDTOListByIDList((List<Long>) recordIDs);
	}
	@Transactional
	public void updateTDDate(long clientID, long tdDate) throws Exception {
		LLIProbableTDClient existingLliProbableTDClient = lliProbableTDClientDAO.getLLIProbableTDClientByClientID(clientID);		
		if(existingLliProbableTDClient == null) {
			LLIProbableTDClient newLliProbableTDClient = new LLIProbableTDClient(clientID,tdDate);
			lliProbableTDClientDAO.insertLLIProbableTDClient(newLliProbableTDClient);
		} else {
			existingLliProbableTDClient.setTdDate(tdDate);
			lliProbableTDClientDAO.updateLLIProbableTDClient(existingLliProbableTDClient);
		}
	}
}
