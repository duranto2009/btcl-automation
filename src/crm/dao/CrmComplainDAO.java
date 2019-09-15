package crm.dao;
import java.sql.PreparedStatement;
import java.util.*;

import common.RequestFailureException;
import common.StringUtils;
import crm.*;
import crm.inventory.CRMInventoryItem;
import util.CurrentTimeFactory;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;

import static util.ModifiedSqlGenerator.*;


public class CrmComplainDAO {
	
	public void insertComplain(CrmComplainDTO crmComplainDTO) throws Exception{
		insert(crmComplainDTO, CrmComplainDTO.class, false);
	}
	
	public void updateComplain(CrmComplainDTO crmComplainDTO) throws Exception{
		updateEntity(crmComplainDTO,CrmComplainDTO.class, false, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<CrmComplainTreeNode> getChildComplainListByParentComplainID(long parentComplainID) throws Exception{
		String conditionString = " WHERE "+ getColumnName(CrmComplainDTO.class, "parentComplainID") + " = " + parentComplainID;
		return (List<CrmComplainTreeNode>) getAllObjectListFullyPopulated(CrmComplainDTO.class,CrmComplainTreeNode.class, conditionString);
	}
	
	public CrmComplainDTO geParentComplainDTOByChildComplainID(long childComplainID) throws Exception{
		CrmComplainDTO childComplainDTO = (CrmComplainDTO) getObjectFullyPopulatedByID(CrmComplainDTO.class, childComplainID);
		return getComplainDTOByComplainID(childComplainDTO.getParentComplainID());
	}
	public CrmComplainDTO getComplainDTOByComplainID(long complainID) throws Exception{
		CrmComplainDTO crmComplainDTO = (CrmComplainDTO) getObjectByID(CrmComplainDTO.class, complainID);
		if(crmComplainDTO == null || crmComplainDTO.isDeleted()){
			throw new RequestFailureException("No such CrmComplainDTO exists");
		}
		return crmComplainDTO;
	}
	@SuppressWarnings("unchecked")
	public List<CrmComplainTreeNode> getComplainTreeNodeListByEmployeeID(long employeeID) throws Exception{
		String conditionString = " WHERE "+ getColumnName(CrmComplainDTO.class, "passedToID") + " = " + employeeID;
		return  (List<CrmComplainTreeNode>) getAllObjectListFullyPopulated(CrmComplainDTO.class,CrmComplainTreeNode.class, conditionString);
	}
	public List<Long> getAllWatingComplainDTOIDByUserID(long crmEmployeeID) throws Exception{
		return (List<Long>)getAllIDList(CrmComplainDTO.class, " WHERE complainResolverID = "+crmEmployeeID);
	}

	public List<CrmComplainDTO> getCrmCompalinDTOListByComplainIDList(Collection<Long> recordIDs) throws Exception{
		return (List<CrmComplainDTO>)getObjectListByIDList(CrmComplainDTO.class, recordIDs);
	}
	public List<Long> getCrmComplainIDListBySearchCriteria(Map<String, String> criteriaMap,List<Long> resolverIDList) throws Exception{
		
		criteriaMap.put("isDeleted", "0");
		
		String subToken = (String)criteriaMap.get("subTokenID");
		if(subToken != null){
			subToken = subToken.replaceAll("[^\\d.]", "");
			criteriaMap.put("subTokenID", subToken);
		}
		String token = (String)criteriaMap.get("tokenID");
		if(token  != null){
			token  = token .replaceAll("[^\\d.]", "");
			criteriaMap.put("tokenID", token );
		}
		String[] keys = new String[]          {"status"		  ,"priority","tag" ,"isDeleted", "submissionTo",   "submissionFrom", "subTokenID", "tokenID"};
		String[] operators = new String[]     {"="			  ,"="       ,"like","=", 		  "<=", 			">="            , "="		  , "="};
		String[] dtoColumnNames = new String[]{"currentStatus","priority","tag" ,"isDeleted", "generationTime", "generationTime", "ID"        , "commonPoolID"};
		Class<?> classObject = CrmComplainDTO.class;
		
		if(resolverIDList != null && resolverIDList.isEmpty()){
			return new ArrayList<>();
		}
		
		String employeeName = StringUtils.trim(criteriaMap.get("employeeName"));
		
		String fixedCondition =  null;
		if(resolverIDList == null){
			// noc user
			if( !employeeName.isEmpty()){
				fixedCondition = getColumnName(classObject, "complainResolverID")+" IN ( select "+getPrimaryKeyColumnName(CrmEmployeeDTO.class)
				+" from "+getTableName(CrmEmployeeDTO.class)+" where "+SqlGenerator.getForeignKeyColumnName(CrmEmployeeDTO.class)+" IN ("
				+" Select "+getPrimaryKeyColumnName(CRMInventoryItem.class)+" from "+getTableName(CRMInventoryItem.class)
				+" where "+getColumnName(CRMInventoryItem.class, "name")+" like '%"+employeeName+"%' and "+getColumnName(CRMInventoryItem.class, "isDeleted")
				+"=0) and "+getColumnName(CrmEmployeeDTO.class, "isDeleted")
				+ "=0)";
			}
		}else{
			fixedCondition = getColumnName(classObject, "complainResolverID")+" IN "+ common.StringUtils
					.getCommaSeparatedString(resolverIDList);
		}
		
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition);
		
		return IDList;
	}

	public List<CrmComplainDTO> getComplainListByPoolID(long commonPoolID) throws Exception {
		String conditionString = " WHERE "+ getColumnName(CrmComplainDTO.class, "commonPoolID") + " = " + commonPoolID 
					+ " ORDER BY " + getColumnName(CrmComplainDTO.class, "generationTime") + " ASC";
		return  (List<CrmComplainDTO>) getAllObjectListFullyPopulated(CrmComplainDTO.class, conditionString);
	}

	@SuppressWarnings("unchecked")
	public List<CrmComplainDTO> getCrmComplainListByResolverID(long employeeID) throws Exception {
		String conditionString = " WHERE " + getColumnName(CrmComplainDTO.class, "complainResolverID") + " = " + employeeID
				+ " ORDER BY " + getColumnName(CrmComplainDTO.class, "generationTime") + " ASC";
		return (List<CrmComplainDTO>) getAllObjectListFullyPopulated(CrmComplainDTO.class, conditionString);

	}
	
	@SuppressWarnings("unchecked")
	public List<CrmComplainDTO> getCrmComplainListByAssignerID(long employeeID) throws Exception {
		String conditionString = " WHERE " + getColumnName(CrmComplainDTO.class, "assignerID") + " = " + employeeID
				+ " ORDER BY " + getColumnName(CrmComplainDTO.class, "generationTime") + " ASC";
		return (List<CrmComplainDTO>) getAllObjectListFullyPopulated(CrmComplainDTO.class, conditionString);

	}

	public CrmComplainHistoryDTO getComplainHistoryDTOByComplainHisotryID(long complainHisotryID) throws Exception {
		return  (CrmComplainHistoryDTO) getObjectByID(CrmComplainHistoryDTO.class, complainHisotryID);
	}

	@SuppressWarnings("unchecked")
	public List<CrmComplainHistoryDTO> getCrmComplainHistoryDTOListByRootComplainHistoryID(Long rootComplainHistoryID) throws Exception {
		Class<?> classObject = CrmComplainHistoryDTO.class;
		String conditionString = " WHERE "+ getColumnName(classObject, "rootComplainHistoryID") + " = "+ rootComplainHistoryID + " ORDER BY " + getColumnName(classObject, "complainHistorySubmissionTime") + " ASC";
		return (List<CrmComplainHistoryDTO>) getAllObjectListFullyPopulated(classObject, conditionString);
	}

	public void updateBlockedStatus(CrmComplainDTO crmComplainDTO) throws Exception {
		//called when a single crm complain is blocked
		int numberOfAffectedRows = ModifiedSqlGenerator.updateEntityByPropertyList(crmComplainDTO, 
				CrmComplainDTO.class, false, false, new String[]{"isBlocked","lastModificationTime"});
		if(numberOfAffectedRows == 0){
			throw new RequestFailureException("No such complain found with id: " + crmComplainDTO.getID());
		}
	}

	public void blockCrmComplainByCommonPoolID(long commonPoolID) throws Exception {
		// called when a crm client complain is blocked : its children crm complins need to be blocked too
		Class<?> classObject = CrmComplainDTO.class;
		String sql = "update " + getTableName(classObject) + " set " + getColumnName(classObject, "isBlocked") + " = 1, " + 
					getColumnName(classObject, "lastModificationTime") + " = " + CurrentTimeFactory.getCurrentTime() + 
					" where " + getColumnName(classObject, "commonPoolID") + " = " + commonPoolID;
		PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
		ps.executeUpdate();
		
		updateSequencerTable(classObject);
	}


}
