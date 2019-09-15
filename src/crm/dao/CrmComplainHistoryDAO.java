package crm.dao;

import static util.ModifiedSqlGenerator.*;

import java.sql.ResultSet;
import java.util.List;

import complain.ComplainHistoryDTO;
import crm.CrmComplainDTO;
import crm.CrmComplainHistoryDTO;
import util.DatabaseConnectionFactory;

public class CrmComplainHistoryDAO {
	
	public void insertComplainHistory(CrmComplainHistoryDTO crmComplainHistoryDTO) throws Exception{
		insert(crmComplainHistoryDTO, CrmComplainHistoryDTO.class, false);
	}
	
	public List<CrmComplainHistoryDTO> getComplainHistoryListByParentComplainHistoryID(long parentComplainHistoryID) throws Exception{
		String conditionString = " WHERE "+ getColumnName(CrmComplainHistoryDTO.class, "parentComplainHistoryID") + " = "+parentComplainHistoryID;
		return (List<CrmComplainHistoryDTO>) getAllObjectListFullyPopulated(CrmComplainHistoryDTO.class, conditionString);
	}
	public CrmComplainHistoryDTO getLatestComplainHistoryDTOByParentHistoryID(long parentHistoryID) throws Exception{
		Class classObject = CrmComplainHistoryDTO.class;
		String sql = " select * from "+getTableName(classObject)+" where "+getColumnName(classObject, "parentComplainHistoryID")
		+" = "+parentHistoryID+" order by "+getLastModificationTimeColumnName(classObject)+" desc limit 1";
		System.out.println(sql);
		ResultSet rs = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeQuery(sql);
		if(rs.next()){
			CrmComplainHistoryDTO complainHistoryDTO = new CrmComplainHistoryDTO();
			populateObjectFromDB(complainHistoryDTO, rs);
			return complainHistoryDTO;
		}else{
			return null;
		}
	}
	
	public CrmComplainHistoryDTO getComplainHistoryByComplainHistoryID(long complainHistoryID) throws Exception{
		CrmComplainHistoryDTO childCrmComplainHistoryDTO =  (CrmComplainHistoryDTO) getObjectFullyPopulatedByID(CrmComplainHistoryDTO.class, complainHistoryID);
		return childCrmComplainHistoryDTO;
	}
	public CrmComplainHistoryDTO getParentComplainHistoryByComplainHistoryID(long complainHistoryID) throws Exception{
		CrmComplainHistoryDTO childCrmComplainHistoryDTO =  (CrmComplainHistoryDTO) getObjectByID(CrmComplainHistoryDTO.class, complainHistoryID);
		if(childCrmComplainHistoryDTO.getParentComplainHistoryID() == null){
			return null;
		}
		return getComplainHistoryByComplainHistoryID(childCrmComplainHistoryDTO.getParentComplainHistoryID());
	}
	public int updateComplainHistoryDTO(CrmComplainHistoryDTO complainHistoryDTO) throws Exception{
		return updateEntity(complainHistoryDTO, CrmComplainHistoryDTO.class, false, false);
	}
	public int updateComplainHistoryDTO(CrmComplainHistoryDTO complainHistoryDTO,String[] propertyList) throws Exception{
		return updateEntityByPropertyList(complainHistoryDTO, CrmComplainHistoryDTO.class, false, false, propertyList);
	}
	
}
