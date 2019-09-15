package coLocation.td;

import coLocation.connection.CoLocationConnectionService;
import common.ClientDTOConditionBuilder;
import common.RequestFailureException;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

public class CoLocationProbableTDDAO {

	Class<CoLocationProbableTDDTO> classObject = CoLocationProbableTDDTO.class;

	CoLocationConnectionService coLocationConnectionService = ServiceDAOFactory.getService(CoLocationConnectionService.class);


	public List<CoLocationProbableTDDTO> getDTOListByIDList(List<Long> idList) throws Exception{
		//GET ALL OBJECTS AND SET THE CONNECTION NAME
		return getObjectListByIDList(classObject, idList).stream().map(t->{
			try {
				t.setConnectionName(coLocationConnectionService.getColocationConnection(t.getConnectionID()).getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return t;
		}).collect(Collectors.toList());
	}

	public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{
		ResultSet rs = getResultSetBySqlPair(
				new CoLocationProbableTDDTOConditionBuilder()
				.selectID()
				.fromCoLocationProbableTDDTO()
				.Where()
				.clientIDEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
						.isTDRequested(false) // return the app list for which td is not requested yet
				.clientIDInSqlPair(
						new ClientDTOConditionBuilder()
						.selectClientID()
						.fromClientDTO()
						.Where()
						.loginNameBothLike(hashtable.get("clientName"))
						.getNullableSqlPair()
						)
//				.tdDateGreaterThanEquals(TimeConverter.getDateFromString( hashtable.get("fromDate")))
//				.tdDateLessThan(TimeConverter.getNextDateFromString(hashtable.get("toDate")))
//				.orderBytdDateAsc()
				.getSqlPair()
				);

		return getSingleColumnListByResultSet(rs, Long.class);
	}

	public CoLocationProbableTDDTO getCoLocationProbableTDByConnectionID(long connectionID) throws Exception {
		List<CoLocationProbableTDDTO> coLocationProbableTDDTOS = getAllObjectList(classObject,
				new CoLocationProbableTDDTOConditionBuilder()
				.Where()
				.connectionIDEquals(connectionID)
				.getCondition()
				);
		
		return coLocationProbableTDDTOS.isEmpty()?null: coLocationProbableTDDTOS.get(0);
	}
	public void insertCoLocationProbableTDClient(CoLocationProbableTDDTO coLocationProbableTDDTO) throws Exception {
		ModifiedSqlGenerator.insert(coLocationProbableTDDTO);
	}
	public void updateCoLocationProbableTDClient(CoLocationProbableTDDTO coLocationProbableTDDTO) throws Exception {
		ModifiedSqlGenerator.updateEntity(coLocationProbableTDDTO);
	}

	public CoLocationProbableTDDTO getCoLocationProbableTDDTOByHistoryId(long historyID) {
		try {
			return ModifiedSqlGenerator.getAllObjectList(CoLocationProbableTDDTO.class,
					new CoLocationProbableTDDTOConditionBuilder()
							.Where()
							.historyIDEquals(historyID)
							.getCondition()
			).stream()
					.findFirst()
					.orElseThrow(()->new RequestFailureException("No Probable TD found for historyID: " + historyID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	void save(CoLocationProbableTDDTO probableTD) throws Exception {
    	ModifiedSqlGenerator.insert(probableTD);
	}
}
