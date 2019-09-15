package lliCommonChargeConfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import connection.DatabaseConnection;

import util.DAOResult;

public class LliCommonChargeConfigService {
	public static Logger logger = Logger.getLogger(LliCommonChargeConfigService.class);
	public void add(CommonChargeDTO dto) throws Exception {
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			new LliCommonChargeConfigDAO().add(dto, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug("fatal:",ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.fatal(ex);
			}
		} finally {
			databaseConnection.dbClose();
		}
	
	}
	
	/* public CommonChargeDTO getLatestDTO() throws Exception{
		  DatabaseConnection databaseConnection = new DatabaseConnection();
		
		
		  CommonChargeDTO latestDTO=null;
		  try{
				databaseConnection.dbOpen();
				databaseConnection.dbTransationStart();
				latestDTO =new LliCommonChargeConfigDAO().getLatestCommonChargeDTO(databaseConnection);
				databaseConnection.dbTransationEnd();
			}catch(Exception ex){
				logger.debug("error inside getLatest id DTO method", ex);
				try{
					databaseConnection.dbTransationRollBack();
				}catch(Exception ex2){
					logger.debug("error inside getLatest Table DTO method");
				}
				throw ex;
			}finally{
				databaseConnection.dbClose();
			}
			return latestDTO;
	 }*/
	 
	 public static void main(String args[]) throws Exception
	 {

	 }
	 
	 public CommonChargeDTO getCurrentActiveCommonChargeDTO() throws Exception {

			DatabaseConnection databaseConnection = new DatabaseConnection();

			CommonChargeDTO activeCharge = null;
			try {
				databaseConnection.dbOpen();
				databaseConnection.dbTransationStart();
				activeCharge = new LliCommonChargeConfigDAO().getCurrentActiveCommonChargeDTO(databaseConnection);
				databaseConnection.dbTransationEnd();
			} catch (Exception ex) {
				logger.debug("error inside getLatest id DTO method", ex);
				try {
					databaseConnection.dbTransationRollBack();
				} catch (Exception ex2) {
					logger.debug("error inside getLatest Table DTO method");
				}
				throw ex;
			} finally {
				databaseConnection.dbClose();
			}
			return activeCharge;
		}
		
		public List<CommonChargeDTO> getAllFutureCommonChargeDTOs() throws Exception {

			DatabaseConnection databaseConnection = new DatabaseConnection();

			List<CommonChargeDTO> allCommonCharges = new ArrayList<CommonChargeDTO>();
			try {
				databaseConnection.dbOpen();
				databaseConnection.dbTransationStart();
				allCommonCharges = new LliCommonChargeConfigDAO().getAllFutureCommonChargeDTOs(databaseConnection);
				databaseConnection.dbTransationEnd();
			} catch (Exception ex) {
				logger.debug("error inside getLatest id DTO method", ex);
				try {
					databaseConnection.dbTransationRollBack();
				} catch (Exception ex2) {
					logger.debug("error inside getLatest Table DTO method");
				}
				throw ex;
			} finally {
				databaseConnection.dbClose();
			}
			return allCommonCharges;
		}
}
