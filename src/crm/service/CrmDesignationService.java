package crm.service;

import annotation.Transactional;
import common.RequestFailureException;
import common.StringUtils;
import crm.CrmDepartmentDTO;
import crm.CrmDesignationNode;
import crm.CrmDesignationDTO;
import crm.CrmEmployeeDTO;
import crm.action.CrmDesignationAction;
import crm.dao.CrmDepartmentDAO;
import crm.dao.CrmDesignationDAO;
import crm.dao.CrmEmployeeDAO;
import crm.inventory.CRMInventoryCatagoryType;
import crm.inventory.CRMInventoryDAO;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import crm.repository.CrmDesignationRepository;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;

import java.lang.reflect.Field;
import java.util.*;

import com.gargoylesoftware.htmlunit.WebConsole.Logger;

import static util.ModifiedSqlGenerator.*;

public class CrmDesignationService {
	CrmDesignationDAO crmDesignationDAO = new CrmDesignationDAO();
	CrmEmployeeDAO crmEmployeeDAO = new CrmEmployeeDAO();
	CrmDepartmentDAO crmDepartmentDAO = new CrmDepartmentDAO();
	CRMInventoryDAO inventoryDAO = new CRMInventoryDAO();

	
	public static boolean isNOC(long userID){
		List<CrmEmployeeDTO> crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(userID);
		for(CrmEmployeeDTO crmEmployeeDTO: crmEmployeeDTOs){
			if(isNOC(crmEmployeeDTO)){
				return true;
			}
		}
		
		return false;
	}
	
