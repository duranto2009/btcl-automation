package crm.dao;

import crm.CrmDesignationNode;
import crm.CrmDesignationDTO;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmDesignationRepository;
import util.DatabaseConnectionFactory;

import static util.ModifiedSqlGenerator.*;

import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.RequestFailureException;
import common.StringUtils;
import connection.DatabaseConnection;

public class CrmDesignationDAO {
	public void insertDesignationDTO(CrmDesignationDTO crmDesignationDTO) throws Exception{
		insert(crmDesignationDTO);
	}
	
	public int updateDesignationDTO (CrmDesignationDTO crmDesignationDTO) throws Exception{
		return updateEntity(crmDesignationDTO, CrmDesignationDTO.class, true, false);
	}
	
	public List<CrmDesignationNode> getDesignationNodeListByCategoryIdList(List<Long> categoryIdList) throws Exception{
		return (List<CrmDesignationNode>) getAllObjectListFullyPopulatedByIDList(CrmDesignationDTO.class,CrmDesignationNode.class, categoryIdList);
	}
	
	public CrmDesignationDTO getCrmDesignationDTOByDesignationID(long designationID) throws Exception{
		return CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(designationID);
	}
	public List<CrmDesignationDTO> getDesignationDTOListByIDList(List<Long> designationIDList) throws Exception{
		List<CrmDesignationDTO> crmDesignationDTOs = new ArrayList<>();
		for (long designationID : designationIDList) {
			crmDesignationDTOs.add(getCrmDesignationDTOByDesignationID(designationID));
		}
		return crmDesignationDTOs;//(List<CrmDesignationDTO>)getAllObjectListFullyPopulatedByIDList(CrmDesignationDTO.class, IDList);
	}
	
	public void deleteDesignationByDesignationIdIfNoChildExists(long designationID) throws Exception{
		deleteEntityByID(CrmDesignationDTO.class, designationID);
	}
	
	public List<Long> getRooDesignationIDList() throws Exception{
		String sql = "select designationTypeID from at_crm_designation_type where exists ( select * from at_crm_inv_cat_type where invctParentID is null and invctID = inventoryCategoryID)";
		DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Long> rootDesignationIDList = new ArrayList<>();
		while(rs.next()){
			rootDesignationIDList.add(rs.getLong(1));
		}
		return rootDesignationIDList;
	}
	
	public List<CrmDesignationNode> getRootDesignationNodeList() throws Exception{
		
		List<Long> rootDesignationIdList = getRooDesignationIDList();
		String conditionString = "WHERE "+ getColumnName(CrmDesignationDTO.class, "designationTypeID") + " IN " + StringUtils.getCommaSeparatedString(rootDesignationIdList);
		return (List<CrmDesignationNode>) getAllObjectListFullyPopulated(CrmDesignationDTO.class, CrmDesignationNode.class, conditionString);	
	}

	public CrmDesignationDTO getPartialDesignationDTOByInventoryCategoryID(int inventoryCategoryID) throws Exception{
		
		return CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(inventoryCategoryID);
	}

	public void deleteDesignationByDesignationID(long designationID, long currentTime) throws Exception {
		deleteEntityByID(CrmDesignationDTO.class, designationID);		
	}
	
	public boolean isValidDesignationID(long designationID) throws Exception{
		return CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByDesignationID(designationID) == null ? false : true;//rs.next();
	}

	public List<CrmDesignationDTO> getDesignationDTOListForRepository(DatabaseConnection databaseConnection,
			boolean isFirstReload) throws Exception {
		return (List<CrmDesignationDTO>)util.SqlGenerator.getAllObjectForRepository(CrmDesignationDTO.class, databaseConnection, isFirstReload);
	}
}
