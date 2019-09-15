package common.bill;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import login.LoginDTO;
import util.NavigationService;

public class BillSearchService implements NavigationService{
	
	int moduleID;
	Logger logger = Logger.getLogger(getClass());
	BillDAO billDAO = new BillDAO();
	public void setModuleID(int moduleID){
		this.moduleID = moduleID;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects)
			throws Exception {
		
		List<Long> billIDList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			billIDList = billDAO.getAllBillIDList(loginDTO,moduleID, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			
		}catch(Exception ex){
			
			logger.debug("FATAL",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			
		}finally{
			
			databaseConnection.dbClose();
			
		}
		
		return billIDList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria,
			LoginDTO loginDTO, Object... objects) throws Exception {
		
		List<Long> billIDList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			billIDList = billDAO.getBillIDListBySearchCriteria(loginDTO,moduleID, searchCriteria, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			
		}catch(Exception ex){
			
			logger.debug("FATAL",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			
		}finally{
			
			databaseConnection.dbClose();
			
		}
		
		return billIDList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects)
			throws Exception {
		
		List<BillDTO> billDTOs = new ArrayList<BillDTO>(); 
		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try{
			
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			billDTOs = billDAO.getBillDTOListByIDList( (List<Long>)recordIDs, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			
		}catch(Exception ex){
			
			logger.debug("FATAL",ex);
			try{
				databaseConnection.dbTransationRollBack();
			}catch(Exception ex2){}
			
		}finally{
			
			databaseConnection.dbClose();
			
		}
		
		return billDTOs;
	}

}
