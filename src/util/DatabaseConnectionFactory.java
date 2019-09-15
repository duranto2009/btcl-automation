package util;
import java.util.Stack;

import connection.DatabaseConnection;
public class DatabaseConnectionFactory {
	private static ThreadLocal<Stack<DatabaseConnection>> databaseConnectionThreadLocalStack = ThreadLocal.withInitial(Stack::new);
    public static boolean isEmpty(){
    	return getDatabaseConnectionStack().isEmpty();
    }
	private static Stack<DatabaseConnection> getDatabaseConnectionStack(){
		return databaseConnectionThreadLocalStack.get();
	}
	public static DatabaseConnection getNewDatabaseConnection(){
		Stack<DatabaseConnection> databaseConnectionStack = getDatabaseConnectionStack();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnectionStack.push(databaseConnection);
		return databaseConnection;
	}
	public static DatabaseConnection getCurrentDatabaseConnection(){
		Stack<DatabaseConnection> databaseConnectionStack = getDatabaseConnectionStack();
		if(databaseConnectionStack.isEmpty()){
			throw new RuntimeException("No databaseConnection found in connection factory");
		}
		DatabaseConnection databaseConnection = databaseConnectionStack.peek();
		return databaseConnection;
	}
	public static void removeLastDatabaseConnection(){
		getDatabaseConnectionStack().pop();
	}
}

