package crm.repository;

import static util.SqlGenerator.getAllObjectForRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import crm.inventory.CRMInventoryCatagoryType;
import crm.service.CrmEmployeeService;
import repository.Repository;
import repository.RepositoryManager;
import util.DatabaseConnectionFactory;
import util.SqlGenerator;

public class CrmInventoryCategoryRepository implements Repository {

	Logger logger = Logger.getLogger(this.getClass());
	CrmEmployeeService crmService = new CrmEmployeeService();
	private static CrmInventoryCategoryRepository instance = null;

	Map<Integer, CRMInventoryCatagoryType> mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID;
	Map<Integer, CRMInventoryCatagoryType> mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID;
	Map<Integer, Map<Integer, CRMInventoryCatagoryType>> mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID;

	private CrmInventoryCategoryRepository() {
		mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID = new ConcurrentHashMap<>();
		mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID = new ConcurrentHashMap<>();
		mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID = new ConcurrentHashMap<>();
		RepositoryManager.getInstance().addRepository(this);
	}


	public static synchronized CrmInventoryCategoryRepository getInstance() {
		if (instance == null) {
			instance = new CrmInventoryCategoryRepository();
		}
		return instance;
	}

	@Override
	public void reload(boolean isFirstReload) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		List<CRMInventoryCatagoryType> intentoryCategoryTypeList;
		try {
			databaseConnection.dbOpen();
			intentoryCategoryTypeList = (List<CRMInventoryCatagoryType>) getAllObjectForRepository(
					CRMInventoryCatagoryType.class,databaseConnection,
					isFirstReload);
			for (CRMInventoryCatagoryType invCategoryType : intentoryCategoryTypeList) {
				mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.remove(invCategoryType.getID());
				if (!invCategoryType.isDeleted()) {
					mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.put(invCategoryType.getID(),
							invCategoryType);
				}
				if (invCategoryType.getParentCatagoryTypeID() == null) {
					mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID.remove(invCategoryType.getID());

					if (!invCategoryType.isDeleted()) {
						mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID
								.put(invCategoryType.getID(), invCategoryType);
					}
				} else {
					/* map contains this parent id */
					if (mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID
							.containsKey(invCategoryType.getParentCatagoryTypeID())) {
						Map<Integer, CRMInventoryCatagoryType> mapOfInventoryDTOToInventoryCategoryTypeID = mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID
								.get(invCategoryType.getParentCatagoryTypeID());
						mapOfInventoryDTOToInventoryCategoryTypeID.remove(invCategoryType.getID());
						if (!invCategoryType.isDeleted()) {
							mapOfInventoryDTOToInventoryCategoryTypeID.put(invCategoryType.getID(),
									invCategoryType);
						}
						if (mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.isEmpty()) {
							mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID
									.remove(invCategoryType.getParentCatagoryTypeID());
						}
					} else {
						Map<Integer, CRMInventoryCatagoryType> mapOfInventoryDTOToInventoryCategoryTypeID = new ConcurrentHashMap<>();
						if (!invCategoryType.isDeleted()) {
							mapOfInventoryDTOToInventoryCategoryTypeID.put(invCategoryType.getID(),
									invCategoryType);
							mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID
									.put(invCategoryType.getParentCatagoryTypeID(),
											mapOfInventoryDTOToInventoryCategoryTypeID);

						}
					}
				}

			}

		} catch (Exception ex) {
			logger.error("Error ", ex);
		} finally {
			databaseConnection.dbClose();
		}

	}


	@Override
	public String getTableName() {
		String tableName = "";
		try {
			tableName = SqlGenerator.getTableName(CRMInventoryCatagoryType.class);
		} catch (Exception ex) {
		}
		return tableName;
	}

}