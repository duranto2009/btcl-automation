package distance;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;

import common.CategoryConstants;
import connection.DatabaseConnection;
import distance.form.DistrictDistanceDTO;
import distance.form.UnionDistanceDTO;
import distance.form.UpazilaDistanceDTO;
import inventory.InventoryItem;
import util.DAOResult;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import static util.ModifiedSqlGenerator.*;

public class DistanceDAO {

	static Logger logger = Logger.getLogger((distance.DistanceDAO.class).getName());
	HashMap<String, Integer> districtMap = new HashMap<String, Integer>();
	
	
	public List<DistrictDistanceDTO> getAllDistrictDistanceDTO() throws Exception{
		return getAllObjectList(DistrictDistanceDTO.class);
	}
	

	public DAOResult updateDistrictDistance(ArrayList<DistrictDistanceDTO> list) throws Exception {
		DAOResult daoResult = new DAOResult();

		int count=0;
		
		for (int index = 0; index < list.size(); index++) {
			
			//isDistrictDistanceInsertedInSameOrder
			boolean isDistrictDistanceInsertedInSameOrder=false;
			boolean isDistrictDistanceInsertedInReverseOrder=false;
			int sourceDistrictID = list.get(index).getSourceDistrictId();
			int destinationDistrictID = list.get(index).getDestinationDistrictId();

			String sql = "select count(*) as count from at_district_distance where rowId="+sourceDistrictID+" and colId="+destinationDistrictID+"";
			logger.debug(sql);
			
			DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
			PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {						
				count = resultSet.getInt("count");
				if(count>0){
					isDistrictDistanceInsertedInSameOrder=true;
				}
				break;
			}	
			
			sql = "select count(*) as count from at_district_distance where rowId="+destinationDistrictID+" and colId="+sourceDistrictID+"";
			logger.debug(sql);
			
			preparedStatement = databaseConnection.getNewPrepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {						
				count = resultSet.getInt("count");
				if(count>0){
					isDistrictDistanceInsertedInReverseOrder=true;
				}
				break;
			}	
				
			if(isDistrictDistanceInsertedInSameOrder){
				
				DistrictDistanceDTO oldDistrctDTO = getDistrictDTOBySourceDistrctIDAndDestinationDistrictID(list.get(index).getSourceDistrictId(), list.get(index).getDestinationDistrictId());
				oldDistrctDTO.setDistance(list.get(index).getDistance());
				
				updateEntity(oldDistrctDTO, DistrictDistanceDTO.class, false, false);
				
			} else if(isDistrictDistanceInsertedInReverseOrder){
				int sourceID = list.get(index).getSourceDistrictId();
				int destinationID = list.get(index).getDestinationDistrictId();
									
				DistrictDistanceDTO oldDistrctDTO = getDistrictDTOBySourceDistrctIDAndDestinationDistrictID(destinationID, sourceID);
				oldDistrctDTO.setDistance(list.get(index).getDistance());
				
				updateEntity(oldDistrctDTO, DistrictDistanceDTO.class, false, false);
			} else{
				list.get(index).setLastModificationTime(System.currentTimeMillis());
				insert(list.get(index), DistrictDistanceDTO.class, false);
			}
		}
		return daoResult;
	}

	public DistrictDistanceDTO getDistrictDTOBySourceDistrctIDAndDestinationDistrictID(int rowId, int colId) throws Exception {
		DistrictDistanceDTO districtDistanceDTO = getDistrictDistanceDTOByTwoDistrictID(rowId, colId);
		return districtDistanceDTO;
	}

	@SuppressWarnings("unchecked")
	public List<DistrictDistanceDTO> getDistancesFromAllDistricts(int sourceDistrictID) throws Exception{
		
		List<DistrictDistanceDTO> dataWithSourceInRowID = (List<DistrictDistanceDTO>)ModifiedSqlGenerator.getAllObjectList(DistrictDistanceDTO.class, " where rowId = "+sourceDistrictID);
		List<DistrictDistanceDTO> dataWithSourceInColumnID = (List<DistrictDistanceDTO>)ModifiedSqlGenerator.getAllObjectList(DistrictDistanceDTO.class, " where colId = "+sourceDistrictID);
		
		for(DistrictDistanceDTO districtData : dataWithSourceInColumnID){
			int realSourceDistrictID = districtData.getDestinationDistrictId();
			districtData.setDestinationDistrictId(districtData.getSourceDistrictId());
			districtData.setSourceDistrictId(realSourceDistrictID);
		}
		
		dataWithSourceInRowID.addAll(dataWithSourceInColumnID);
		return dataWithSourceInRowID;
	}
	public DistrictDistanceDTO getDistanceBetweenTwoDistricts(long rowId, long colId) throws Exception{
		return getDistrictDistanceDTOByTwoDistrictID(rowId, colId);
	}

