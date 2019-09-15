package util;
import static util.SqlGenerator.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import connection.DatabaseConnection;

public class ActivityLogDAO {
	public void insert(ActivityLogDTO activityLogDTO,DatabaseConnection databaseConnection) throws Exception{
		activityLogDTO.setID(databaseConnection.getNextID(getTableName(activityLogDTO.getClass())));
		SqlGenerator.insert(activityLogDTO,activityLogDTO.getClass(), databaseConnection, true);
	}
	public List<ActivityLogDTO> getActivityLogBetweenTimeRange(long fromTime, long toTime, DatabaseConnection databaseConnection) throws Exception{
		List<ActivityLogDTO> activityLogDTOs = new ArrayList();
		String sql = "select * from "+getTableName(ActivityLogDTO.class)+" where aclgActivityTime between "+fromTime+" and "+toTime;
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			activityLogDTOs.add(createActivityLogDTO(rs));
		}
		return activityLogDTOs;
	}
	private ActivityLogDTO createActivityLogDTO(ResultSet rs) throws Exception{
		ActivityLogDTO activityLogDTO = new ActivityLogDTO();
		populateObjectFromDB(activityLogDTO, rs);
		return activityLogDTO;
	}
	public List<ActivityLogDTO> getActivityLogDTOsOrderedByTime(DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString = " order by";
		return (List<ActivityLogDTO>)getAllObjectList(ActivityLogDTO.class, databaseConnection, conditionString);
	}
}