	public CrmDesignationDTO getCrmDesignationDTOByCrmInventoryCategoryID(int crmInventoryCategoryID){
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmInventoryCategoryID);
		if(crmDesignationDTO == null){
			throw new RequestFailureException("No CrmDesignation found by crm Inventory id + " + crmInventoryCategoryID);
		}
		return crmDesignationDTO;
	}
	
	
	private void copySubtreeFromOtherTree(CrmDesignationDTO srcDesignationDTO,CrmDesignationDTO destDesignationDTO) throws Exception{
		List<CrmDesignationDTO> childDesignationList = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationChildListByParentDesignationID(srcDesignationDTO.getDesignationID());
		for(CrmDesignationDTO crmChildDesignationDTO: childDesignationList){
			CrmDesignationDTO crmNewChildDesignationDTO = new CrmDesignationDTO();
			ModifiedSqlGenerator.populateObjectFromOtherObject(crmChildDesignationDTO, crmNewChildDesignationDTO, CrmDesignationDTO.class);
			crmNewChildDesignationDTO.setParentCatagoryTypeID(destDesignationDTO.getID());
			crmDesignationDAO.insertDesignationDTO(crmNewChildDesignationDTO);
			copySubtreeFromOtherTree(crmChildDesignationDTO, crmNewChildDesignationDTO);
		}
	}
	
	@Transactional
	public void copyDesignationFromOtherDepartment(long srcDepartmentID, long destinationDepartmentID ) throws Exception{
		
		CrmDepartmentDTO sourceDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(srcDepartmentID);
		if(sourceDepartmentDTO == null){
			throw new RequestFailureException("No source department found");
		}
		
		if(sourceDepartmentDTO.getRootDesignationID()==null){
			throw new RequestFailureException("No organogram is there to copy from");
		}
		
		CrmDepartmentDTO destinationDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(destinationDepartmentID);
		if(destinationDepartmentDTO == null){
			throw new RequestFailureException("No destination department found");
		}
		
		if(destinationDepartmentDTO.getRootDesignationID()!=null){
			throw new RequestFailureException("The department has already an organogram. To copy from other "
					+ "department at fist delete the existing organogram");
		}
		
		CrmDesignationDTO srcDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(sourceDepartmentDTO.getRootDesignationID());
		if(srcDesignationDTO==null){
			throw new RequestFailureException("No organogram is there to copy from");
		}
		
		CrmDesignationDTO newCrmDesignationDTO = new CrmDesignationDTO();
		ModifiedSqlGenerator.populateObjectFromOtherObject(srcDesignationDTO, newCrmDesignationDTO, CrmDesignationDTO.class);
		crmDesignationDAO.insertDesignationDTO(newCrmDesignationDTO);
		CrmDepartmentDTO crmDepartmentDTOForUpdate = new CrmDepartmentDTO();
		ModifiedSqlGenerator.populateObjectFromOtherObject(destinationDepartmentDTO, crmDepartmentDTOForUpdate, CrmDepartmentDTO.class);
		crmDepartmentDTOForUpdate.setRootDesignationID(newCrmDesignationDTO.getDesignationID());
		crmDepartmentDAO.updateDepartmentDTO(crmDepartmentDTOForUpdate);
		copySubtreeFromOtherTree(srcDesignationDTO, newCrmDesignationDTO);
	}
	
	public static boolean isNOC(CrmEmployeeDTO crmEmployeeDTO){
		CrmDesignationDTO designationTypeDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());

		if(designationTypeDTO == null){
			return false;
		}
		CrmDesignationDTO rootDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(designationTypeDTO.getDesignationID());
		if(rootDesignationDTO == null){
			return false;
		}
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootDesignationDTO.getDesignationID());
		if(crmDepartmentDTO == null){
			return false;
		}
		return crmDepartmentDTO.isNOC();
	}
	
	
	
	@Transactional
	public void insert(CrmDesignationDTO crmDesignationDTO, long departmentID) throws Exception {

		if (crmDesignationDTO.isDeleted()) {
			throw new RequestFailureException("Invalid");
		}
		
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance()
				.getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO == null){
			throw new RequestFailureException("No department found");
		}
		if(crmDesignationDTO.getParentCatagoryTypeID()==null){
			if(  crmDepartmentDTO.getRootDesignationID()!=null){
				throw new RequestFailureException("This department has already a head of the department."
						+ " No a request to add another head of the department is invalid");
			}
		}else{
			CrmDesignationDTO parentDesignationDTO = CrmAllDesignationRepository.getInstance()
					.getCrmDesignationDTOByInventoryCategoryID(crmDesignationDTO.getParentCatagoryTypeID());
			if(parentDesignationDTO==null){
				throw new RequestFailureException("Invalid parent designation ID selected.");
			}
			CrmDesignationDTO rootDesignationDTO = CrmAllDesignationRepository.getInstance()
					.getRootDesignationDTOByDesignationID(parentDesignationDTO.getDesignationID());
			if(rootDesignationDTO==null){
				throw new Exception("Invalid data. No root designation found.");
			}
			if(! rootDesignationDTO.getDesignationID().equals( crmDepartmentDTO.getRootDesignationID() )){
				throw new RequestFailureException("Designation of another department selected.");
			}
		}
		
		crmDesignationDAO.insertDesignationDTO(crmDesignationDTO);
		
		if(crmDesignationDTO.getParentCatagoryTypeID() == null){
			CrmDepartmentDTO crmDepartmentDTOForUpdate = new CrmDepartmentDTO();
			crmDepartmentDTOForUpdate.setID(crmDepartmentDTO.getID());
			crmDepartmentDTOForUpdate.setRootDesignationID(crmDesignationDTO.getDesignationID());
			crmDepartmentDTOForUpdate.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
			ModifiedSqlGenerator.updateEntityByPropertyList(crmDepartmentDTOForUpdate, CrmDepartmentDTO.class, false, false, new String[]{"rootDesignationID","lastModificationTime"});
		}
		CrmAllDesignationRepository.getInstance().reload(false);
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmDesignationDTO getDesignationDTOByDesignationID(long designationID) throws Exception {
		return crmDesignationDAO.getCrmDesignationDTOByDesignationID(designationID);
	}

	@Transactional
	public void update(CrmDesignationDTO crmDesignationDTO) throws Exception {
		
		CrmDesignationDTO crmDesignationDTOFromRepo = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());  
		if(crmDesignationDTOFromRepo==null){
			throw new RequestFailureException("No designation found with the requested designationID");
		}
		crmDesignationDTO.setParentCatagoryTypeID(crmDesignationDTOFromRepo.getParentCatagoryTypeID());
		crmDesignationDTO.setID(crmDesignationDTOFromRepo.getID());
		
		if(crmDesignationDTO.getParentCatagoryTypeID()!=null){
			CrmDesignationDTO parentDesignationDTO = CrmAllDesignationRepository.getInstance()
					.getCrmDesignationDTOByInventoryCategoryID(crmDesignationDTO.getParentCatagoryTypeID());
			validatePermissionConsistencyWithParent(parentDesignationDTO, crmDesignationDTO);
			
		}
		List<Integer> descendantCategoryIDList = getDescendantDesignationCategoryIDsByRootDesignationID(crmDesignationDTO);
		String descendentCategoryIDListConvertedToString = StringUtils.getCommaSeparatedString(descendantCategoryIDList);
		String fixedConditionForDesignation = " where "+SqlGenerator.getForeignKeyColumnName(CrmDesignationDTO.class)+" in " + descendentCategoryIDListConvertedToString;
		
		String fixedConditionForEmployee = " where inventoryItemID in ("
												+ " select invitID from at_crm_inventory_item where invitInvCatTypeID in "
												+ 		descendentCategoryIDListConvertedToString
												+ " )";
		String updateQueryForDesignationTree = getUpdateQuery(crmDesignationDTO, CrmDesignationDTO.class, descendantCategoryIDList, fixedConditionForDesignation);  
		String updateQueryForEmployeeTree = getUpdateQuery(crmDesignationDTO, CrmEmployeeDTO.class, descendantCategoryIDList, fixedConditionForEmployee);

		DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement()
				.executeUpdate(updateQueryForDesignationTree);
		
		DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement()
				.executeUpdate(updateQueryForEmployeeTree);
		ModifiedSqlGenerator.updateSequencerTable(CrmEmployeeDTO.class);
		
		
		int numOfAffectedRows = crmDesignationDAO.updateDesignationDTO(crmDesignationDTO);
		if(numOfAffectedRows == 0){
			throw new RequestFailureException( "Designation update failed" );
		}

	}
	
	
	private String getUpdateQuery(CrmDesignationDTO crmDesignationDTO, Class<?> classObject, List<Integer> descendantCategoryIDList, String fixedCondition) throws Exception{
		List<Field>validFields = getValidFieldList(crmDesignationDTO);
		String updateQueryForDesignationTree = getUpdateQueryWithColumnNames(validFields, descendantCategoryIDList, classObject) + fixedCondition;
		return updateQueryForDesignationTree;
		
	}
	private String getUpdateQueryWithColumnNames(List<Field> validFields, List<Integer> descendantCategoryIDList, Class<?> classObject) throws Exception{
		String updateQuery = "";
		for(Field field : validFields){
			String columnName = getColumnName(classObject, field.getName());
			if(!updateQuery.isEmpty()){
				updateQuery += ",";
			}
			updateQuery += (columnName + "=0");
		}
		if(!updateQuery.isEmpty()){
			updateQuery += ",";
		}
		updateQuery += getLastModificationTimeColumnName(classObject)+"="+CurrentTimeFactory.getCurrentTime();
		updateQuery = "update " + getTableName(classObject) + " set " + updateQuery;
		return updateQuery;
	}
	
	
	private void validatePermissionConsistencyWithParent(CrmDesignationDTO parentDesignationDTO, CrmDesignationDTO crmDesignationDTO) throws Exception {
		if(parentDesignationDTO != null){
			for(Field field: CrmDesignationDTO.class.getDeclaredFields()){
				field.setAccessible(true);
				if( isValidPermissionField(field)
						&&  checkConsistentParentPermissionWithChild(field,parentDesignationDTO, crmDesignationDTO)){
					throw new RequestFailureException("Parent Designation does not have the permission "+ field.getName());
				}
			}
		}
	}

	private boolean isValidPermissionField(Field field) {
		return (field.getName().startsWith("hasPermission")  &&  !field.getName().endsWith("Permission"));
	}
	private boolean checkConsistentParentPermissionWithChild(Field field, CrmDesignationDTO parentDesignationDTO, CrmDesignationDTO crmDesignationDTO) throws Exception{
		return (field.getBoolean(parentDesignationDTO) == false) && (field.getBoolean(crmDesignationDTO) == true);
	}
	
	
	private String getUpdateQueryForEmployeeTreeWithColumnNames(List<Field> validFields, List<Integer> descendantCategoryIDList) throws Exception {
		
		String updateQuery = "";
		for(Field field: validFields){
			String columnName = getColumnName(CrmEmployeeDTO.class, field.getName());
			if(!updateQuery.isEmpty()){
				updateQuery +=",";
			}
			updateQuery += (columnName + "=0");
		}
		if(!updateQuery.isEmpty()){
			updateQuery += ",";
		}
		updateQuery += getLastModificationTimeColumnName(CrmEmployeeDTO.class)+"="+CurrentTimeFactory.getCurrentTime();
		updateQuery = "update "+getTableName(CrmEmployeeDTO.class)+" set "+ updateQuery + " where inventoryItemID in ("
				+ " select invitID from at_crm_inventory_item where invitInvCatTypeID in "
				+ StringUtils.getCommaSeparatedString(descendantCategoryIDList)
				+ " )";
		
		return updateQuery;
	}
	private List<Field> getValidFieldList(CrmDesignationDTO crmDesignationDTO) throws Exception {
		List<Field> validFields = new ArrayList<>();
		for(Field field: CrmDesignationDTO.class.getDeclaredFields()){
			field.setAccessible(true);
			if(isValidPermissionField(field)&& (field.getBoolean(crmDesignationDTO)==false)){
				validFields.add(field);
			}
		}
		return validFields;
		
	}

	private List<Integer> getDescendantDesignationCategoryIDsByRootDesignationID(CrmDesignationDTO crmDesignationDTO) 
			throws Exception{
		if(crmDesignationDTO==null){
			return Collections.emptyList();
		}
		List<Integer> result = new ArrayList<>();
		for(CrmDesignationDTO childDesignationDTO: CrmAllDesignationRepository.getInstance()
				.getCrmDesignationChildListByParentDesignationID(crmDesignationDTO.getDesignationID())){
			result.addAll( getDescendantDesignationCategoryIDsByRootDesignationID(childDesignationDTO) );
		}
		result.add(crmDesignationDTO.getID());
		return result;
	} 
	@Transactional
	public void deleteByDesignationID(long designationID) throws Exception {
		CrmDesignationDTO designationTypeDTO = crmDesignationDAO
				.getCrmDesignationDTOByDesignationID(designationID);

		if (inventoryDAO.hasChildOfInventoryCategory(designationTypeDTO.getID(),
				DatabaseConnectionFactory.getCurrentDatabaseConnection())) {
			throw new RequestFailureException("This designation can not be deleted. As it has child designation(s)");
		}
		if (inventoryDAO.hasAnyCRMInventoryItemOfCategory(designationTypeDTO.getID(),
				DatabaseConnectionFactory.getCurrentDatabaseConnection())) {
			throw new RequestFailureException("This designation can not be deleted. As it has employee(s)");
		}
		long currentTime = CurrentTimeFactory.getCurrentTime();
		
		CrmDesignationDTO rootDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(designationID);
		
		if(rootDesignationDTO != null & rootDesignationDTO.getDesignationID().equals(designationID)){
			CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(designationID);
			
			CrmDepartmentDTO crmDepartmentDTOForUpdate = new CrmDepartmentDTO();
			ModifiedSqlGenerator.populateObjectFromOtherObject(crmDepartmentDTO, crmDepartmentDTOForUpdate, CrmDepartmentDTO.class);
			
			crmDepartmentDTOForUpdate.setRootDesignationID(null);
			crmDepartmentDTOForUpdate.setLastModificationTime(CurrentTimeFactory.getCurrentTime());
			crmDepartmentDAO.updateDepartmentDTO(crmDepartmentDTOForUpdate);
		}
		
		inventoryDAO.deleteInventoryCategoryTypeByCategoryID(designationTypeDTO.getID(), currentTime);
		crmDesignationDAO.deleteDesignationByDesignationID(designationID, currentTime);
		
		
		
	}

	private List<CrmDesignationNode> getCrmDesignationListByParentDesignationDTO(
			CrmDesignationDTO crmDesignationDTO) {
		
		List<CrmDesignationDTO> crmDesignationDTOs = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationChildListByParentDesignationID(crmDesignationDTO.getDesignationID());
		
		List<CrmDesignationNode> crmDesignationNodes = new ArrayList<>();
		for(CrmDesignationDTO childDesignationDTO: crmDesignationDTOs){
			
			CrmDesignationNode crmDesignationNode = new CrmDesignationNode();
			populateObjectFromOtherObject(childDesignationDTO, crmDesignationNode, CrmDesignationDTO.class);
			crmDesignationNode.setChildCrmDesignationNodeList( getCrmDesignationListByParentDesignationDTO(childDesignationDTO) );
			crmDesignationNodes.add(crmDesignationNode);
		}
		
		return crmDesignationNodes;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmDesignationDTO> getChildDesignationDTOListByParentDesignationID(
			long parentDesignationID) throws Exception {
		
		
		return CrmAllDesignationRepository.getInstance().getCrmDesignationChildListByParentDesignationID(parentDesignationID);
	}
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmDesignationNode> getDesignationTreeRoots() throws Exception {
		List<CrmDesignationDTO> rootDesignationDTOList = getRootDesignationDTOs();
		// logger.debug(rootDesignationDTOList);
		List<CrmDesignationNode> rootDesignationNodeList = new ArrayList<>();
		for (CrmDesignationDTO crmDesignationDTO : rootDesignationDTOList) {
			
			CrmDesignationDTO tempertedCrmDesignationDTO = new CrmDesignationDTO();
			populateObjectFromOtherObject(crmDesignationDTO, tempertedCrmDesignationDTO, CrmDesignationDTO.class);
			
			tempertedCrmDesignationDTO.setParentCatagoryTypeID(null);
			
			CrmDesignationDTO rootDesignationDTO =  CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
	        CrmDepartmentDTO currentEmployeeDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootDesignationDTO.getDesignationID());
	        tempertedCrmDesignationDTO.setNOC(currentEmployeeDepartmentDTO.isNOC());
	        tempertedCrmDesignationDTO.setDepartmentName(currentEmployeeDepartmentDTO.getDepartmentName());
	        
			CrmDesignationNode rootDesignationNode = new CrmDesignationNode();
			populateObjectFromOtherObject(tempertedCrmDesignationDTO, rootDesignationNode, CrmDesignationDTO.class);
			rootDesignationNode
					.setChildCrmDesignationNodeList(getCrmDesignationListByParentDesignationDTO(rootDesignationNode));
			rootDesignationNodeList.add(rootDesignationNode);
		}
		return rootDesignationNodeList;
	}
	
	
	
	public List<CrmDesignationNode> getDesignationDTOListByDepartmentID(long departmentID){
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO == null){
			return Collections.EMPTY_LIST;
		}
		Long rootDesignationID = crmDepartmentDTO.getRootDesignationID();
		if(rootDesignationID == null){
			return Collections.EMPTY_LIST;
		}
		CrmDesignationNode crmRootDesignationNode = getDesignationTreeNodeByDesignationID(rootDesignationID);
		List<CrmDesignationNode> crmDesignationNodes = new ArrayList<>();
		if(crmRootDesignationNode!= null){
			crmDesignationNodes.add(crmRootDesignationNode);
		}
		return crmDesignationNodes;
	}
	
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public CrmDesignationNode getDesignationTreeNodeByDesignationID(long crmDesignationID) {
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
				.getCrmDesignationDTOByDesignationID(crmDesignationID);
		if(crmDesignationDTO==null){
			return null;
		}
		CrmDesignationDTO tempertedCrmDesignationDTO = new CrmDesignationDTO();
		populateObjectFromOtherObject(crmDesignationDTO, tempertedCrmDesignationDTO
				, CrmDesignationDTO.class);
		
		tempertedCrmDesignationDTO.setParentCatagoryTypeID(null);
		
		CrmDesignationDTO rootDesignationDTO =  CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
        CrmDepartmentDTO currentEmployeeDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootDesignationDTO.getDesignationID());
        tempertedCrmDesignationDTO.setNOC(currentEmployeeDepartmentDTO.isNOC());
        tempertedCrmDesignationDTO.setDepartmentName(currentEmployeeDepartmentDTO.getDepartmentName());
        
		CrmDesignationNode rootDesignationNode = new CrmDesignationNode();
		populateObjectFromOtherObject(tempertedCrmDesignationDTO, rootDesignationNode, CrmDesignationDTO.class);
		rootDesignationNode.setChildCrmDesignationNodeList(getCrmDesignationListByParentDesignationDTO(rootDesignationNode));
		return rootDesignationNode;
	}
	
	public CrmDesignationDTO getRootDesignationDTOByDepartmentID(long departmentID) throws Exception{
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
		if(crmDepartmentDTO==null){
			throw new RequestFailureException("No department found with ID "+departmentID);
		}
		if(crmDepartmentDTO.getRootDesignationID()==null){
			return null;
		}
		
		return CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(crmDepartmentDTO.getRootDesignationID());
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmDesignationDTO> getRootDesignationDTOs() throws Exception {
		List<CRMInventoryCatagoryType> rootInventoryCategoryType =  CrmAllDesignationRepository.getInstance().getRootInventoryCategoryTypeList();
		
		List<CrmDesignationDTO> rootDesignationDTOList = new ArrayList<>();
		
		for (CRMInventoryCatagoryType crmInventoryCatagoryType : rootInventoryCategoryType) {
			rootDesignationDTOList.add(CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmInventoryCatagoryType.getID()));
		}
		return rootDesignationDTOList;
	}

	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<CrmDesignationDTO> getDesignationDTOListForRepository(boolean isFirstReload) throws Exception {
		return crmDesignationDAO.getDesignationDTOListForRepository(
				DatabaseConnectionFactory.getCurrentDatabaseConnection(), isFirstReload);
	}

	public void deleteDesignationTreeByDesignationRootNode(CrmDesignationNode crmDesignationRootNode) throws Exception {
		for(CrmDesignationNode designationChildNode: crmDesignationRootNode.getChildCrmDesignationNodeList()){
			deleteDesignationTreeByDesignationRootNode(designationChildNode);
		}
		crmDesignationDAO.deleteDesignationByDesignationID(crmDesignationRootNode.getDesignationID()
				, CurrentTimeFactory.getCurrentTime());
		inventoryDAO.deleteInventoryCategoryTypeByCategoryID(crmDesignationRootNode.getID(), CurrentTimeFactory.getCurrentTime());
	}

}
