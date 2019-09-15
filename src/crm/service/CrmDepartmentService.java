package crm.service;

import java.util.*;
import annotation.Transactional;
import common.RequestFailureException;
import crm.CrmDepartmentDTO;
import crm.CrmDesignationNode;
import crm.CrmDesignationDTO;
import crm.CrmEmployeeNode;
import crm.dao.CrmDepartmentDAO;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import inventory.InventoryDAO;
import login.LoginDTO;
import util.DatabaseConnectionFactory;
import util.NavigationService;
import util.ServiceDAOFactory;
import inventory.*;
public class CrmDepartmentService implements NavigationService {
	CrmEmployeeService crmEmployeeService = (CrmEmployeeService)ServiceDAOFactory.getService(CrmEmployeeService.class);
	CrmDesignationService crmDesignationService = (CrmDesignationService)ServiceDAOFactory.getService(CrmDesignationService.class);
	InventoryDAO inventoryDAO = new InventoryDAO();
	CrmDepartmentDAO crmDepartmentDAO = new CrmDepartmentDAO();
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return crmDepartmentDAO.getAllDepartmentIDWithSearchCriteria(new Hashtable<>());
	}
	
	
	
	@Transactional
	public void deleteDepartmentByDepartmentID(long departmentID) throws Exception{
		List<CrmEmployeeNode> crmEmployeeRoots = crmEmployeeService.getCrmEmplyeeNodeListByDepartmentID(departmentID);
		for(CrmEmployeeNode crmEmployeeRoot: crmEmployeeRoots){
			crmEmployeeService.deleteEmployeeTreeByTreeRoot(crmEmployeeRoot);
		}
		
		List<CrmDesignationNode> crmDesignationRootNodes = crmDesignationService
				.getDesignationDTOListByDepartmentID(departmentID);
		for(CrmDesignationNode crmDesignationRootNode: crmDesignationRootNodes){
			crmDesignationService.deleteDesignationTreeByDesignationRootNode(crmDesignationRootNode);
		}
		crmDepartmentDAO.deleteDepartmentByDepartmentIDList(Arrays.asList(departmentID));
	}
	

	public static boolean hasAnyEmployee(long departmentID) throws Exception{
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance()
				.getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO==null){
			throw new RequestFailureException("No department found.");
		}
		if(crmDepartmentDTO.getRootDesignationID()==null){
			return false;
		}
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
									.getCrmDesignationDTOByDesignationID(crmDepartmentDTO.getRootDesignationID());
		if(crmDesignationDTO == null){
			throw new Exception("Inconsistent data. Department has root designation"
					+ " ID but no designation found with that ID");
		}
		return CrmAllEmployeeRepository.getInstance()
				.hasAnyEmployeeHavingCategoyID(crmDesignationDTO.getID());
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return crmDepartmentDAO.getAllDepartmentIDWithSearchCriteria(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		List<CrmDepartmentDTO> crmDepartmentDTOs = (List<CrmDepartmentDTO> ) 
				crmDepartmentDAO.getCrmDeptListByIDList(recordIDs);
		populateDepartmentDTOsWithNames(crmDepartmentDTOs, InventoryConstants.CATEGORY_ID_DISTRICT);
		populateDepartmentDTOsWithNames(crmDepartmentDTOs, InventoryConstants.CATEGORY_ID_UPAZILA);
		populateDepartmentDTOsWithNames(crmDepartmentDTOs, InventoryConstants.CATEGORY_ID_UNION);
		return crmDepartmentDTOs;
	}
	@Transactional
	public void populateDepartmentDTOWithNames(CrmDepartmentDTO crmDepartmentDTO, int categoryType) throws Exception{
		populateDepartmentDTOsWithNames(Arrays.asList(crmDepartmentDTO), categoryType);
	}
	private void populateDepartmentDTOsWithNames(List<CrmDepartmentDTO> crmDepartmentDTOs, int categoryType) throws Exception {
		List<Long> placeIDs = getPlaceIDListByDepartmentDTOs(crmDepartmentDTOs, categoryType);
		Map<Long, String> mapOfPlaceNameToPlaceID = getMapOfPlaceNameToPlaceID(placeIDs);
		setPlaceNameUsingMapOfPlaceNameByPlaceID(crmDepartmentDTOs, mapOfPlaceNameToPlaceID, categoryType);
		
	}

	private void setPlaceNameUsingMapOfPlaceNameByPlaceID(List<CrmDepartmentDTO> crmDepartmentDTOs, Map<Long, String> mapOfPlaceNameToPlaceID, int categoryType) {
		for(CrmDepartmentDTO crmDepartmentDTO: crmDepartmentDTOs){
			if(categoryType == InventoryConstants.CATEGORY_ID_DISTRICT){
				crmDepartmentDTO.setDistrictName(mapOfPlaceNameToPlaceID.get(crmDepartmentDTO.getDistrictID()));
			}else if(categoryType == InventoryConstants.CATEGORY_ID_UPAZILA){
				crmDepartmentDTO.setUpazilaName(mapOfPlaceNameToPlaceID.get(crmDepartmentDTO.getUpazilaID()));
			}else if(categoryType == InventoryConstants.CATEGORY_ID_UNION){
				crmDepartmentDTO.setUnionName(mapOfPlaceNameToPlaceID.get(crmDepartmentDTO.getUnionID()));
			}
		}
	}

	private Map<Long, String> getMapOfPlaceNameToPlaceID(List<Long> placeIDs) throws Exception {
		return inventoryDAO.getInventoryItemNameListByItemIDList(placeIDs, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}

	private List<Long> getPlaceIDListByDepartmentDTOs(List<CrmDepartmentDTO> crmDepartmentDTOs, int categoryType) {
		List<Long> placeIDs = new ArrayList<>();
		for(CrmDepartmentDTO crmDepartmentDTO: crmDepartmentDTOs){
			if(categoryType == InventoryConstants.CATEGORY_ID_DISTRICT){
				placeIDs.add(crmDepartmentDTO.getDistrictID());
			}else if(categoryType == InventoryConstants.CATEGORY_ID_UPAZILA){
				placeIDs.add(crmDepartmentDTO.getUpazilaID());
			}else if(categoryType == InventoryConstants.CATEGORY_ID_UNION){
				placeIDs.add(crmDepartmentDTO.getUnionID());
			}
		}
		return placeIDs;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<InventoryItem> getAreaNameByPartialMatch(String partialName, Long parentID, int categoryType) throws Exception {
		 return inventoryDAO.getInventoryItemListByParentItemAndCatagoryID(parentID, categoryType, partialName, DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}
	@Transactional
	public void insertCrmDepartmentDTO(CrmDepartmentDTO crmDepartmentDTO) throws Exception{
		validateDepartment(crmDepartmentDTO);
		crmDepartmentDAO.insertCrmDepartmentDTO(crmDepartmentDTO);
	}

	private void validateDepartment(CrmDepartmentDTO crmDepartmentDTO)throws Exception {
		Long districtID, upazilaID, unionID;
		districtID = crmDepartmentDTO.getDistrictID();
		upazilaID = crmDepartmentDTO.getUpazilaID();
		unionID = crmDepartmentDTO.getUnionID();
		checkChildWithNoParent(districtID, upazilaID, unionID);
		checkConsistencyWithDB(districtID, upazilaID, unionID);
		// two check : one for duplicate, another in parent child check.
	}

	private void checkConsistencyWithDB(Long districtID, Long upazilaID, Long unionID) throws Exception {
		checkUnionUpazilaRelationship(unionID, upazilaID);
		checkUpazilaDistrictRelationship(upazilaID, districtID);
	}


	private void checkUpazilaDistrictRelationship(Long upazilaID, Long districtID) throws Exception {
		if(upazilaID != null){
			InventoryItem upazilaInventory = inventoryDAO.getInventoryItemByItemID(upazilaID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
			if(upazilaInventory == null){
				throw new RequestFailureException("No union found with the provided upazila ID");
			}
			if(upazilaInventory.getInventoryCatagoryTypeID() != 2){
				//2 == categoryType
				throw new RequestFailureException("Invalid category type for upazila");
			}
			if(!districtID.equals(upazilaInventory.getParentID())){
				throw new RequestFailureException("There is no upazilas under the specified district");
			}
		}
	}

	private void checkUnionUpazilaRelationship(Long unionID, Long upazilaID) throws Exception {
		if(unionID != null){
			InventoryItem unionInventory = inventoryDAO.getInventoryItemByItemID(unionID, DatabaseConnectionFactory.getCurrentDatabaseConnection());
			if(unionInventory == null){
				throw new RequestFailureException("No union found with the provided union ID");
			}
			if(unionInventory.getInventoryCatagoryTypeID() != 3){
				// 3 == category Type
				throw new RequestFailureException("Invalid category type for union.");
			}
			if(!upazilaID.equals(unionInventory.getParentID())){
				throw new RequestFailureException("There is no unions under the specified upazila.");
			}
		}
	}

	private void checkChildWithNoParent(Long districtID, Long upazilaID, Long unionID)throws Exception {
		if(districtID == null && upazilaID != null){
			throw new RequestFailureException("You must select a "
					+ "district before selecting a upazilla");
		}
		if(unionID != null && upazilaID == null){
			throw new RequestFailureException("You must select "
					+ "an upazila before selecting a union.");
		}
	}
	@Transactional
	public void deleteDepartmentByDepartmentIDList(List<Long> departmentIDs) throws Exception{
		if(departmentIDs.isEmpty()){
			throw new RequestFailureException("No department id is provided for delete");
		}
		
		for(Long departmentID : departmentIDs){
			deleteDepartmentByDepartmentID(departmentID);
		}
//		crmDepartmentDAO.deleteDepartmentByDepartmentIDList(departmentIDs);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmDepartmentDTO> getCrmDepartmentDTOsByPartialDeptName(String partialName, Long districtID, Long upazilaID, Long unionID) throws Exception {
		return CrmDepartmentRepository.getInstance().getDepartmentDTOsByPartialDeptNameAndLocationIDs(districtID, upazilaID, unionID, partialName);
	}
	@Transactional
	public void updateCrmDepartmentDTO(CrmDepartmentDTO crmDepartmentDTO) throws Exception {
		CrmDepartmentDTO prevCrmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(crmDepartmentDTO.getID());
		if(prevCrmDepartmentDTO == null){
			throw new RequestFailureException("Database inconsistency found with departmentID" + crmDepartmentDTO.getID());
		}
		crmDepartmentDTO.setRootDesignationID(prevCrmDepartmentDTO.getRootDesignationID());
		validateDepartment(crmDepartmentDTO);
		crmDepartmentDAO.updateDepartmentDTO(crmDepartmentDTO);
	}

	
}
