package crm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import common.StringUtils;
import connection.DatabaseConnection;
import crm.CrmDepartmentDTO;
import crm.CrmDesignationDTO;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

public class CrmDepartmentRepository  implements Repository {
	
	Logger logger = Logger.getLogger(CrmDepartmentRepository.class);
	private static CrmDepartmentRepository instance = null;

	Map<Long, CrmDepartmentDTO> mapOfCrmDepartmentDTOToRootDesignationID;
	Map<Long, CrmDepartmentDTO> mapOfCrmDepartmentDTOToCrmDepartmentID;

	private CrmDepartmentRepository() {
		mapOfCrmDepartmentDTOToRootDesignationID = new ConcurrentHashMap<>();
		mapOfCrmDepartmentDTOToCrmDepartmentID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}
	
	public List<CrmDepartmentDTO> getCrmDepartmentDTOByLocationAndPartialName(Long districtID,
			Long upazillaID,Long unionID,String partialName,long limit){
		List<CrmDepartmentDTO> crmDepartmentDTOs = new ArrayList<>();
		
		partialName = StringUtils.toUpperCase(partialName);
		
		for(CrmDepartmentDTO crmDepartmentDTO: mapOfCrmDepartmentDTOToCrmDepartmentID.values()){
			
			if(!StringUtils.isBlank(partialName)&& crmDepartmentDTO.getDepartmentName().toUpperCase().contains(partialName)){
				continue;
			}
			
			if(unionID!=null){
				
				if(unionID.equals(crmDepartmentDTO.getUpazilaID())){
					crmDepartmentDTOs.add(crmDepartmentDTO);
				}
				
			}else if(upazillaID != null){
				
				if(upazillaID.equals(crmDepartmentDTO.getUpazilaID())){
					crmDepartmentDTOs.add(crmDepartmentDTO);
				}
				
			}else if(districtID != null){
				
				if(districtID.equals(crmDepartmentDTO.getDistrictID())){
					crmDepartmentDTOs.add(crmDepartmentDTO);
				}
				
			}else{
				crmDepartmentDTOs.add(crmDepartmentDTO);
			}
		}
		
		return crmDepartmentDTOs;
	}

	public CrmDepartmentDTO getCrmDepartmentByRootDesignationID(long rootDesignationID) {	
		return mapOfCrmDepartmentDTOToRootDesignationID.get(rootDesignationID);
	}

	public CrmDepartmentDTO getCrmDepartmentDTOByDepartmentID(long departmentID) {
		return mapOfCrmDepartmentDTOToCrmDepartmentID.get(departmentID);
	}


	public static synchronized CrmDepartmentRepository getInstance() {
		if (instance == null) {
			instance = new CrmDepartmentRepository();
		}
		return instance;
	}
	
	public List<CrmDepartmentDTO> getDepartmentDTOsByPartialDeptNameAndLocationIDs(Long districtId, Long upazilaID, Long unionID, String partialDeptName){
		return getDepartmentDTOsByPartialDeptNameAndLocationIDs(districtId,  upazilaID, unionID, partialDeptName, Integer.MAX_VALUE);
	}
	public List<CrmDepartmentDTO> getDepartmentDTOsByPartialDeptNameAndLocationIDs(Long districtId, Long upazilaID, Long unionID, String partialDeptName, int limit){
		List<CrmDepartmentDTO> crmDepartmentDTOs = new ArrayList<>();
		
		for(CrmDepartmentDTO crmDepartmentDTO: mapOfCrmDepartmentDTOToCrmDepartmentID.values()){
			if(districtId!=null && !districtId.equals(crmDepartmentDTO.getDistrictID())){
				continue;
			}
			if(upazilaID != null && !upazilaID.equals(crmDepartmentDTO.getUpazilaID())){
				continue;
			}
			if(unionID != null && !unionID.equals(crmDepartmentDTO.getUnionID())){
				continue;
			}
			if(StringUtils.isBlank(partialDeptName) || crmDepartmentDTO.getDepartmentName().toUpperCase().contains(StringUtils.toUpperCase(partialDeptName))){
				crmDepartmentDTOs.add(crmDepartmentDTO);
			}
			if(crmDepartmentDTOs.size() == limit){
				break;
			}
		}
		
		return crmDepartmentDTOs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CrmDepartmentDTO> crmDepartmentDTOList = new ArrayList<>();
		try {
			databaseConnection.dbOpen();
			crmDepartmentDTOList = (List<CrmDepartmentDTO>) util.SqlGenerator.getAllObjectForRepository(CrmDepartmentDTO.class,
					databaseConnection, isFirstReload);
			
			for (CrmDepartmentDTO crmDepartmentDTO : crmDepartmentDTOList) {
				
				
				
				
				CrmDepartmentDTO previousDepartmentDTO = mapOfCrmDepartmentDTOToCrmDepartmentID.get(crmDepartmentDTO.getID());
				if(previousDepartmentDTO!=null){
					if(previousDepartmentDTO.getLastModificationTime() == crmDepartmentDTO.getLastModificationTime()){
						continue;
					}
					mapOfCrmDepartmentDTOToCrmDepartmentID.remove(previousDepartmentDTO.getID());
					if(previousDepartmentDTO.getRootDesignationID()!=null){
						mapOfCrmDepartmentDTOToRootDesignationID.remove(previousDepartmentDTO.getRootDesignationID());
					}
				}
				if(!crmDepartmentDTO.isDeleted()){
					mapOfCrmDepartmentDTOToCrmDepartmentID.put(crmDepartmentDTO.getID(), crmDepartmentDTO);
					if(crmDepartmentDTO.getRootDesignationID()!=null){
						mapOfCrmDepartmentDTOToRootDesignationID.put(crmDepartmentDTO.getRootDesignationID(), crmDepartmentDTO);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error", ex);
		}finally {
			databaseConnection.dbClose();
		}
		
	}

	@Override
	public String getTableName() {
		String tableName = "";
		try {
			tableName = SqlGenerator.getTableName(CrmDepartmentDTO.class);
		} catch (Exception ex) {
		}
		return tableName;
	}

}
