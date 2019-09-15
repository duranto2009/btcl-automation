package crm.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import crm.CrmDesignationDTO;
import crm.service.CrmDesignationService;
import repository.Repository;
import repository.RepositoryManager;
import util.DatabaseConnectionFactory;
import util.ServiceDAOFactory;
import util.SqlGenerator;

public class CrmDesignationRepository implements Repository {

	Logger logger = Logger.getLogger(this.getClass());
	CrmDesignationService crmDesignationService = (CrmDesignationService) ServiceDAOFactory
			.getService(CrmDesignationService.class);// new CrmService();
	private static CrmDesignationRepository instance = null;

	Map<Long, CrmDesignationDTO> mapOfPartialCrmDesignationDTOToDesignationID;
	Map<Integer, CrmDesignationDTO> mapOfPartialCrmDesignationDTOToInventoryCategoryID;

	private CrmDesignationRepository() {
		mapOfPartialCrmDesignationDTOToDesignationID = new HashMap<>();
		mapOfPartialCrmDesignationDTOToInventoryCategoryID = new HashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}

	public static synchronized CrmDesignationRepository getInstance() {
		if (instance == null) {
			instance = new CrmDesignationRepository();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CrmDesignationDTO> crmDesignationDTOs = new ArrayList<CrmDesignationDTO>();
		try {
			databaseConnection.dbOpen();
			crmDesignationDTOs = (List<CrmDesignationDTO>)util.SqlGenerator.getAllObjectForRepository(CrmDesignationDTO.class, databaseConnection, isFirstReload);
			for (CrmDesignationDTO crmDesignationDTO : crmDesignationDTOs) {
				mapOfPartialCrmDesignationDTOToDesignationID.remove(crmDesignationDTO.getDesignationID());
				if(!crmDesignationDTO.isDeleted()){
					mapOfPartialCrmDesignationDTOToDesignationID.put(crmDesignationDTO.getDesignationID(),
						crmDesignationDTO);
				}
				
				mapOfPartialCrmDesignationDTOToInventoryCategoryID.remove(crmDesignationDTO.getID());
				if(!crmDesignationDTO.isDeleted()){
					mapOfPartialCrmDesignationDTOToInventoryCategoryID.put(crmDesignationDTO.getID(), crmDesignationDTO);
				}
				
			}
		} 
		catch (Exception ex) {
			System.err.println("ex : " + ex);
			logger.error("Error", ex);
		}finally {
			databaseConnection.dbClose();
		}

	}

	@Override
	public String getTableName() {
		String tableName = "";
		try {
			tableName = SqlGenerator.getTableName(CrmDesignationDTO.class);
		} catch (Exception ex) {
		}
		return tableName;
	}

}