	@SuppressWarnings("unchecked")
	public List<DistrictDistanceDTO> getAllDistricts() throws Exception {

		List<InventoryItem> allInventoryOfDistrictCategory = (List<InventoryItem>)ModifiedSqlGenerator.getAllObjectList(InventoryItem.class, 
				" where "+ModifiedSqlGenerator.getColumnName(InventoryItem.class, "inventoryCatagoryTypeID")+" = "+ CategoryConstants.CATEGORY_ID_DISTRICT + 
				" order by " + ModifiedSqlGenerator.getColumnName( InventoryItem.class, "name" ) + " asc " );
		
		List<DistrictDistanceDTO> allDistricts = new ArrayList<DistrictDistanceDTO>();
		for(InventoryItem inventoryOfDistrictCategory : allInventoryOfDistrictCategory){
			DistrictDistanceDTO districtDistanceDTO = new DistrictDistanceDTO();
			districtDistanceDTO.setId((int)inventoryOfDistrictCategory.getID());
			districtDistanceDTO.setName(inventoryOfDistrictCategory.getName());

			allDistricts.add(districtDistanceDTO);
		}
		
		return allDistricts;
	}

	//Upazila
	public ArrayList<UpazilaDistanceDTO> getAllUpazillasByDistrictId(long districtId) throws Exception {

		ArrayList<UpazilaDistanceDTO> data = new ArrayList<UpazilaDistanceDTO>();
		
		String sql = "select * from at_inventory_item where invitInvCatTypeID="+CategoryConstants.CATEGORY_ID_UPAZILA+" and invitParentItemID="+districtId;
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			UpazilaDistanceDTO row = new UpazilaDistanceDTO();
			row.setParentDistrictId(resultSet.getLong("invitParentItemID"));
			row.setUpazilaId(resultSet.getLong("invitID"));
			row.setDistance(resultSet.getInt("distance"));
			
			data.add(row);
		}
		logger.debug("data size:  "+data.size());
		return data;

	}
	
	public boolean isUpazilaEntriedInDistanceTable(long upazilaId) throws Exception {
		int count = 0;
		
		String sql = "select count(*) as count from at_upazila_distance where upUpazilaID="+upazilaId+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			count = resultSet.getInt("count");
			break;
		}
		if(count>0){
			return true;
		}
		return false; 
	}

	public boolean isUnionEntriedInDistanceTable(long unionID) throws Exception {
		int count = 0;
		
		String sql = "select count(*) as count from at_union_distance where unionUnionID="+unionID+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			count = resultSet.getInt("count");
			break;
		}
		if(count>0){
			return true;
		}
		return false; 
	}

	public boolean isUpazilaUnderThisDistrict(long districtID, long upazilaID) throws Exception {
		int count = 0;
		
		String sql = "select count(*) as count from at_inventory_item where invitID="+upazilaID+" and invitInvCatTypeID = 2 and invitParentItemID="+districtID+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			count = resultSet.getInt("count");
			break;
		}
		if(count>0){
			return true;
		}		
		return false;
	}

	
	public long getUpIdByUpazilaId(long upazilaId) throws Exception {
		long upID = 0;
		
		String sql = "select upID from at_upazila_distance where upUpazilaID="+upazilaId+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			upID = resultSet.getLong("upID");
			break;
		}
		
		return upID; 
	}

	public int getDistanceByUpId(long upId) throws Exception {
		int distance = 0;
		
		String sql = "select upDistance from at_upazila_distance where upUpazilaID="+upId+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			distance = resultSet.getInt("upDistance");
			break;
		}
		
		return distance;
	}

	
	public UpazilaDistanceDTO getUpazilaDTOByUpazilaId(long upazilaId) throws Exception {
		UpazilaDistanceDTO upazilaDTO = new UpazilaDistanceDTO();
		
		String sql = "select * from at_upazila_distance where upUpazilaID="+upazilaId+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {
			upazilaDTO.setId(resultSet.getLong("upID"));
			upazilaDTO.setParentDistrictId(resultSet.getLong("upDistrictID"));
			upazilaDTO.setUpazilaId(resultSet.getLong("upUpazilaID"));
			upazilaDTO.setDistance(resultSet.getInt("upDistance"));
			break;
		}
		
		return upazilaDTO;
	}

	public UnionDistanceDTO getUnionDTOByUnionId(long unionID) throws Exception{
		UnionDistanceDTO unionDTO = new UnionDistanceDTO();
		
		String sql = "select * from at_union_distance where unionUnionID="+unionID+"";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			unionDTO.setId(resultSet.getLong("unionID"));
			unionDTO.setParentUpazilaID(resultSet.getLong("upUpazilaID"));
			unionDTO.setUnionID(resultSet.getLong("unionUnionID"));
			unionDTO.setDistance(resultSet.getInt("unionDistance"));
			break;
		}

		return unionDTO;
	}

	
	public void updateUpazila(UpazilaDistanceDTO upazilaDTO) throws Exception {
		updateEntity(upazilaDTO);
	}

	public void updateUnion(UnionDistanceDTO unionDTO) throws Exception {
		updateEntity(unionDTO);
	}
	
	
	public UpazilaDistanceDTO insertUpazilaDTO(UpazilaDistanceDTO row) throws Exception {
		UpazilaDistanceDTO upazilaDTO = new UpazilaDistanceDTO();
		insert(row, UpazilaDistanceDTO.class, false);
		upazilaDTO = getUpazilaDTOByUpazilaId(row.getUpazilaId());
		return upazilaDTO;
	}

	public UnionDistanceDTO insertUnionDTO(UnionDistanceDTO row) throws Exception {
		UnionDistanceDTO unionDTO = new UnionDistanceDTO();
		insert(row, UnionDistanceDTO.class, false);
		unionDTO = getUnionDTOByUnionId(row.getUnionID());
		return unionDTO;
	}

	public ArrayList<DistrictDistanceDTO> getMatchedDistricts(String districtValue) throws Exception {
		ArrayList<DistrictDistanceDTO> districtDistanceDTOList = new ArrayList<>();
		
		String sql = "select * from at_inventory_item where invitInvCatTypeID="+CategoryConstants.CATEGORY_ID_DISTRICT+" and invitName LIKE '%" + districtValue + "%' limit 10";
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			DistrictDistanceDTO districtDistanceDTO = new DistrictDistanceDTO();
			districtDistanceDTO.setId(resultSet.getInt("invitID"));
			districtDistanceDTO.setName(resultSet.getString("invitName"));
			districtDistanceDTOList.add(districtDistanceDTO);
		}

		return districtDistanceDTOList;
	}

	public ArrayList<UpazilaDistanceDTO> getMatchedUpazilas(String upazilaValue, String parentDistrictID) throws Exception {
		ArrayList<UpazilaDistanceDTO> upazilaDistanceDTOList = new ArrayList<>();
		
		String sql = "select * from at_inventory_item where invitParentItemID="+ parentDistrictID +" and invitInvCatTypeID="+CategoryConstants.CATEGORY_ID_UPAZILA+" and invitName LIKE '%" + upazilaValue + "%' limit 10";
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while (resultSet.next()) {
			UpazilaDistanceDTO upazilaDistanceDTO = new UpazilaDistanceDTO();
			upazilaDistanceDTO.setUpazilaId(resultSet.getInt("invitID"));
			upazilaDistanceDTO.setUpazilaName(resultSet.getString("invitName"));
			upazilaDistanceDTOList.add(upazilaDistanceDTO);
		}
		return upazilaDistanceDTOList;
	}

	public List<UnionDistanceDTO> getUnionDistanceDTOListByUpazilaID(String upazilaID) throws Exception {
		List<UnionDistanceDTO> unionDistanceDTOList = new ArrayList<>();
		
		String sql = "select invitID, invitName from at_inventory_item where invitInvCatTypeID="+CategoryConstants.CATEGORY_ID_UNION+" and invitParentItemID = " + upazilaID +"";
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while (resultSet.next()) {
			UnionDistanceDTO row;			
			boolean isUnionEntried = isUnionEntriedInDistanceTable(resultSet.getLong("invitID"));
			
			if(isUnionEntried){
				row = getUnionDTOByUnionId(resultSet.getLong("invitID"));
			}else{
				UnionDistanceDTO tempUnion = new UnionDistanceDTO();
				tempUnion.setUnionID(resultSet.getLong("invitID"));
				tempUnion.setParentUpazilaID(Long.parseLong(upazilaID));
				tempUnion.setDistance(0);
				row = insertUnionDTO(tempUnion);
			}
			
			row.setUnionName(resultSet.getString("invitName"));
			unionDistanceDTOList.add(row);
		}
		
		return unionDistanceDTOList;
	}

	public List<UpazilaDistanceDTO> getUpazilaDistanceDTOListByDistrictID(String districtID) throws Exception {
		List<UpazilaDistanceDTO> upazilaDistanceDTOList = new ArrayList<>();
		
		String sql = "select invitID, invitName from at_inventory_item where invitInvCatTypeID="+CategoryConstants.CATEGORY_ID_UPAZILA+" and invitParentItemID = " + districtID +"";
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while (resultSet.next()) {
			UpazilaDistanceDTO row;			
			boolean isUpazilaEntried = isUpazilaEntriedInDistanceTable(resultSet.getLong("invitID"));
			
			if(isUpazilaEntried){
				row = getUpazilaDTOByUpazilaId(resultSet.getLong("invitID"));
			}else{
				UpazilaDistanceDTO tempUpazila = new UpazilaDistanceDTO();
				tempUpazila.setUpazilaId(resultSet.getLong("invitID"));
				tempUpazila.setParentDistrictId(Long.parseLong(districtID));
				tempUpazila.setDistance(0);
				tempUpazila.setLastModificationTime(System.currentTimeMillis());
				row = insertUpazilaDTO(tempUpazila);
			}
			row.setUpazilaName(resultSet.getString("invitName"));
			upazilaDistanceDTOList.add(row);			
		}
		
		return upazilaDistanceDTOList;
	}

	//For finding distance Between Any two locations
	double getDistanceFromUnionTable(long upazilaID, long unionID) throws Exception {
		double distance = 0;
		String sql = "select unionDistance from at_union_distance where upUpazilaID = " + upazilaID + " and unionUnionID=" + unionID;
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			distance = resultSet.getInt("unionDistance");
			break;
		}
		return distance;
	}

	double getDistanceFromUpazilaTable(long districtID, long upazilaID) throws Exception{
		double distance = 0;
		String sql = "select upDistance from at_upazila_distance where upDistrictID = " + districtID + " and upUpazilaID=" + upazilaID;
		logger.debug(sql);
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			distance = resultSet.getInt("upDistance");
			break;
		}
		return distance;
	}
	
	DistrictDistanceDTO getDistrictDistanceDTOByTwoDistrictID(long districtID1, long districtID2) throws Exception{
		boolean isDistrictsInSameOrderInDatabase=false;
		DistrictDistanceDTO districtDistanceDTO = new DistrictDistanceDTO();
		
		String sql = "select count(*) as count from at_district_distance where rowId = " + districtID1 + " and colId=" + districtID2;
		logger.debug(sql);

		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			if (resultSet.getInt("count")>0){
				isDistrictsInSameOrderInDatabase=true;
			}
			break;
		}
		
		if (isDistrictsInSameOrderInDatabase){
			sql = "select * from at_district_distance where rowId = " + districtID1 + " and colId=" + districtID2;
		}else{
			sql = "select * from at_district_distance where rowId = " + districtID2 + " and colId=" + districtID1;
		}
		
		preparedStatement = databaseConnection.getNewPrepareStatement(sql);
		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			districtDistanceDTO.setId(resultSet.getInt("id"));
			districtDistanceDTO.setSourceDistrictId(resultSet.getInt("rowId"));
			districtDistanceDTO.setDestinationDistrictId(resultSet.getInt("colId"));
			districtDistanceDTO.setDistance(resultSet.getDouble("distance"));
			break;
		}
		return districtDistanceDTO;
	}

	double getDistanceFromDistrictTable(long districtID1, long districtID2) throws Exception {
		DistrictDistanceDTO districtDistanceDTO = getDistrictDistanceDTOByTwoDistrictID(districtID1, districtID2);				
		return districtDistanceDTO.getDistance();
	}

}
