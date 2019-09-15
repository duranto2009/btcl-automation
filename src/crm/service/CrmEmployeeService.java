package crm.service;

import static common.StringUtils.containsIgnoringCase;
import static util.ModifiedSqlGenerator.populateObjectFromOtherObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import annotation.Transactional;
import common.CategoryConstants;
import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;
import crm.CrmComplainDTO;
import crm.CrmDepartmentDTO;
import crm.CrmDesignationDTO;
import crm.CrmEmployeeDTO;
import crm.CrmEmployeeDesignationValuePair;
import crm.CrmEmployeeDetails;
import crm.CrmEmployeeNode;
import crm.dao.CrmComplainDAO;
import crm.dao.CrmDesignationDAO;
import crm.dao.CrmEmployeeDAO;
import crm.inventory.CRMInventoryCatagoryType;
import crm.inventory.CRMInventoryDAO;
import crm.inventory.CRMInventoryItem;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import inventory.InventoryConstants;
import inventory.InventoryDAO;
import inventory.InventoryItem;
import login.LoginDTO;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.ServiceDAOFactory;

public class CrmEmployeeService implements NavigationService {
	private static Integer mutextLockForEmployee = new Integer(0);
	Logger logger = Logger.getLogger(this.getClass());
	CrmEmployeeDAO crmEmployeeDAO = new CrmEmployeeDAO();
	CRMInventoryDAO crmInventoryDAO = new CRMInventoryDAO();
	CrmComplainDAO crmComplainDAO = new CrmComplainDAO();
	CrmDesignationDAO designationDAO = new CrmDesignationDAO();
	CrmComplainService crmComplainService = new CrmComplainService();
	InventoryDAO inventoryDAO = new InventoryDAO();
	public void deleteEmployeeTreeByTreeRoot(CrmEmployeeNode crmRootEmployeeNode) throws Exception{
		for(CrmEmployeeNode childEmployeeNode: crmRootEmployeeNode.getChildCrmEmployeeNodeList()){
			deleteEmployeeTreeByTreeRoot(childEmployeeNode);
		}
		crmEmployeeDAO.deleteEmployeeById(crmRootEmployeeNode.getCrmEmployeeID());
		crmInventoryDAO.deleteCRMInventoryItemByItemID(crmRootEmployeeNode.getID(), DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}


	public List<CrmEmployeeNode> getCrmEmplyeeNodeListByDepartmentID(long departmentID) throws Exception{

		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO == null||crmDepartmentDTO.getRootDesignationID()==null){
			return Collections.emptyList();
		}
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(crmDepartmentDTO.getRootDesignationID());
		if(crmDesignationDTO == null){
			return Collections.emptyList();
		}
		List<CrmEmployeeDTO> crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance()
													.getCrmEmployeeDTOListByInventoryCategoryID(crmDesignationDTO.getID());
		if(crmEmployeeDTOs.isEmpty()){
			return Collections.emptyList();
		}
		

		List<CrmEmployeeNode> crmEmployeeNodes = new ArrayList<>();
		
		for(CrmEmployeeDTO crmEmployeeDTO : crmEmployeeDTOs){
			CrmEmployeeNode crmEmployeeNode = getEmployeeNodeByEmpolyeeDTO(crmEmployeeDTO);
			crmEmployeeNodes.add(crmEmployeeNode);
		}
		
		return crmEmployeeNodes;
	}
	
	
	private void addDepartmentHeadEmployee(CrmEmployeeDTO crmEmployeeDTO,long departmentID) throws Exception{
		CRMInventoryItem parentCRMInventoryItem = crmInventoryDAO.getCRMInventoryItemByItemID(crmEmployeeDTO.getParentID(),
				DatabaseConnectionFactory.getCurrentDatabaseConnection());
		crmEmployeeDTO.setPathFromRootToParent(parentCRMInventoryItem.getPathFromRootToParent() + "/" + parentCRMInventoryItem.getID());
	
	}
	
