package crm.dao;

import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;
import crm.*;
import crm.inventory.CRMInventoryItem;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import login.LoginDTO;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static util.ModifiedSqlGenerator.*;

public class CrmEmployeeDAO {

	public CrmEmployeeDTO getPartialEmployeeDTOByEmployeeID(long employeeID) throws Exception {
		return  (CrmEmployeeDTO)getObjectByID(CrmEmployeeDTO.class, employeeID);
	}

	public CrmEmployeeDTO getPartialEmployeeDTOByInventoryItemID(long inventoryItemID) throws Exception {
		 String conditionString = " WHERE " +
		 getColumnName(CrmEmployeeDTO.class, "ID") + " = " + inventoryItemID;
		 List<CrmEmployeeDTO> crmEmployeeDTOs = (List<CrmEmployeeDTO>)
		 getAllObjectList(CrmEmployeeDTO.class,conditionString);
		return  crmEmployeeDTOs.isEmpty()? null : crmEmployeeDTOs.get(0);																									// crmEmployeeDTOs.get(0);
	}

	public void insertEmployeeDTO(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		insert(crmEmployeeDTO, CrmEmployeeDTO.class, true);
	}

	public void updateEmployeeDTO(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		int numOfAffectedRows = updateEntity(crmEmployeeDTO, CrmEmployeeDTO.class, true, false);
		if (numOfAffectedRows == 0) {
			throw new RequestFailureException("Update failed.");
		}
		
//		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
//		ps.executeUpdate();
	}
	public void updateConcurrentEmployeeDTO(CrmEmployeeDTO crmEmployeeDTO) throws Exception {
		int numOfAffectedRows = updateEntity(crmEmployeeDTO, CrmEmployeeDTO.class, true, true);
		if (numOfAffectedRows == 0) {
			throw new RequestFailureException("Update failed.");
		}
	}

	/* Should replace by repository */
	public List<CrmEmployeeNode> getEmloyeeNodeListByItemIdList(List<Long> itemIdList) throws Exception {
		return (List<CrmEmployeeNode>) getAllObjectListFullyPopulatedByIDList(CrmEmployeeDTO.class,
				CrmEmployeeNode.class, itemIdList);
	}

	public CrmEmployeeDTO getEmployeeDTOByEmployeeID(long employeeID) throws Exception {
		//return CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(employeeID);
		return  (CrmEmployeeDTO)  getObjectFullyPopulatedByID(CrmEmployeeDTO.class, employeeID);

	}

	public List<CrmEmployeeDTO> getEmployeeDTOListByUserID(long userID) throws Exception {
		/*List<CrmEmployeeDTO> crmEmployeeDTOs = (List<CrmEmployeeDTO>) getObjectFullyPopulatedByString(
				CrmEmployeeDTO.class, new Object[] { userID, 0 }, new String[] { "userID", "isDeleted" });
		return crmEmployeeDTOs;*/
		
		
		return CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(userID);
	}

	public void deleteEmployeeById(long employeeID) throws Exception {
		deleteEntityByID(CrmEmployeeDTO.class, employeeID);
	}

	public List<Long> getRootEmployeeIDList() throws Exception {
		String sql = "select crmEmployeeID from at_crm_employee where exists ( select * from at_crm_inventyory_item where invitParentID is null and invitID = inventoryItemID)";
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Long> rootEmployeeIDList = new ArrayList<>();
		while (rs.next()) {
			rootEmployeeIDList.add(rs.getLong(1));
		}
		return rootEmployeeIDList;
	}
	
	public List<Long> getInventoryItemListByInventoryCategoryID(int categoryID) throws Exception{
		String sql = "select invitID from at_crm_inventory_item where invitInvCatTypeID = ?";
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setInt(1, categoryID);
		ResultSet rs = ps.executeQuery();
		List<Long> inventoryItemList = new ArrayList<>();
		while (rs.next()) {
			inventoryItemList.add(rs.getLong(1));
		}
		return inventoryItemList;
	}

	public List<CrmEmployeeNode> getRootEmployeeNodeList() throws Exception {

		List<Long> rootEmployeeIdList = getRootEmployeeIDList();
		String conditionString = "WHERE " + getColumnName(CrmEmployeeDTO.class, "crmEmployeeID") + " IN "
				+ StringUtils.getCommaSeparatedString(rootEmployeeIdList);
		return (List<CrmEmployeeNode>) getAllObjectListFullyPopulated(CrmEmployeeDTO.class, CrmEmployeeNode.class,
				conditionString);
	}

