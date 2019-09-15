package common;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import annotation.Transactional;
import util.ConnectionType;
import util.DatabaseConnectionFactory;
import util.FormPopulator;
import util.TransactionType;

public class UniversalDTOService {
	private String getTableName(Class<?> clazz) throws Exception{
		return clazz.getSimpleName();
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public <T> T get(Class<T> clazz) throws Exception{
		
		String sql = "select columnName,value from at_universal_table where tableName = '"+getTableName(clazz)+"'";
		ResultSet resultSet = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeQuery(sql);
		T returnObject = clazz.newInstance();
		
		while(resultSet.next()){
			FormPopulator.populate(returnObject, resultSet.getString("columnName"), resultSet.getString("value"));
		}
		
		return returnObject;
	}
	@Transactional(transactionType=TransactionType.INDIVIDUAL_TRANSACTION)
	public void update(Object object) throws Exception{
		String tableName = getTableName(object.getClass());
		for(Field field: object.getClass().getDeclaredFields()){
			field.setAccessible(true);
			String sql = "update at_universal_table set value='"+field.get(object)+"' where tableName = '"+tableName+"' and columnName = '"+field.getName()+"'";
			DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeUpdate(sql);
		}
	}
}
