package common;
import java.util.*;

import static util.SqlGenerator.*;
import connection.DatabaseConnection;
public class FineAndNotificationChart {
	Logger logger = Logger.getLogger(getClass());
	Map<Integer, List<NotificationConfigurationEntry> > mapOfConfigurationListToEntityTypeID 
		= new HashMap<Integer, List<NotificationConfigurationEntry> >();
	private static FineAndNotificationChart instance = null;
	/*public NotificationConfugarationEntry getFineAndNotificationConfugarationEntryByStateAndEntityTypeIDAndDay(int entityTypeID,int state,int fromDay,int toDay){
		List<NotificationConfugarationEntry> fineAndNotificationConfugarationEntries = getFineAndNotificationConfugarationEntriesByEntityTypeIDAndState(entityTypeID, state);
		for(NotificationConfugarationEntry fineAndNotificationConfugarationEntry: fineAndNotificationConfugarationEntries){
			if(fineAndNotificationConfugarationEntry.getDays() == day){
				return fineAndNotificationConfugarationEntry;
			}
		}
		for(int i = fineAndNotificationConfugarationEntries.size()-1;i>=0;i--){
			NotificationConfugarationEntry fineAndNotificationConfugarationEntry = fineAndNotificationConfugarationEntries.get(i);
			if(fromDay<=fineAndNotificationConfugarationEntry.getDays() && toDay>=fineAndNotificationConfugarationEntry.getDays()){
				return fineAndNotificationConfugarationEntry;
			}
		}
		return null;
	}*/
	public Map<Integer, List<NotificationConfigurationEntry> > getMapOfConfigListToEntityTypeID(){
		return mapOfConfigurationListToEntityTypeID;
	} 
	private FineAndNotificationChart(){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try{
			databaseConnection.dbOpen();
			List<NotificationConfigurationEntry> fineAndNotificationConfugarationEntries = 
					(List<NotificationConfigurationEntry>)
					getAllObjectForRepository(NotificationConfigurationEntry.class, databaseConnection, true);
			
			for(NotificationConfigurationEntry fineAndNotificationConfugarationEntry: 
														fineAndNotificationConfugarationEntries){
				if(!mapOfConfigurationListToEntityTypeID.containsKey(fineAndNotificationConfugarationEntry.getEntityTypeID())){
					mapOfConfigurationListToEntityTypeID.put(fineAndNotificationConfugarationEntry.getEntityTypeID(), 
												new ArrayList<NotificationConfigurationEntry> () );
				}
				mapOfConfigurationListToEntityTypeID.get(
						fineAndNotificationConfugarationEntry.getEntityTypeID()).
								add(fineAndNotificationConfugarationEntry);
			}
			for(List<NotificationConfigurationEntry> notiConfigEntriesOfAnEntity: 
				mapOfConfigurationListToEntityTypeID.values()){
				Collections.sort(notiConfigEntriesOfAnEntity);
				System.out.println("Enity type ID = "+notiConfigEntriesOfAnEntity.get(0).getEntityTypeID()
						+" and state = "+notiConfigEntriesOfAnEntity.get(0).getStatesSeparatedByComma());
				System.out.println(notiConfigEntriesOfAnEntity);
			}
			
			
		}catch(Exception ex){
			logger.debug("fatal",ex);
		}finally{
			databaseConnection.dbClose();
		}
	}
	public List<NotificationConfigurationEntry> getFineAndNotificationConfigurationEntriesByEntityTypeID
														(int entityTypeID,int state){
		if(!mapOfConfigurationListToEntityTypeID.containsKey(entityTypeID)){
			return new ArrayList<NotificationConfigurationEntry>();
		}
		return mapOfConfigurationListToEntityTypeID.get(entityTypeID);
	}
	public synchronized static FineAndNotificationChart getInstance(){
		if(instance == null){
			instance = new FineAndNotificationChart();
		}
		return instance;
	}
}