	public boolean isValidEmployeeID(long employeeID) throws Exception {
		/*String sql = "SELECT 1 FROM at_crm_employee WHERE crmEmployeeID = ?)";
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setLong(1, employeeID);
		ResultSet rs = ps.executeQuery();*/
		return CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(employeeID) == null ? false: true;//rs.next();
	}

	/*public List<CrmEmployeeDesignationValuePair> getDescendantEmployeeListByPartialName(String partialName,
			long employeeID) throws Exception { // implement

		// this
		String sql = "  select crmEmployeeID,b.invitName employeeName ,a.invctname designationName "
				+ "from at_crm_inv_cat_type  a join "
				+ "(  select invitID,invitInvCatTypeID,invitName  from at_crm_inventory_item where invitPathFromRootToParent like CONCAT('%/',(select inventoryItemId from at_crm_employee where crmEmployeeID = "
				+ employeeID + "),'/%') and invitIsDeleted = 0 "
				+ (StringUtils.isBlank(partialName) ? "" : " AND invitName like '%" + partialName + "%'")
				+ ") b  on (a.invctID = b.invitInvCatTypeID) join at_crm_employee on (invitID = inventoryItemID)";

		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<CrmEmployeeDesignationValuePair> employeeDesignationPair = new ArrayList<CrmEmployeeDesignationValuePair>();
		CrmEmployeeDesignationValuePair crmEmployeeDesignationPair;
		while (rs.next()) {
			crmEmployeeDesignationPair = new CrmEmployeeDesignationValuePair();
			populateObjectFromDB(crmEmployeeDesignationPair, rs, CrmEmployeeDesignationValuePair.class);
			employeeDesignationPair.add(crmEmployeeDesignationPair);

		}
		return employeeDesignationPair;

	}*/
	
	public List<CrmEmployeeDesignationValuePair> getDescendantEmployeeListByPartialNameAndEmployeeIDList(String partialName,
			List<Long> employeeIDs) throws Exception { // implement

		// this
		
		String sql = "  select crmEmployeeID,b.invitName employeeName ,a.invctname designationName "
				+ "from at_crm_inv_cat_type  a join "
				+ "(  select invitID,invitInvCatTypeID,invitName  from at_crm_inventory_item where invitPathFromRootToParent like CONCAT('%/',(select inventoryItemId from at_crm_employee where crmEmployeeID IN "
				+ StringUtils.getCommaSeparatedString(employeeIDs) + "),'/%') and invitIsDeleted = 0 "
				+ (StringUtils.isBlank(partialName) ? "" : " AND invitName like '%" + partialName + "%'")
				+ ") b  on (a.invctID = b.invitInvCatTypeID) join at_crm_employee on (invitID = inventoryItemID)";

		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<CrmEmployeeDesignationValuePair> employeeDesignationPair = new ArrayList<CrmEmployeeDesignationValuePair>();
		CrmEmployeeDesignationValuePair crmEmployeeDesignationPair;
		while (rs.next()) {
			crmEmployeeDesignationPair = new CrmEmployeeDesignationValuePair();
			populateObjectFromDB(crmEmployeeDesignationPair, rs, CrmEmployeeDesignationValuePair.class);
			employeeDesignationPair.add(crmEmployeeDesignationPair);

		}
		return employeeDesignationPair;

	}

