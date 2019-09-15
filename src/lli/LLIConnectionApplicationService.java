package lli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.TransactionType;

public class LLIConnectionApplicationService implements NavigationService{
	@DAO
	LLIConnectionApplicationDAO lliConnectionApplicationDAO;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(transactionType = TransactionType.READONLY)
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		Hashtable hashTable = new Hashtable();
		if(objects.length == 1) {
			hashTable.put("applicationType", objects[0]);
		}
		return getIDsWithSearchCriteria(hashTable, loginDTO, objects);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(transactionType = TransactionType.READONLY)
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
		return lliConnectionApplicationDAO.getIDsWithSearchCriteria(searchCriteria,loginDTO);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType = TransactionType.READONLY)
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return lliConnectionApplicationDAO.getLLIApplicationListByApplicationIDList((List)recordIDs);
	}
	
	
	/**
	 * @author Dhrubo
	 */
	@Transactional
	public void insertNewApplication(LLIApplicationType lliConnectionApplication) throws Exception{
		lliConnectionApplication.setID(DatabaseConnectionFactory.getCurrentDatabaseConnection()
				.getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLIApplicationType.class)));
		lliConnectionApplicationDAO.insertLLIConnectionApplication(lliConnectionApplication);
	}
	
	/**
	 * @author Dhrubo
	 */
	@Transactional
	public void insertNewApplicationInstance(LLIApplicationInstance lliConnectionApplicationInstance) throws Exception {
		lliConnectionApplicationInstance.setID(DatabaseConnectionFactory.getCurrentDatabaseConnection()
				.getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLIApplicationInstance.class)));
		lliConnectionApplicationInstance.setApplicationDate(System.currentTimeMillis());
		lliConnectionApplicationDAO.insertLLIConnectionApplicationInstance(lliConnectionApplicationInstance);
	}

	/**
	 * @author Dhrubo
	 */
	@Transactional(transactionType = TransactionType.READONLY)
	public List<LLIDropdownPair> getApplicationTypeList() throws Exception {
		List<LLIApplicationType> lliConnectionApplicationList = lliConnectionApplicationDAO.getApplicationTypeList();
		List<LLIDropdownPair> LLIDropdownPairList = new ArrayList<>();
		for(LLIApplicationType lliConnectionApplication : lliConnectionApplicationList) {
			LLIDropdownPairList.add( new LLIDropdownPair(lliConnectionApplication.getID(), lliConnectionApplication.getName()) );
		}
		return LLIDropdownPairList;
	}
	/**
	 * @author Dhrubo
	 */
	@Transactional(transactionType = TransactionType.READONLY)
	public String getApplicationFieldsByApplicationTypeID(Long applicationTypeID) throws Exception {
		return lliConnectionApplicationDAO.getApplicationTypeByApplicationTypeID(applicationTypeID).getFields();
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public LLIApplicationType getApplicationTypeByID(Long applicationTypeID) throws Exception {
		return lliConnectionApplicationDAO.getApplicationTypeByApplicationTypeID(applicationTypeID);
	}
	@Transactional(transactionType = TransactionType.READONLY)
	public LLIApplicationInstance getApplicationInstanceByID(Long applicationInstanceID) throws Exception {
		return lliConnectionApplicationDAO.getApplicationInstanecByApplicationInstanceID(applicationInstanceID);
	}
	
}
