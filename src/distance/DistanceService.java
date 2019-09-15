package distance;

import java.util.ArrayList;
import java.util.List;

import annotation.Transactional;
import connection.DatabaseConnection;
import distance.form.DistrictDistanceDTO;
import distance.form.UnionDistanceDTO;
import distance.form.UpazilaDistanceDTO;
import inventory.InventoryDAO;
import inventory.InventoryItem;
import util.DAOResult;
import util.DatabaseConnectionFactory;

public class DistanceService {
	DistanceDAO distanceDAO = new DistanceDAO();
	
	@Transactional
	public DAOResult updateDistance(ArrayList<DistrictDistanceDTO> list) throws Exception {
		DAOResult daoResult = new DAOResult();
		daoResult = distanceDAO.updateDistrictDistance(list);
		return daoResult;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<DistrictDistanceDTO> getDistancesFromAllDistricts(int id) throws Exception {
		List<DistrictDistanceDTO> districtDistanceDTOList = new ArrayList<DistrictDistanceDTO>();
		districtDistanceDTOList = distanceDAO.getDistancesFromAllDistricts(id);
		return districtDistanceDTOList;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public DistrictDistanceDTO getDistanceBetweenTwoDistricts(long id, long feDistrictId) throws Exception{
		return distanceDAO.getDistanceBetweenTwoDistricts(id, feDistrictId);
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<DistrictDistanceDTO> getAllDistricts() throws Exception {
		List<DistrictDistanceDTO> list = new ArrayList<DistrictDistanceDTO>();
		list = distanceDAO.getAllDistricts();
		return list;
	}

	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public UpazilaDistanceDTO getUpazilaDTOByUpazilaId(long upazilaId) throws Exception {
		UpazilaDistanceDTO upazilaDTO = new UpazilaDistanceDTO();
		upazilaDTO= distanceDAO.getUpazilaDTOByUpazilaId(upazilaId);
		return upazilaDTO;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public UnionDistanceDTO getUnionDTOByUnionId(long unionID) throws Exception {
		UnionDistanceDTO unionDTO = new UnionDistanceDTO();
		unionDTO= distanceDAO.getUnionDTOByUnionId(unionID);
		return unionDTO;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isUpazilaUnderThisDistrict(long districtID, long upazilaID) throws Exception{
		boolean isUpazilaUnderThisDistrict = false;					
		isUpazilaUnderThisDistrict = distanceDAO.isUpazilaUnderThisDistrict(districtID, upazilaID);		
		return isUpazilaUnderThisDistrict;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isUpazilaEntriedInDistanceTable(long upazilaID) throws Exception {
		boolean isUpazilaEntriedInDistanceTable = false;
		isUpazilaEntriedInDistanceTable = distanceDAO.isUpazilaEntriedInDistanceTable(upazilaID);
		return isUpazilaEntriedInDistanceTable;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isUnionEntriedInDistanceTable(long unionID) throws Exception {
		boolean isUnionEntriedInDistanceTable = false;
		isUnionEntriedInDistanceTable = distanceDAO.isUpazilaEntriedInDistanceTable(unionID);
		return isUnionEntriedInDistanceTable;
	}


	@Transactional
	public void updateUpazila(UpazilaDistanceDTO upazilaDTO) throws Exception {
		distanceDAO.updateUpazila(upazilaDTO);
	}
	@Transactional
	public void updateUpazilaUnderSameDistrict(List<UpazilaDistanceDTO> upazilaDistanceDTOList) throws Exception {
		for (UpazilaDistanceDTO upazilaDistanceDTO : upazilaDistanceDTOList) {
			this.updateUpazila(upazilaDistanceDTO);
		}
	}
	@Transactional
	public void updateUnion(UnionDistanceDTO unionDTO) throws Exception {		
		distanceDAO.updateUnion(unionDTO);	
	}
	@Transactional
	public UpazilaDistanceDTO insertUpazilaDTO(UpazilaDistanceDTO tempUpazila) throws Exception {
		UpazilaDistanceDTO upazilaDTO = new UpazilaDistanceDTO();
		upazilaDTO = distanceDAO.insertUpazilaDTO(tempUpazila);
		return upazilaDTO;
	}
	@Transactional
	public UnionDistanceDTO insertUnionDTO(UnionDistanceDTO tempUnion) throws Exception {
		UnionDistanceDTO unionDTO = new UnionDistanceDTO();
		unionDTO = distanceDAO.insertUnionDTO(tempUnion);
		return unionDTO;
	}

	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public ArrayList<DistrictDistanceDTO> getMatchedDistricts(String districtValue) throws Exception {
		ArrayList<DistrictDistanceDTO> districtDistanceDTOList = new ArrayList<>();
		districtDistanceDTOList = distanceDAO.getMatchedDistricts(districtValue);
		return districtDistanceDTOList;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public ArrayList<UpazilaDistanceDTO> getMatchedUpazilas(String upazilaValue, String parentDistrictID) throws Exception {
		ArrayList<UpazilaDistanceDTO> upazilaDistanceDTOList = new ArrayList<>();
		upazilaDistanceDTOList = distanceDAO.getMatchedUpazilas(upazilaValue, parentDistrictID);
		return upazilaDistanceDTOList;
	}


	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<UpazilaDistanceDTO> getUpazilaDistanceDTOListByDistrictID(String districtID) throws Exception {
		List<UpazilaDistanceDTO> upazilaDistanceDTOList = new ArrayList<>();
		upazilaDistanceDTOList=distanceDAO.getUpazilaDistanceDTOListByDistrictID(districtID);
		return upazilaDistanceDTOList;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<UnionDistanceDTO> getUnionDistanceDTOListByUpazilaID(String upazilaID) throws Exception {
		List<UnionDistanceDTO> unionDistanceDTOList = new ArrayList<>();
		unionDistanceDTOList=distanceDAO.getUnionDistanceDTOListByUpazilaID(upazilaID);
		return unionDistanceDTOList;
	}

	private long[] getHierarchyOfLocationFromDistrictToUnion(long tempLocationID) throws Exception {
		InventoryDAO inventoryDAO = new InventoryDAO();
		
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		
		long[] structure = {-1,-1,-1}; //districtID, upazilaID, unionID
		
		long locationID = tempLocationID;
		InventoryItem inventoryItem = inventoryDAO.getInventoryItemByItemID(locationID, databaseConnection);
		
		int typeOfLocation = inventoryItem.getInventoryCatagoryTypeID();
		
		structure[typeOfLocation-1]=locationID;
		while(typeOfLocation!=1){
			locationID = inventoryItem.getParentID();
			inventoryItem = inventoryDAO.getInventoryItemByItemID(locationID, databaseConnection);
			typeOfLocation = inventoryItem.getInventoryCatagoryTypeID();
			structure[typeOfLocation-1]=locationID;
		}
		
		return structure;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public double getDistanceBetweenTwoLocation(long fromLocationID, long toLocationID) throws Exception {
		if (fromLocationID <= 0 || toLocationID <= 0) return 0;
		
		double distance = 0;
		
		long[] fromStructure = getHierarchyOfLocationFromDistrictToUnion(fromLocationID);
		long[] toStructure = getHierarchyOfLocationFromDistrictToUnion(toLocationID);
		
		double distanceD1D2 = 0, distanceD1Up1 = 0, distanceD2Up2 = 0, distanceUp1Un1 = 0, distanceUp2Un2 = 0;
		
		if(fromStructure[0]!=toStructure[0]){
			distanceD1D2 = distanceDAO.getDistanceFromDistrictTable(fromStructure[0], toStructure[0]);
			if(fromStructure[1]!=-1){
				distanceD1Up1 = distanceDAO.getDistanceFromUpazilaTable(fromStructure[0], fromStructure[1]);
			}
			if(toStructure[1]!=-1){
				distanceD2Up2 = distanceDAO.getDistanceFromUpazilaTable(toStructure[0], toStructure[1]);
			}
			if(fromStructure[2]!=-1){
				distanceUp1Un1 = distanceDAO.getDistanceFromUnionTable(fromStructure[1], fromStructure[2]);
			}
			if(toStructure[2]!=-1){
				distanceUp2Un2 = distanceDAO.getDistanceFromUnionTable(toStructure[1], toStructure[2]);
			}
		}
		else{
			if((fromStructure[1]!=-1) && (toStructure[1]!=-1) && (fromStructure[1]==toStructure[1])){
				if(fromStructure[2]!=toStructure[2]){
					if(fromStructure[2]!=-1){
						distanceUp1Un1 = distanceDAO.getDistanceFromUnionTable(fromStructure[1], fromStructure[2]);
					}
					if(toStructure[2]!=-1){
						distanceUp2Un2 = distanceDAO.getDistanceFromUnionTable(toStructure[1], toStructure[2]);
					}
				}				
			}
			else{
				if (fromStructure[1]!=-1){
					distanceD1Up1 =  distanceDAO.getDistanceFromUpazilaTable(fromStructure[0], fromStructure[1]);
					if(fromStructure[2]!=-1){
						distanceUp1Un1 = distanceDAO.getDistanceFromUnionTable(fromStructure[1], fromStructure[2]);
					}
				}
				if (toStructure[1]!=-1){
					distanceD2Up2 =  distanceDAO.getDistanceFromUpazilaTable(toStructure[0], toStructure[1]);
					if(toStructure[2]!=-1){
						distanceUp2Un2 = distanceDAO.getDistanceFromUnionTable(toStructure[1], toStructure[2]);
					}
				}
			}
			
		}
		distance = distanceD1D2 + distanceD1Up1 + distanceD2Up2 + distanceUp1Un1 + distanceUp2Un2;
		return distance;
	}

}