	/*
	 * Here normal employee means those who are not the head of the department.
	 */
	private void addNormalEmployee(CrmEmployeeDTO crmEmployeeDTO,long departmentID) throws Exception{

		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO == null){
			throw new RequestFailureException("No department found");
		}
		if(crmDepartmentDTO.getRootDesignationID()==null){
			throw new RequestFailureException("No head of the department designation found");
		}
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByDesignationID(crmDepartmentDTO.getRootDesignationID());
		if(crmDesignationDTO == null){
			throw new RequestFailureException("No head of the department designation found");
		}
		if(!CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByInventoryCategoryID(crmDesignationDTO.getID()).isEmpty()){
			throw new RequestFailureException("This department has already a head of the department");
		}
		
		
		crmEmployeeDTO.setPathFromRootToParent("/");
	}
	
	@Transactional
	public void insertEmployee(CrmEmployeeDTO crmEmployeeDTO,long departmentID) throws Exception {
		// checks if there exists a inventory Id with parent ID and a
		// crmEmployee poingting to that inventory item and both are non deleted
		// check also the designation id validityr . consult with me.
		
	
		if(crmEmployeeDTO.getUserID() == null) {
			throw new RequestFailureException("Yo must select a username from drop down menu");
		}
		synchronized (mutextLockForEmployee) {
			isValidAddRequest(crmEmployeeDTO.getInventoryCatagoryTypeID(), crmEmployeeDTO.getParentID());
			
			validatePermissionOfCrmEmployeeAccordingToCrmDesignation(crmEmployeeDTO);
			if (crmEmployeeDTO.getParentID() != null) {
				addDepartmentHeadEmployee(crmEmployeeDTO, departmentID);
			} else {
				addNormalEmployee(crmEmployeeDTO, departmentID);
			}
			crmEmployeeDAO.insertEmployeeDTO(crmEmployeeDTO);
		}

	}
	
	private void isValidAddRequest(int categoryID, Long parentItemID) throws Exception {

		CRMInventoryCatagoryType inventoryCatagoryType = crmInventoryDAO.getInventoryCatagoryByCatagoryID(categoryID,
				DatabaseConnectionFactory.getCurrentDatabaseConnection());// categoryID;
		if (inventoryCatagoryType == null || inventoryCatagoryType.isDeleted()) {
			throw new RequestFailureException("Not inventory category found with categoryID = " + categoryID);
		}
		if (parentItemID == null && inventoryCatagoryType.getParentCatagoryTypeID() != null) {
			throw new RequestFailureException("Invalid parent item is selected. Parent item id null and category's parent category id not null");
		}

		if (inventoryCatagoryType.getParentCatagoryTypeID() == null && parentItemID == null) {
			return; // for root employee
		}

		CRMInventoryItem parentItem = crmInventoryDAO.getCRMInventoryItemByItemID(parentItemID,
				DatabaseConnectionFactory.getCurrentDatabaseConnection());

		if (parentItemID != null && (parentItem == null || parentItem.isDeleted())) {
			throw new RequestFailureException("No parent item found with ID " + parentItemID);
		}

		if (parentItem != null) {
			if (!parentItem.getInventoryCatagoryTypeID().equals(inventoryCatagoryType.getParentCatagoryTypeID())) {
				throw new RequestFailureException(
						"Parent item with id = " + parentItemID + " should be a item of category with category id = "
								+ inventoryCatagoryType.getParentCatagoryTypeID() + " while it is of category id "
								+ parentItem.getInventoryCatagoryTypeID());
			}
		}
		

	}
	
	private List<Long> getEmployeeIDListByRootEmployeeID(CrmEmployeeDTO rootEmployeeDTO) 
			throws Exception{
		if(rootEmployeeDTO == null){
			Collections.emptyList();
		}
		long employeeID = rootEmployeeDTO.getCrmEmployeeID();
		List<CrmEmployeeDTO> childEmployeeList = CrmAllEmployeeRepository
				.getInstance().getChildEmployeeDTOListByEmployeeID(employeeID);
		List<Long> employeeIDList = new ArrayList<>();
		employeeIDList.add(employeeID);
		for(CrmEmployeeDTO childEmployeeDTO: childEmployeeList){
			employeeIDList.addAll(getEmployeeIDListByRootEmployeeID(childEmployeeDTO));
		}
		return employeeIDList;
	}
	
	@Transactional
	public void updateEmployee(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		/// updates the name and the permissions. nothing else. ? and also the
		/// userID. Allow null in userID field.
		// check if crmEmployeeDTO id and inventory item id is ok or not ???
		// use login dto get crmEmpoyeeID who is updating the permissions. An
		/// employee can only update this descendant.
		// also check if the user ID assigned in this employeeDTO is assigned to
		/// other employee. If the user ID is assigned to other employee throw
		/// Excepton.
		synchronized (mutextLockForEmployee) {
			CrmEmployeeDTO crmEmployeeDTOFromRepo = CrmAllEmployeeRepository.getInstance()
														.getCrmEmployeeDTOByEmployeeID(crmEmployeeDTO.getCrmEmployeeID());
			if(crmEmployeeDTOFromRepo==null){
				throw new RequestFailureException("No requested employee found with given ID");
			}
			
			
			crmEmployeeDTO.setParentID(crmEmployeeDTOFromRepo.getParentID());
			crmEmployeeDTO.setID(crmEmployeeDTOFromRepo.getID());
			
			
			if(crmEmployeeDTO.getParentID()!=null){
				CrmEmployeeDTO parentEmployeeDTO = CrmAllEmployeeRepository.getInstance()
						.getCrmEmployeeDTOByCRMInventoryItemID(crmEmployeeDTO.getParentID());
				validatePermissionConsistencyWithParent(parentEmployeeDTO, crmEmployeeDTO);
			}
			
			String sql = getUpdateQueryStringForChildEmployee(crmEmployeeDTO);
			DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement()
					.executeUpdate(sql);
			
			crmEmployeeDAO.updateEmployeeDTO(crmEmployeeDTO);
		}
	}
	
	
	private void validatePermissionConsistencyWithParent(CrmEmployeeDTO parentEmployeeDTO, CrmEmployeeDTO crmEmployeeDTO) throws Exception {

		if(parentEmployeeDTO!=null){
			for(Field field: CrmEmployeeDTO.class.getDeclaredFields()){
				field.setAccessible(true);
				if(isValidPermissionField(field) && checkConsistentParentPermissionWithChild(field,parentEmployeeDTO, crmEmployeeDTO)){
					throw new RequestFailureException("Parent Employee does not have the permission " + field.getName());
				}
			}
		}
	}

	private boolean checkConsistentParentPermissionWithChild(Field field, CrmEmployeeDTO parentEmployeeDTO,
			CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		return (field.getBoolean(parentEmployeeDTO) == false )&& (field.getBoolean(crmEmployeeDTO) == true);
	}

	private boolean isValidPermissionField(Field field){
		return field.getName().startsWith("hasPermission") && !field.getName().endsWith("Permission");
	}
	private String getUpdateQueryStringForChildEmployee(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		
		List<Long> childEmployeeIDs = getEmployeeIDListByRootEmployeeID(crmEmployeeDTO);
		
		String updateString = getUpdateQueryForEmployeePermissionConsistency(crmEmployeeDTO);
		
		updateString += " and " + ModifiedSqlGenerator.getColumnName(CrmEmployeeDTO.class, "crmEmployeeID")
							+ " IN " + StringUtils.getCommaSeparatedString(childEmployeeIDs);
		return updateString;
	}
	
	
	private String getUpdateQueryForEmployeePermissionConsistency(CrmEmployeeDTO crmEmployeeDTO)
			throws Exception{
		
		List<String>validColumnNames = getValidColumnNames(crmEmployeeDTO);
		String updateQuery = getUpdateQueryForValidColumnNames(validColumnNames);
		return updateQuery;
	}
	


	private String getUpdateQueryForValidColumnNames(List<String> validColumnNames) throws Exception {
		String updateQuery = "";
		for(String columnName: validColumnNames){
			if(!updateQuery.isEmpty()){
				updateQuery+=",";
			}
			updateQuery+=(columnName+"=0");
		}
		if(!updateQuery.isEmpty()){
			updateQuery+=",";
		}
		updateQuery+=ModifiedSqlGenerator.getLastModificationTimeColumnName(CrmEmployeeDTO.class)+"="+CurrentTimeFactory.getCurrentTime();
		updateQuery = "update "+ModifiedSqlGenerator.getTableName(CrmEmployeeDTO.class)+" set "+updateQuery;
		return updateQuery;
	}

	private List<String> getValidColumnNames(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		List<String> columnNames = new ArrayList<>();
		for(Field field: CrmEmployeeDTO.class.getDeclaredFields()){
			field.setAccessible(true);
			if(isValidPermissionField(field) && field.getBoolean(crmEmployeeDTO)==false){
				columnNames.add(ModifiedSqlGenerator.getColumnName(CrmEmployeeDTO.class, field.getName()));
			}
		}
		return columnNames;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeNode> getEmployeeNodeByUserID(long userID) throws Exception {
		List<CrmEmployeeDTO> crmEmployeeDTOs = crmEmployeeDAO.getEmployeeDTOListByUserID(userID);
		List<CrmEmployeeNode> crmEmployeeNodes = new ArrayList<>();
		
		for(CrmEmployeeDTO crmEmployeeDTO: crmEmployeeDTOs){
			crmEmployeeNodes.add(getEmployeeNodeByEmployeeID(crmEmployeeDTO.getCrmEmployeeID()));
		}
		return crmEmployeeNodes;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeNode> getEmployeeNodesByEmployeeID(long userID) throws Exception {
		List<CrmEmployeeNode> rootEmployeeNodes = new ArrayList<>();
		List<CrmEmployeeDTO> crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(userID);
		
		for(CrmEmployeeDTO crmEmployeeDTO: crmEmployeeDTOs){
			CrmEmployeeDTO tempertedCrmEmployeeDTO = new CrmEmployeeDTO();
			populateObjectFromOtherObject(crmEmployeeDTO, tempertedCrmEmployeeDTO, CrmEmployeeDTO.class);
			tempertedCrmEmployeeDTO.setParentID(null);
			
			CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
			CrmDesignationDTO rootDesignationDTO =  CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
	        CrmDepartmentDTO currentEmployeeDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootDesignationDTO.getDesignationID());
	        tempertedCrmEmployeeDTO.setNOC(currentEmployeeDepartmentDTO.isNOC());
	        tempertedCrmEmployeeDTO.setDepartmentName(currentEmployeeDepartmentDTO.getDepartmentName());
	        
			CrmEmployeeNode crmEmployeeNode = getEmployeeNodeByEmpolyeeDTO(tempertedCrmEmployeeDTO);
			rootEmployeeNodes.add(crmEmployeeNode);
		}
		
		return rootEmployeeNodes;
	
	}

	@Transactional
	public void deleteEmployeeByID(long employeeID) throws Exception {
		CrmComplainService crmComplainService = (CrmComplainService) ServiceDAOFactory.getService(CrmComplainService.class);
		synchronized (mutextLockForEmployee) {
			CrmEmployeeDTO employeeDTO = crmEmployeeDAO.getEmployeeDTOByEmployeeID(employeeID);
			if (crmInventoryDAO.hasChildOfCRMInventoryItem(employeeDTO.getID(),
					DatabaseConnectionFactory.getCurrentDatabaseConnection())) {
				throw new RequestFailureException("Employee can not be deleted as it has child(s)");
			}
			if(crmComplainService.getCrmComplainListByResolverID(employeeID).size() > 0 || crmComplainService.getCrmComplainListByAssignerID(employeeID).size() > 0){
				throw new RequestFailureException("Employee can not be deleted as it has complains");
			}
			crmInventoryDAO.deleteCRMInventoryItemByItemID(employeeDTO.getID(),
					DatabaseConnectionFactory.getCurrentDatabaseConnection());
			crmEmployeeDAO.deleteEmployeeById(employeeID);
		}
	}

	private List<CrmEmployeeNode> getEmployeeListByParentEmployeeDTO(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		List<CrmEmployeeNode> crmEmployeeNodes = new ArrayList<>();
		List<CrmEmployeeDTO> childEmployeeList = CrmAllEmployeeRepository.getInstance().getChildEmployeeDTOListByEmployeeID(crmEmployeeDTO.getCrmEmployeeID());
		for (CrmEmployeeDTO childEmployeeDTO : childEmployeeList) {
			if(childEmployeeDTO == null||childEmployeeDTO.isDeleted()){
				continue;
			}
			CrmEmployeeNode crmEmployeeNode = new CrmEmployeeNode();

			populateObjectFromOtherObject(childEmployeeDTO, crmEmployeeNode, CrmEmployeeDTO.class);
			crmEmployeeNode.setChildCrmEmployeeNodeList(getEmployeeListByParentEmployeeDTO(crmEmployeeNode));
			crmEmployeeNodes.add(crmEmployeeNode);
		}
		return crmEmployeeNodes;
	}

	private List<CrmEmployeeDTO> getRootEmployeeList() throws Exception {
		return CrmAllEmployeeRepository.getInstance().getRootEmployeeList();
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeNode> getAllRootEmployeeNodeWithDescendantEmployeeList() throws Exception {
		List<CrmEmployeeDTO> rootEmployeeDTOs = getRootEmployeeList();
		List<CrmEmployeeNode> rootEmployeeNodes = new ArrayList<>();

		for (CrmEmployeeDTO employeeDTO : rootEmployeeDTOs) {
			
			CrmEmployeeDTO tempertedCrmEmployeeDTO = new CrmEmployeeDTO();
			populateObjectFromOtherObject(employeeDTO, tempertedCrmEmployeeDTO, CrmEmployeeDTO.class);
			tempertedCrmEmployeeDTO.setParentID(null);
			
			CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(employeeDTO.getInventoryCatagoryTypeID());
			CrmDesignationDTO rootDesignationDTO =  CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
	        CrmDepartmentDTO currentEmployeeDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootDesignationDTO.getDesignationID());
	        tempertedCrmEmployeeDTO.setNOC(currentEmployeeDepartmentDTO.isNOC());
	        tempertedCrmEmployeeDTO.setDepartmentName(currentEmployeeDepartmentDTO.getDepartmentName());
			CrmEmployeeNode crmEmployeeNode = getEmployeeNodeByEmpolyeeDTO(tempertedCrmEmployeeDTO);
			rootEmployeeNodes.add(crmEmployeeNode);
		}

		return rootEmployeeNodes;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmEmployeeNode getEmployeeNodeByEmployeeID(long employeeID) throws Exception {
		CrmEmployeeDTO employeeDTO = crmEmployeeDAO.getEmployeeDTOByEmployeeID(employeeID);
		CrmEmployeeNode employeeNode = getEmployeeNodeByEmpolyeeDTO(employeeDTO);
		return employeeNode;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmEmployeeDTO getEmployeeDTOByEmployeeID(long employeeID) throws Exception {
		return CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(employeeID);
	}

	public CrmEmployeeNode getEmployeeNodeByEmpolyeeDTO(CrmEmployeeDTO crmEmployeeDTO) throws Exception {

		CrmEmployeeNode employeeNode = new CrmEmployeeNode();
		populateObjectFromOtherObject(crmEmployeeDTO, employeeNode, CrmEmployeeDTO.class);
		employeeNode.setChildCrmEmployeeNodeList(getEmployeeListByParentEmployeeDTO(employeeNode));
		return employeeNode;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public boolean isAncestor(long ancestorID, long descendantID) throws Exception {
		return crmInventoryDAO.isAncestor(ancestorID, descendantID);
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeDesignationValuePair> getDescendantEmployeeListByPartialName(String partialName
			,Long departmentID,LoginDTO loginDTO)
			throws Exception {
		
		List<CrmEmployeeDesignationValuePair> crmEmployeeDesignationValuePairs = new ArrayList<>();
		
		for(CrmEmployeeDTO crmEmployeeDTO: CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(loginDTO.getUserID())){
			 crmEmployeeDesignationValuePairs.addAll(
					 getDesignationEmloyeeListByPartialNameAndAnsestorCrmEmployeeID(partialName
							 , crmEmployeeDTO.getCrmEmployeeID(),departmentID,loginDTO));
		}
		
		return crmEmployeeDesignationValuePairs;
	}

	private void validatePermissionOfCrmEmployeeAccordingToCrmDesignation(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		// CRMInventoryCatagoryType inventoryCatagoryType = inventoryDAO.
		CrmDesignationDTO crmDesignationDTO = designationDAO
				.getPartialDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
		if (!crmDesignationDTO.isHasPermissionToAssignComplain() && crmEmployeeDTO.isHasPermissionToAssignComplain()) {
			
			throw new RequestFailureException("This designation has no PermissionToAssignComplain");
		}
		if (!crmDesignationDTO.isHasPermissionToChangeStatus() && crmEmployeeDTO.isHasPermissionToChangeStatus()) {
			
			throw new RequestFailureException("This designation has no PermissionToChangeStatus");
		}
		if (!crmDesignationDTO.isHasPermissionToForwardComplain() && crmEmployeeDTO.isHasPermissionToForwardComplain()) {
			
			throw new RequestFailureException("This designation has no PermissionToForwardComplain");
		}
		if (!crmDesignationDTO.isHasPermissionToPassComplain() && crmEmployeeDTO.isHasPermissionToPassComplain()) {
			throw new RequestFailureException("This designation has no PermissionToPassComplain");
		}
		if (!crmDesignationDTO.isHasPermissionToPassComplainAssigningPermission() && crmEmployeeDTO.isHasPermissionToPassComplainAssigningPermission()) {
			throw new RequestFailureException("This designation has no PermissionToPassComplainAssigningPermission");
		}
		if (!crmDesignationDTO.isHasPermissionToPassComplainForwardingPermission() && crmEmployeeDTO.isHasPermissionToPassComplainForwardingPermission()) {
			throw new RequestFailureException("This designation has no PermissionToPassComplainForwardingPermission");
		}
		if (!crmDesignationDTO.isHasPermissionToPassComplainPassingPermission() && crmEmployeeDTO.isHasPermissionToPassComplainPassingPermission()) {
			throw new RequestFailureException("This designation has no PermissionToPassComplainPassingPermission");
		}
		if (!crmDesignationDTO.isHasPermissionToPassStatusChangingPermission() && crmEmployeeDTO.isHasPermissionToPassStatusChangingPermission()) {
			throw new RequestFailureException("This designation has no PermissionToPassStatusChangingPermission");
		}
	}

	private void checkUserCanPassPermissionsAndTakeAction(long loginEmployeeID, CrmEmployeeDTO crmEmployeeDTO)
			throws Exception {
		CrmEmployeeDTO loginCrmEmployeeDTO = crmEmployeeDAO.getEmployeeDTOByEmployeeID(loginEmployeeID);

		if (!loginCrmEmployeeDTO.isHasPermissionToPassComplainAssigningPermission()
				&& crmEmployeeDTO.isHasPermissionToPassComplainAssigningPermission()) {
			throw new RequestFailureException("You have no permission pass ComplainAssigningPermission!");
		}
		if (!loginCrmEmployeeDTO.isHasPermissionToPassComplainForwardingPermission()
				&& crmEmployeeDTO.isHasPermissionToPassComplainForwardingPermission()) {
			throw new RequestFailureException("You have no permission pass ComplainForwardingPermission!");
		}
		if (!loginCrmEmployeeDTO.isHasPermissionToPassComplainPassingPermission()
				&& crmEmployeeDTO.isHasPermissionToPassComplainPassingPermission()) {
			throw new RequestFailureException("You have no permission pass ComplainPassingPermission!");
		}
		if (!loginCrmEmployeeDTO.isHasPermissionToPassStatusChangingPermission()
				&& crmEmployeeDTO.isHasPermissionToPassStatusChangingPermission()) {
			throw new RequestFailureException("You have no permission pass StatusChangingPermission!");
		}
	}

	
	private List<CrmEmployeeDesignationValuePair> getDesignationEmloyeeListByPartialNameAndAnsestorCrmInventoryDTO(String partialName,
			CRMInventoryItem crmCRMInventoryItem){
		List<CrmEmployeeDesignationValuePair> crmEmployeeDesignationValuePairs = new ArrayList<>();
		if( StringUtils.isBlank(partialName) || containsIgnoringCase(crmCRMInventoryItem.getName(), partialName) ){
			CRMInventoryCatagoryType crmCategoryTypeOfThisEmployee = CrmAllDesignationRepository.getInstance()
					.getCrmInventoryCategoryTypeDTOByInventoryCategoryID(crmCRMInventoryItem.getInventoryCatagoryTypeID()); 
			if(crmCategoryTypeOfThisEmployee == null){
				throw new RequestFailureException("Inconsistent data."
						+ " No crmCategory found with inventory item ID "+crmCRMInventoryItem.getID());
			}
			
			String designationNameOfThisEmployee = crmCategoryTypeOfThisEmployee.getName();
			
			CrmEmployeeDTO partialCrmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
					.getCrmEmployeeDTOByCRMInventoryItemID(crmCRMInventoryItem.getID()); 
			if(partialCrmEmployeeDTO == null){
				throw new RequestFailureException("Inconsistent data. "
						+ "No crmEmployee found with inventory item ID "+crmCRMInventoryItem.getID());
			}
			
			if(partialCrmEmployeeDTO !=null){
					long crmEmployeeID = partialCrmEmployeeDTO.getCrmEmployeeID();
					CrmEmployeeDesignationValuePair designationValuePair = new CrmEmployeeDesignationValuePair(crmEmployeeID, crmCRMInventoryItem.getName(), designationNameOfThisEmployee);
					crmEmployeeDesignationValuePairs.add(designationValuePair);
			}
		}
		for(CRMInventoryItem crmChildCRMInventoryItem: CrmAllEmployeeRepository.getInstance()
				.getChildCRMInventoryItemListByInventoryID(crmCRMInventoryItem.getID())){
			crmEmployeeDesignationValuePairs.addAll(
					getDesignationEmloyeeListByPartialNameAndAnsestorCrmInventoryDTO(partialName, crmChildCRMInventoryItem));
		}
		return crmEmployeeDesignationValuePairs;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeDesignationValuePair> getDesignationEmloyeeListByPartialNameAndCrmComplainID(String partialName,
			long complainID,Long departmentID,LoginDTO ansestorLoginDTO) throws Exception{
		CrmComplainDTO crmComplainDTO = crmComplainDAO.getComplainDTOByComplainID(complainID);
		if(crmComplainDTO == null){
			throw new RequestFailureException("No such complain found");
		}
		return getDesignationEmloyeeListByPartialNameAndAnsestorCrmEmployeeID(partialName, 
				crmComplainDTO.getComplainResolverID(), departmentID, ansestorLoginDTO);
	}
	public List<CrmEmployeeDesignationValuePair> getDesignationEmloyeeListByPartialNameAndAnsestorCrmEmployeeID(String partialName,
			long ancestorEmployeeID,Long departmentID,LoginDTO ansestorLoginDTO) throws Exception{
		
		
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(ancestorEmployeeID);
		if(crmEmployeeDTO == null){
			throw new RequestFailureException("No such CrmEmployee found with id "+ancestorEmployeeID);
		}
		
		CrmDesignationDTO crmDesignationDTOOfAncestorEmployee = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
		CrmDesignationDTO rootCrmDesignationDTOOfAncestorEmployee = CrmAllDesignationRepository.getInstance()
				.getRootDesignationDTOByDesignationID(crmDesignationDTOOfAncestorEmployee.getDesignationID());
		
		
		if(crmEmployeeDTO.getUserID() == null || !crmEmployeeDTO.getUserID().equals(ansestorLoginDTO.getUserID())){
			throw new RequestFailureException("You can not act as "+crmEmployeeDTO.getName());
		}
		List<CrmEmployeeDesignationValuePair> crmDesignationValuePairs = Collections.emptyList();
		
		
		if(departmentID != null){
			// attemps to forward
			if(!crmEmployeeDTO.isHasPermissionToForwardComplain()){
				throw new RequestFailureException("You don't have permission to forward complain");
			}
		// check if this department is the department of the logged in user
			
			CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance()
													.getCrmDepartmentDTOByDepartmentID(departmentID);
			if(crmDepartmentDTO != null && crmDepartmentDTO.getRootDesignationID()!=null){
				
				CrmDesignationDTO rootDesignationDTO = CrmAllDesignationRepository.getInstance()
															.getCrmDesignationDTOByDesignationID(crmDepartmentDTO.getRootDesignationID());
				
				if(rootDesignationDTO == null){
					throw new RequestFailureException("No employee exists in this department");
				}
				


				
				List<CrmEmployeeDTO> crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance()
										.getCrmEmployeeDTOListByInventoryCategoryID(rootDesignationDTO.getID());
				if(crmEmployeeDTOs.isEmpty()){
					throw new RequestFailureException("No employee exists in this department");
				}
				
				CRMInventoryItem rootCrmInventoryItem = CrmAllEmployeeRepository.getInstance()
														.getCrmEmployeeDTOByEmployeeID(
																crmEmployeeDTOs.get(0).getCrmEmployeeID());
				if(rootCrmInventoryItem == null){
					throw new RequestFailureException("No employee exists in this department");
				}
				
				crmDesignationValuePairs = getDesignationEmloyeeListByPartialNameAndAnsestorCrmInventoryDTO(partialName
							, rootCrmInventoryItem);
						
					
				
			}
		}else{
			// inside own department
			CRMInventoryItem crmInventoryItem = CrmAllEmployeeRepository.getInstance()
													.getCrmEmployeeDTOByEmployeeID(ancestorEmployeeID);
			if(crmInventoryItem == null){
				throw new RequestFailureException("The employee , yor are acting as is deleted. Please reload the page.");
			}

			crmDesignationValuePairs = getDesignationEmloyeeListByPartialNameAndAnsestorCrmInventoryDTO(partialName
					, crmInventoryItem);
			crmDesignationValuePairs.remove(0); // removes the self from the list as one can not pass complain to oneself
		}
		
		return crmDesignationValuePairs;
	}

	public void pomoteCrmEmployee(long crmEmployeeID,long lastModificationTime) throws Exception{
		CrmEmployeeDTO prevCrmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmEmployeeID);
		if(prevCrmEmployeeDTO == null){
			throw new RequestFailureException("No crm Employee found with employee id "+crmEmployeeID);
		}
		if(prevCrmEmployeeDTO.getParentID() == null){
			throw new RequestFailureException("Root employee can not be promoted");
		}
		if(prevCrmEmployeeDTO.getLastModificationTime()!=lastModificationTime){
			throw new RequestFailureException("Crm Employee data has changed. Please reload the page again.");
		}
		List<CrmEmployeeDTO> childEmployeeList = CrmAllEmployeeRepository.getInstance().getChildEmployeeDTOListByEmployeeID(crmEmployeeID);
		if(!childEmployeeList.isEmpty()){
			// if any child exists under the employee then place them under a transient employee
			CrmEmployeeDTO newTransientEmployeeDTO = new CrmEmployeeDTO();
			populateObjectFromOtherObject(prevCrmEmployeeDTO, newTransientEmployeeDTO, CrmEmployeeDTO.class);
			// by default every crmEmployee will be transient
			newTransientEmployeeDTO.setName("");
			newTransientEmployeeDTO.setUserID(null);
			newTransientEmployeeDTO.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
			crmEmployeeDAO.insertEmployeeDTO(newTransientEmployeeDTO);
			
			
			for(CrmEmployeeDTO childEmployeeDTO: childEmployeeList){
				childEmployeeDTO.setParentID(newTransientEmployeeDTO.getID());
				childEmployeeDTO.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
				crmEmployeeDAO.updateEmployeeDTO(childEmployeeDTO);
			}
		}
		
		
		CrmEmployeeDTO parentEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(prevCrmEmployeeDTO.getParentID());
		
		prevCrmEmployeeDTO.setParentID(parentEmployeeDTO.getParentID());
		prevCrmEmployeeDTO.setInventoryCatagoryTypeID(parentEmployeeDTO.getInventoryCatagoryTypeID());
		prevCrmEmployeeDTO.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
		
		crmEmployeeDAO.updateEmployeeDTO(prevCrmEmployeeDTO);
	}
	
	List<CrmEmployeeDesignationValuePair> getCrmEmployeeDesignationValuePairByEmployeeTree(CrmEmployeeNode rootEmployeeNode,String partialName){
		List<CrmEmployeeDesignationValuePair> crmEmployeeDesignationValuePairs = new ArrayList<>();
		if(rootEmployeeNode.getName().contains(partialName)){
			int crmInventoryCategoryTypeID = rootEmployeeNode.getInventoryCatagoryTypeID();
			CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
					.getCrmDesignationDTOByInventoryCategoryID(crmInventoryCategoryTypeID);
			if(crmDesignationDTO!=null){
				CrmEmployeeDesignationValuePair crmEmployeeDesignationValuePair = new CrmEmployeeDesignationValuePair();
				crmEmployeeDesignationValuePair.setEmployeeName(rootEmployeeNode.getName());
				crmEmployeeDesignationValuePair.setEmployeeID(rootEmployeeNode.getCrmEmployeeID());
				crmEmployeeDesignationValuePair.setDesignationName(crmDesignationDTO.getName());
				crmEmployeeDesignationValuePairs.add(crmEmployeeDesignationValuePair);
			}
		}
		for(CrmEmployeeNode childEmployeeNode: rootEmployeeNode.getChildCrmEmployeeNodeList()){
			crmEmployeeDesignationValuePairs.addAll(getCrmEmployeeDesignationValuePairByEmployeeTree(childEmployeeNode, partialName));
		}
		
		return crmEmployeeDesignationValuePairs;
	}
	

	public List<CrmEmployeeDesignationValuePair> getDesignationEmloyeeListByPartialName(String partialName, long departmentID) throws Exception{
		partialName = StringUtils.trim(partialName);
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO == null){
			return Collections.emptyList();
		}
		List<CrmEmployeeNode> crmEmployeeNodes = getCrmEmplyeeNodeListByDepartmentID(departmentID);
		if(crmEmployeeNodes.isEmpty()){
			return Collections.emptyList();
		}
		return getCrmEmployeeDesignationValuePairByEmployeeTree(crmEmployeeNodes.get(0), partialName);
	}
	
	public long changeSeniorByEmployeeID(long crmEmployeeID,long newParentID,long lastModificationTime) throws Exception{
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmEmployeeID);
		if(crmEmployeeDTO == null){
			throw new RequestFailureException("No crm employee found with id "+crmEmployeeID);
		}
		if(crmEmployeeDTO.getLastModificationTime()!=lastModificationTime){
			throw new RequestFailureException("Employee state changed. Please reload the page");
		}
		CrmEmployeeDTO parentEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(newParentID);
		if(parentEmployeeDTO == null){
			throw new RequestFailureException("No parent crmEmployee found with employee id "+newParentID);
		}
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
		
		
		if(!crmDesignationDTO.getParentCatagoryTypeID().equals(parentEmployeeDTO.getInventoryCatagoryTypeID())){
			throw new RequestFailureException("Invalid parent selected.");
		}
		crmEmployeeDTO.setParentID(newParentID);
		
		crmEmployeeDAO.updateConcurrentEmployeeDTO(crmEmployeeDTO);
		return crmEmployeeDTO.getLastModificationTime();
	}
	public List<CrmEmployeeDTO> getEmployeeListForSeniorSelectionByEmployeeID(long crmEmployeeID,long lastModificationTime) throws Exception{
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmEmployeeID);
		if(crmEmployeeDTO == null){
			throw new RequestFailureException("No crm employee found with id "+crmEmployeeID);
		}
		if(crmEmployeeDTO.getLastModificationTime()!=lastModificationTime){
			throw new RequestFailureException("Employee state changed. Please reload the page");
		}
		if(!crmEmployeeDTO.hasParent()){
			throw new RequestFailureException("Root employee can not be assigned under any other employee");
		}
		CrmEmployeeDTO parentEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmEmployeeDTO.getParentID());
		
		List<CrmEmployeeDTO> crmEmployeeDTOList = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByInventoryCategoryID(parentEmployeeDTO.getInventoryCatagoryTypeID());
		
		return crmEmployeeDTOList;
	}


	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return crmEmployeeDAO.getCrmEmployeeIDsWithSearchCriteria(new Hashtable<>(), loginDTO);
	}


	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return crmEmployeeDAO.getCrmEmployeeIDsWithSearchCriteria(searchCriteria, loginDTO);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return crmEmployeeDAO.getCrmEmployeeListByIDList(recordIDs);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Map<Integer,Map<Long,InventoryItem> > getMapOfInventoryItemToInventoryItemIDToCategoryTypeID() throws Exception{
		return new InventoryDAO().getMapOfInventoryItemToInventoryItemIDToCategoryTypeID(Arrays.asList(1,2,3));
	}
	@Transactional
	public Map<Integer, Long> getInventoryListByUserID(long userID) throws Exception{
		List<CrmEmployeeDTO> crmEmployeeDTOs = crmEmployeeDAO.getEmployeeDTOListByUserID(userID);
		
		Map<Integer, Long>inventoryIDsOfCrmEmployeeDTOs = new HashMap<Integer, Long>();
		if(crmEmployeeDTOs.isEmpty()) {
			return inventoryIDsOfCrmEmployeeDTOs;
		}
		for(CrmEmployeeDTO crmEmployeeDTO: crmEmployeeDTOs){
			CrmDesignationDTO crmDesignation = CrmAllDesignationRepository.getInstance().
														getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
			CrmDesignationDTO rootCrmDesignation = CrmAllDesignationRepository.getInstance().
														getRootDesignationDTOByDesignationID(crmDesignation.getDesignationID());
			CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().
														getCrmDepartmentByRootDesignationID(rootCrmDesignation.getDesignationID());
			
			if(crmDepartmentDTO!=null) {
				Long districtID = crmDepartmentDTO.getDistrictID();
				Long upazilaID = crmDepartmentDTO.getUpazilaID();
				Long unionID = crmDepartmentDTO.getUnionID();
				if(unionID != null){
					inventoryIDsOfCrmEmployeeDTOs.put(CategoryConstants.CATEGORY_ID_UNION, unionID);
				}else if(upazilaID != null){
					inventoryIDsOfCrmEmployeeDTOs.put(CategoryConstants.CATEGORY_ID_UPAZILA, upazilaID);
				}else if(districtID != null){
					inventoryIDsOfCrmEmployeeDTOs.put(CategoryConstants.CATEGORY_ID_DISTRICT, districtID);
				}else {
					inventoryIDsOfCrmEmployeeDTOs.put(0,null);
				}
			}
		}
		return inventoryIDsOfCrmEmployeeDTOs;
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmEmployeeDetails> getCrmEmployeeDetailsByUserID(long userID) throws Exception {
		List<CrmEmployeeDTO> crmEmployeeDTOs = crmEmployeeDAO.getEmployeeDTOListByUserID(userID);
		List<CrmEmployeeDetails> crmEmployeeDetailsList = new ArrayList<>();
		Map<Integer, Map<Long, InventoryItem>> resultMap = getMapOfInventoryItemToInventoryItemIDToCategoryTypeID();
		for(CrmEmployeeDTO crmEmployeeDTO : crmEmployeeDTOs) {
			CrmEmployeeDetails crmEmployeeDetails = getCrmEmployeeDetails(crmEmployeeDTO, resultMap);
			crmEmployeeDetailsList.add(crmEmployeeDetails);
		}
		return crmEmployeeDetailsList;
	}


	private CrmEmployeeDetails getCrmEmployeeDetails(CrmEmployeeDTO crmEmployeeDTO, Map<Integer, Map<Long, InventoryItem>> resultMap) throws Exception{
		CrmEmployeeDetails crmEmployeeDetails = new CrmEmployeeDetails();
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().
												getCrmDesignationDTOByInventoryCategoryID(
													crmEmployeeDTO.getInventoryCatagoryTypeID()
												);
		CrmDesignationDTO rootCrmDesignationDTO = CrmAllDesignationRepository.getInstance().
													getRootDesignationDTOByDesignationID(
														crmDesignationDTO.getDesignationID()
													);
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().
												getCrmDepartmentByRootDesignationID(
													rootCrmDesignationDTO.getDesignationID()
												);
		
		
		crmEmployeeDetails.setDistrictName(crmDepartmentDTO.getDistrictID() == null ? "All":resultMap.get(InventoryConstants.CATEGORY_ID_DISTRICT).get(crmDepartmentDTO.getDistrictID()).getName());
		crmEmployeeDetails.setUpazilaName(crmDepartmentDTO.getUpazilaID() == null ? "All":resultMap.get(InventoryConstants.CATEGORY_ID_UPAZILA).get(crmDepartmentDTO.getUpazilaID()).getName());
		crmEmployeeDetails.setUnionName(crmDepartmentDTO.getUnionID() == null ? "All":resultMap.get(InventoryConstants.CATEGORY_ID_UNION).get(crmDepartmentDTO.getUnionID()).getName());
		
		crmEmployeeDetails.setDepartment(crmDepartmentDTO);
		crmEmployeeDetails.setDesignation(crmDesignationDTO);
		crmEmployeeDetails.setRootDesignation(rootCrmDesignationDTO);
		crmEmployeeDetails.setEmployee(crmEmployeeDTO);
		return crmEmployeeDetails;
	}


	
}
