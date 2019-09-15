package common;
import static util.SqlGenerator.*;
import java.util.*;

import connection.DatabaseConnection;
public class FineConfigDAO {
	public List<FineConfig> getActiveBillFineConfigs(DatabaseConnection databaseConnection) throws Exception{
		
		return getBillFineConfigsByCurrentTime(System.currentTimeMillis(), databaseConnection);
	}
	public List<FineConfig> getBillFineConfigsByCurrentTime(long currentTime,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = FineConfig.class;
		String activationDateColumnName = getColumnName(classObject,"activationDate");
		String conditionString = " where "+activationDateColumnName+" in ( "+
		"select max("+activationDateColumnName+") from "+getTableName(classObject)+" where "+activationDateColumnName+"<="+currentTime+")";
		return (List<FineConfig>)getAllObjectList(classObject, databaseConnection, conditionString);
	}
	
}