	public List<CrmEmployeeDesignationValuePair> getAllEmployeeListIncludeDescendantAndExcludeAncestorByPartialName(
			String partialName, long employeeID) throws Exception { // implement

		// this
		String sql = "";
		// "SELECT crmEmployeeID,B.invitName employeeName, A.invctname
		// designationName "
		// + "FROM at_inv_cat_type A JOIN "
		// + "( SELECT invitID,invitInvCatTypeID,invitName FROM
		// at_inventory_item WHERE invitPathFromRootToParent LIKE
		// CONCAT('%/',(SELECT inventoryItemId FTOM at_crm_employee WHERE
		// crmEmployeeID = "
		// + employeeID + "),'/%') AND invitIsDeleted = 0 "
		// + (StringUtils.isBlank(partialName) ? "" : " AND invitName LIKE '%" +
		// partialName + "%'")
		// + ") B ON (A.invctID = B.invitInvCatTypeID) JOIN at_crm_employee ON
		// (invitID = inventoryItemID)";

		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<CrmEmployeeDesignationValuePair> crmEmployeeMinDTOs = new ArrayList<CrmEmployeeDesignationValuePair>();
		CrmEmployeeDesignationValuePair crmEmployeeDesignationPair;
		while (rs.next()) {
			crmEmployeeDesignationPair = new CrmEmployeeDesignationValuePair();
			populateObjectFromDB(crmEmployeeDesignationPair, rs, CrmEmployeeDesignationValuePair.class);
			crmEmployeeMinDTOs.add(crmEmployeeDesignationPair);

		}
		return crmEmployeeMinDTOs;

	}

	public void updateEmployeeDTO(String conditionString) throws Exception {
		String sql = "UPDATE " + getTableName(CrmEmployeeDTO.class) + " SET " + conditionString;
		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		System.out.println("Update query: " + ps);
		ps.executeUpdate();
		updateSequencerTable(CrmEmployeeDTO.class);

	}

	public List<CrmEmployeeDTO> getEmployeeListForRepository(DatabaseConnection databaseConnection,
			boolean isFirstReload) throws Exception {
		return (List<CrmEmployeeDTO>) util.SqlGenerator.getAllObjectForRepository(CrmEmployeeDTO.class,
				databaseConnection, isFirstReload);
	}
	
	public void updateCrm(){
		System.out.println("Hello");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Long> getCrmEmployeeIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
		CrmDepartmentDAO crmDepartmentDAO = new CrmDepartmentDAO();
		searchCriteria.put("isDeleted", "0");
		List<Long> crmDepartmentIDs = (List<Long>)crmDepartmentDAO.getAllDepartmentIDWithSearchCriteria(searchCriteria);
		List<CrmDepartmentDTO> crmDepartmentDTOs= (List<CrmDepartmentDTO>)crmDepartmentDAO.getCrmDeptListByIDList(crmDepartmentIDs);
		
		HashSet<Long> rootDesignationIDSet = new HashSet<>();
		for(CrmDepartmentDTO crmDepartmentDTO: crmDepartmentDTOs){
			if(crmDepartmentDTO.getRootDesignationID() != null){
				rootDesignationIDSet.add(crmDepartmentDTO.getRootDesignationID());
			}
		}
		List<CRMInventoryItem> crmInventoryItems  = CrmAllEmployeeRepository.getInstance().getCrmInventoryItemDTOListByPartialName((String)searchCriteria.get("crmEmployeeName"));
		List<Long> crmInventoryItemIDs = new ArrayList<>();
		for(CRMInventoryItem crmInventoryItem : crmInventoryItems){
			
//			System.err.println(crmInventoryItem.getName());
			CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmInventoryItem.getInventoryCatagoryTypeID());
			
			if(crmDesignationDTO == null){
//				throw new RequestFailureException("crmDesignationDTO is null");
				continue;
			}
			
			CrmDesignationDTO rootCrmDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
			if(rootCrmDesignationDTO == null) {
//				throw new RequestFailureException("rootCrmDesignationDTO is null");
				continue;
			}
			
			CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootCrmDesignationDTO.getDesignationID());
			if(crmDepartmentDTO == null){
//				throw new RequestFailureException("crmDeparmentDTO is null");
				continue;
			}
			
			
			if(rootDesignationIDSet.contains(crmDepartmentDTO.getRootDesignationID())){
				crmInventoryItemIDs.add(crmInventoryItem.getID());
			}
			
		}
		return crmInventoryItemIDs;
	}

	@SuppressWarnings("unchecked")
	public Collection<CRMInventoryItem> getCrmEmployeeListByIDList(Collection<Long> recordIDs) throws Exception{
		Class<?> classobject = CRMInventoryItem.class;
		return (Collection<CRMInventoryItem>) ModifiedSqlGenerator.getObjectListByIDList(classobject, recordIDs);
	}

	public void updateChildrenEmployeeDTOPermissions(String sql) throws Exception {
		PreparedStatement pStatement = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		pStatement.executeQuery();
	}

}
