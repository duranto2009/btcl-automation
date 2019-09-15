package crm.repository;

import crm.CrmDesignationDTO;
import repository.Repository;
import util.SqlGenerator;

public class CrmDesignationTreeRepository implements Repository {
	private static CrmDesignationTreeRepository instance = null;

	private CrmDesignationTreeRepository() {

	}

	public static synchronized CrmDesignationTreeRepository getInstance() {
		if (instance == null) {
			instance = new CrmDesignationTreeRepository();
		}
		return instance;
	}

	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub

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
