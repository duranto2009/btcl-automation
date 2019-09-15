package common.note;

import java.util.ArrayList;
import java.util.List;

import connection.DatabaseConnection;
import util.SqlGenerator;

public class CommonNoteDAO {

	public void insert( DatabaseConnection databaseConnection ) throws Exception{
		
		SqlGenerator.insert(this, this.getClass(), databaseConnection, false );
	}

	public static void insert( CommonNote note, DatabaseConnection databaseConnection ) throws Exception{

		SqlGenerator.insert( note, note.getClass(), databaseConnection, false );
	}

	public static CommonNote getById( long id, DatabaseConnection databaseConnection ) throws Exception{
		
		return (CommonNote) SqlGenerator.getObjectByID( CommonNote.class, id, databaseConnection );
	}
	
	public CommonNote getByRequestId( long requestID, DatabaseConnection databaseConnection ) throws Exception{
		
		List<CommonNote> noteList = SqlGenerator.getAllObjectListFullyPopulated(CommonNote.class, databaseConnection, " where comntReqID = " + requestID);
		if(noteList != null && noteList.size() > 0)
		{
			return noteList.get(0);
		}
		else return null;
	}
	
	public static void update( CommonNote note, DatabaseConnection databaseConnection ) throws Exception{
		
		SqlGenerator.updateEntity( note, note.getClass(), databaseConnection, false, false);
	}

}
