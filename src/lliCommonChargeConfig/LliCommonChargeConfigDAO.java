package lliCommonChargeConfig;

import java.util.List;

import org.apache.log4j.Logger;

import common.RequestFailureException;
import connection.DatabaseConnection;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;
import lliCommonChargeConfig.CommonChargeDTO;

public class LliCommonChargeConfigDAO {
	Logger logger = Logger.getLogger(getClass());

	Class<CommonChargeDTO> classObject = CommonChargeDTO.class;
			
	public void add(CommonChargeDTO dto, DatabaseConnection databaseConnection) throws Exception {
		logger.debug("VpnCommonChargeConfigDAO add dto method");
		SqlGenerator.insert(dto, CommonChargeDTO.class, databaseConnection, false);
	}
	
	public List<CommonChargeDTO> getCommonChargeDTOListByDateRange(long fromDate,long toDate) throws Exception{

		if(fromDate>toDate){
			throw new RequestFailureException("From date must be less than to date");
		}
		
		List<CommonChargeDTO> commonChargeDTOs = ModifiedSqlGenerator.getAllObjectList(classObject, " where "+SqlGenerator.getColumnName(classObject, "activationDate")+"<="+fromDate+" and "+SqlGenerator.getColumnName(classObject, "isDeleted")+"=0 order by "+SqlGenerator.getColumnName(classObject,"activationDate")+" desc limit 1");
		if(commonChargeDTOs.isEmpty()){
			return commonChargeDTOs;
		}
		return ModifiedSqlGenerator.getAllObjectList(classObject, " where "+SqlGenerator.getColumnName(classObject, "activationDate")+" between "+fromDate+" and "+toDate+" and "+SqlGenerator.getColumnName(classObject, "isDeleted")+"=0 order by "+SqlGenerator.getColumnName(classObject, "activationDate"));
	}
	/*
	@SuppressWarnings("unchecked")
	public CommonChargeDTO getLatestCommonChargeDTO(DatabaseConnection databaseConnection) throws Exception{
		String idColumnName = SqlGenerator.getColumnName(CommonChargeDTO.class, "id");
		List<CommonChargeDTO>list=(List<CommonChargeDTO>)SqlGenerator.getAllObjectList(CommonChargeDTO.class, databaseConnection," order by  "+ idColumnName);
		logger.debug(list.size());
		if(list.size()>0)return list.get(list.size()-1);
		else return null;
	}*/
	
	public CommonChargeDTO getCurrentActiveCommonChargeDTO(DatabaseConnection databaseConnection) throws Exception {
		
		String activationDateColumnName  =  SqlGenerator.getColumnName( CommonChargeDTO.class, "activationDate" );
		
		String conditionGetFuture  = " where " + activationDateColumnName + " <= " + System.currentTimeMillis() + " and " + SqlGenerator.getColumnName(CommonChargeDTO.class, "isDeleted" ) + " = 0" + " order by " + activationDateColumnName + " desc limit 1";
		
		List<CommonChargeDTO> currentCharge = SqlGenerator.getAllObjectList( CommonChargeDTO.class, databaseConnection, conditionGetFuture );

		if( !currentCharge.isEmpty() )
			return currentCharge.get(0);
		else 
			return null;
	}


	public List<CommonChargeDTO> getAllFutureCommonChargeDTOs(DatabaseConnection databaseConnection) throws Exception {
		
		String activationDateColumnName  =  SqlGenerator.getColumnName( CommonChargeDTO.class, "activationDate" );
		
		String conditionGetFuture  = " where " + activationDateColumnName + " > " + System.currentTimeMillis();
		
		List<CommonChargeDTO> futureCharge = SqlGenerator.getAllObjectList( CommonChargeDTO.class, databaseConnection, conditionGetFuture );

		return futureCharge;
	}

}
