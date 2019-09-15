package notification;

import org.apache.log4j.Logger;
import java.util.*;

import connection.DatabaseConnection;
import repository.Repository;
import util.SqlGenerator;

public class NotificationTemplateRepository implements Repository{
	static NotificationTemplateRepository instance = null;
	Logger logger = Logger.getLogger(getClass());
	Class classObject = NotificationTemplate.class;
	Map<Integer,NotificationTemplate> mapOfNotificationTemplateToActionStateID = new HashMap<Integer, NotificationTemplate>();
	
	private NotificationTemplateRepository(){}
	@Override
	public void reload(boolean reloadAll) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			List<NotificationTemplate> notificationTemplates = (List<NotificationTemplate>)SqlGenerator.getAllObjectForRepository(classObject, databaseConnection, reloadAll);
			for(NotificationTemplate notificationTemplate: notificationTemplates){
				mapOfNotificationTemplateToActionStateID.put(notificationTemplate.getActionStateID(), notificationTemplate);
			}
		}catch(Exception ex){
			logger.debug("FATAL",ex);
			
		}finally{
			databaseConnection.dbClose();
		}
	}
	public NotificationTemplate getNotificationTemplateByActionState(int actionState){
		return mapOfNotificationTemplateToActionStateID.get(actionState);
	}
	public static synchronized NotificationTemplateRepository getInstance(){
		if(instance == null){
			instance = new NotificationTemplateRepository();
		}
		return instance;
	}
	@Override
	public String getTableName() {
		String tableName = "";
		try{
			tableName = SqlGenerator.getTableName(classObject);
		}catch(Exception ex){
			logger.debug("FATAL",ex);
		}
		return tableName;
	}

}
