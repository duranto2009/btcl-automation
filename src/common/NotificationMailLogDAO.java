package common;

import static util.SqlGenerator.*;
import connection.DatabaseConnection;
import java.util.*;
public class NotificationMailLogDAO {
	public void insertNotificationMailLog(NotificationMailLog notificationMailLog,DatabaseConnection databaseConnection) throws Exception{
		insert(notificationMailLog, NotificationMailLog.class, databaseConnection, false);
	}
	public void updateNotificationMailLog(NotificationMailLog notificationMailLog,DatabaseConnection databaseConnection) throws Exception{
		updateEntity(notificationMailLog, NotificationMailLog.class, databaseConnection, false,false);
	}
	public NotificationMailLog getNotificationMailLogByEntityIDAndEntityTypeID(long entityID,int entityTypeID,DatabaseConnection databaseConnection) throws Exception{
		Class classObject = NotificationMailLog.class;
		String entityIDColumnName = getColumnName(classObject, "entityID");
		String entityTypeIDColumnName = getColumnName(classObject, "entityTypeID");
		String conditionString = " where "+entityIDColumnName+"="+entityID+" and "+entityTypeIDColumnName+"="+entityTypeID;
		List<NotificationMailLog> notificationMailLogs = (List<NotificationMailLog>)getAllObjectList(classObject, databaseConnection, conditionString);
		return notificationMailLogs.isEmpty()?null:notificationMailLogs.get(0);
	}
}
