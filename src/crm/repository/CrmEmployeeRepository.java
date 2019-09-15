package crm.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;

import connection.DatabaseConnection;
import crm.CrmEmployeeDTO;
import crm.service.CrmEmployeeService;
import repository.Repository;
import repository.RepositoryManager;
import util.DatabaseConnectionFactory;
import util.ServiceDAOFactory;
import util.SqlGenerator;

public class CrmEmployeeRepository implements Repository {

	Logger logger = Logger.getLogger(this.getClass());
	CrmEmployeeService crmEmployeeService = (CrmEmployeeService) ServiceDAOFactory.getService(CrmEmployeeService.class);// new
																														// CrmService();
	private static CrmEmployeeRepository instance = null;

	Map<Long, CrmEmployeeDTO> mapOfPartialEmployeeDTOToEmployeeID;
	Map<Long, CrmEmployeeDTO> mapOfPartialEmployeeDTOToInventoryItemID;
	Map<Long, Set<CrmEmployeeDTO>> mapOfPartialEmployeeDTOToUserID;
//	Map<Integer,Set<Long>> mapOfEmployeeIDSetToCategoryTypeID; 
	
	Map<Long, Set<CrmEmployeeDTO>> mapOfEmployeeIDSetToUserID;

	private CrmEmployeeRepository() {
		mapOfPartialEmployeeDTOToEmployeeID = new ConcurrentHashMap<>();
		mapOfPartialEmployeeDTOToInventoryItemID = new ConcurrentHashMap<>();
		mapOfPartialEmployeeDTOToUserID = new ConcurrentHashMap<>();
		mapOfEmployeeIDSetToUserID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}

	public static synchronized CrmEmployeeRepository getInstance() {
		if (instance == null) {
			instance = new CrmEmployeeRepository();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CrmEmployeeDTO> crmEmployeeDTOList = new ArrayList<>();
		try {
			databaseConnection.dbOpen();
			crmEmployeeDTOList = (List<CrmEmployeeDTO>) util.SqlGenerator.getAllObjectForRepository(CrmEmployeeDTO.class,
					databaseConnection, isFirstReload);
			
			for (CrmEmployeeDTO crmEmployeeDTO : crmEmployeeDTOList) {
				
				CrmEmployeeDTO previousCrmEmployeeDTO = mapOfPartialEmployeeDTOToEmployeeID.get(crmEmployeeDTO.getCrmEmployeeID());
				if(previousCrmEmployeeDTO != null && previousCrmEmployeeDTO.getLastModificationTime()==crmEmployeeDTO.getLastModificationTime()){
					continue;
				}
				
				if(previousCrmEmployeeDTO != null){
					mapOfPartialEmployeeDTOToInventoryItemID.remove(previousCrmEmployeeDTO.getID() );
					mapOfPartialEmployeeDTOToEmployeeID.remove(previousCrmEmployeeDTO.getCrmEmployeeID());
					/*mapOfEmployeeIDSetToCategoryTypeID.get(previousCrmEmployeeDTO.getInventoryCatagoryTypeID()).remove(previousCrmEmployeeDTO.getCrmEmployeeID());
					if(mapOfEmployeeIDSetToCategoryTypeID.get(previousCrmEmployeeDTO.getInventoryCatagoryTypeID()).isEmpty()){
						mapOfEmployeeIDSetToCategoryTypeID.remove(previousCrmEmployeeDTO.getInventoryCatagoryTypeID());
					}*/
				}
				if(!crmEmployeeDTO.isDeleted()){
					mapOfPartialEmployeeDTOToEmployeeID.put(crmEmployeeDTO.getCrmEmployeeID(), crmEmployeeDTO);
					mapOfPartialEmployeeDTOToInventoryItemID.put(crmEmployeeDTO.getID(), crmEmployeeDTO);
					/*if(!mapOfEmployeeIDSetToCategoryTypeID.containsKey(crmEmployeeDTO.getInventoryCatagoryTypeID())){
						mapOfEmployeeIDSetToCategoryTypeID.put(crmEmployeeDTO.getInventoryCatagoryTypeID(),new ConcurrentHashSet<>());
					}
					mapOfEmployeeIDSetToCategoryTypeID.get(crmEmployeeDTO.getInventoryCatagoryTypeID()).add(crmEmployeeDTO.getCrmEmployeeID());
					*/
					
				}
				if(previousCrmEmployeeDTO != null && previousCrmEmployeeDTO.getUserID()!=null){
					
					if(mapOfEmployeeIDSetToUserID.containsKey(previousCrmEmployeeDTO.getUserID())){
						mapOfEmployeeIDSetToUserID.get(previousCrmEmployeeDTO.getUserID()).remove(previousCrmEmployeeDTO);
					}
					if(mapOfEmployeeIDSetToUserID.get(previousCrmEmployeeDTO.getUserID()).isEmpty()){
						mapOfEmployeeIDSetToUserID.remove(previousCrmEmployeeDTO.getUserID());
					}
				}
				
				if(crmEmployeeDTO.getUserID()!= null && !crmEmployeeDTO.isDeleted()){
					
					if(!mapOfEmployeeIDSetToUserID.containsKey(crmEmployeeDTO.getUserID())){
						mapOfEmployeeIDSetToUserID.put(crmEmployeeDTO.getUserID(), new HashSet<>());
					}
					mapOfEmployeeIDSetToUserID.get(crmEmployeeDTO.getUserID()).add(crmEmployeeDTO);
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
			tableName = SqlGenerator.getTableName(CrmEmployeeDTO.class);
		} catch (Exception ex) {
		}
		return tableName;
	}

}
