package util;

import databasemanager.DatabaseManager;

public class SqlExecutionTemplate {
	static private DatabaseManager databaseManager = null;

	public static DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public static void setDatabaseManager(DatabaseManager databaseManager) {
		SqlExecutionTemplate.databaseManager = databaseManager;
	}
	public static  <R> R execute(SqlExecutor<R> sqlExecutor){
		
		return sqlExecutor.execute();
	}
}
