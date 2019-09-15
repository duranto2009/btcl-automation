package coLocation.td;

import annotation.DAO;
import annotation.Transactional;
import coLocation.connection.CoLocationConnectionDTO;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import login.LoginDTO;
import util.NavigationService;
import util.TimeConverter;
import util.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class CoLocationProbableTDService implements NavigationService {

	@DAO
	CoLocationProbableTDDAO coLocationProbableTDDAO;

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
		List <CoLocationProbableTDDTO> list = coLocationProbableTDDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(transactionType = TransactionType.READONLY)
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return coLocationProbableTDDAO.getDTOListByIDList((List<Long>) recordIDs);
	}
	//TODO need to change
	@Transactional
	public void updateTDDate(long clientID, long tdDate) throws Exception {
		CoLocationProbableTDDTO existingLliProbableTDClient = coLocationProbableTDDAO.getCoLocationProbableTDByConnectionID(clientID);
		if(existingLliProbableTDClient == null) {
			CoLocationProbableTDDTO newCoLocationProbableTD = new CoLocationProbableTDDTO().builder().clientID(clientID).dayLeft(tdDate).build();
			coLocationProbableTDDAO.insertCoLocationProbableTDClient(newCoLocationProbableTD);
		} else {
			existingLliProbableTDClient.setDayLeft(tdDate);
			coLocationProbableTDDAO.updateCoLocationProbableTDClient(existingLliProbableTDClient);
		}
	}

	@Transactional
	public CoLocationProbableTDDTO getCoLocationProbableTDDTOByHistoryId(long historyID){
		return coLocationProbableTDDAO.getCoLocationProbableTDDTOByHistoryId(historyID);
	}
	@Transactional
	public CoLocationProbableTDDTO getCoLocationProbableTDByConnectionID(long historyID) throws Exception {
		return coLocationProbableTDDAO.getCoLocationProbableTDByConnectionID(historyID);
	}

	@Transactional
	public void updateCoLocationProbableTDClient(CoLocationProbableTDDTO coLocationProbableTDDTO) throws Exception {
		coLocationProbableTDDAO.updateCoLocationProbableTDClient(coLocationProbableTDDTO);
	}

	@Transactional
	public void saveByConnection(CoLocationConnectionDTO connection) throws Exception {
		CoLocationProbableTDDTO probableTD = createProbableTDFromConnectionAndDayCount(connection);
		coLocationProbableTDDAO.save(probableTD);
	}

	private CoLocationProbableTDDTO createProbableTDFromConnectionAndDayCount(CoLocationConnectionDTO connection) {
		CoLocationProbableTDDTO probableTD = new CoLocationProbableTDDTO();
		probableTD.setClientID(connection.getClientID());
		ClientDTO clientDTO = AllClientRepository.getInstance().getVpnClientByClientID(connection.getClientID(), ModuleConstants.Module_ID_COLOCATION);
		if(clientDTO == null){
			throw new RequestFailureException("No Client Found for Client id " + connection.getClient() + " moduleId " + ModuleConstants.ActiveModuleMap
					.get(ModuleConstants.Module_ID_COLOCATION));
		}

		probableTD.setClientName(clientDTO.getLoginName());
		probableTD.setConnectionID(connection.getClientID());
		probableTD.setHistoryID(connection.getHistoryID());
		probableTD.setConnectionName(connection.getName());
		probableTD.setTDRequested(false);
		probableTD.setDayLeft(
		        LocalDate.now(ZoneId.systemDefault())
                        .atStartOfDay()
                        .plusMonths(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );
		return probableTD;
	}
}
