package comment;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import connection.DatabaseConnection;
import login.LoginDTO;
import request.CommonRequestDTO;
import user.UserDTO;
import util.SqlGenerator;

import static util.SqlGenerator.*;
 
public class CommentDAO {
	public void insertComment(CommentDTO commentDTO,
			DatabaseConnection databaseConnection) throws Exception {
		insert(commentDTO, CommentDTO.class, databaseConnection, false);
	}

	public void updateComment(CommentDTO commentDTO,
			DatabaseConnection databaseConnection) throws Exception {
		updateEntity(commentDTO, CommentDTO.class, databaseConnection,
				false,false);
	}

	public void deleteComment(long commentID,
			DatabaseConnection databaseConnection) throws Exception {
		// deleteEntityByID(CommentDTO.class, databaseConnection, commentID);
	}

	public void insertCommentDocument(CommentDocumentDTO commentDocumentDTO,
			DatabaseConnection databaseConnection) throws Exception {
		insert(commentDocumentDTO, CommentDocumentDTO.class,
				databaseConnection, false);
	}

	public void deleteCommentDocument(long commentDocumentID,
			DatabaseConnection databaseConnection) throws Exception {
		// deleteEntityByID(CommentDocumentDTO.class, databaseConnection,
		// commentDocumentID);
	}

	public CommentDTO getCommentByID(long commentID,
			DatabaseConnection databaseConnection) throws Exception {
		return (CommentDTO) getObjectByID(CommentDTO.class, commentID,
				databaseConnection);
	}

	public CommentDocumentDTO getCommentDocumentByCommentDocumentID(
			long commentDocumentID, DatabaseConnection databaseConnection)
			throws Exception {
		return (CommentDocumentDTO) getObjectByID(CommentDocumentDTO.class,
				commentDocumentID, databaseConnection);
	}

	public List<CommentDocumentDTO> getCommentDTOListByCommentID(
			long commentID, DatabaseConnection databaseConnection)
			throws Exception {
		String conditionString = " where "
				+ getColumnName(CommentDocumentDTO.class, "commentID") + " = "
				+ commentID;
		return (List<CommentDocumentDTO>) getAllObjectList(
				CommentDocumentDTO.class, databaseConnection, conditionString);
	}

	public CommentDTO getCommentDTOByCommentID(long commentID,
			DatabaseConnection databaseConnection) throws Exception {
		return (CommentDTO) getObjectByID(CommentDTO.class, commentID,
				databaseConnection);
	}

	
	public List<CommentDTO> getCommentDTOByEntityTypeAndIDAndModuleID( long entityTypeID, long entityID, DatabaseConnection databaseConnection, int offset, int length) throws Exception {
		String conditionString = " where "
				+ getColumnName(CommentDTO.class, "entityTypeID") + " = "
				+ entityTypeID + " and "
				+ getColumnName(CommentDTO.class, "entityID") + " = "
				+ entityID + " order by "
				+ getColumnName(CommentDTO.class, "time") + " desc  limit "
				+ offset + "," + length;
		return (List<CommentDTO>) getAllObjectList(CommentDTO.class,
				databaseConnection, conditionString);
	}
	
	public ArrayList<Long> getIDs( long entityID, DatabaseConnection databaseConnection, LoginDTO loginDTO) throws Exception {
		ArrayList<Long> IDs = new ArrayList<Long>();
		 String conditionString = " where " + getColumnName(CommentDTO.class, "entityID") + " = " + entityID + " order   by cTime desc";
		 
		ArrayList<CommentDTO> comments = (ArrayList<CommentDTO>) getAllObjectList(CommentDTO.class, databaseConnection, conditionString);
		for (CommentDTO dto : comments) {
			IDs.add(dto.getID());
		}
		return IDs;
	}
	
	public Collection getDTOs(Collection recordIDs, DatabaseConnection databaseConnection) throws Exception {
    	String sql = "SELECT * from at_comment ";
    	if (recordIDs.size() > 0)
    	{
    		sql += " where ";
    		for (int i = 0; i < recordIDs.size(); i++)
    		{
    			sql += " cID = " + ( (ArrayList) recordIDs).get(i);
    			if (i <= recordIDs.size() - 2)
    			{
    				sql += " or ";
    			}
    		}

    	}
    	sql+=" order by cTime desc"; 
    	
    	Collection data = new ArrayList();
    	Statement stmt = databaseConnection.getNewStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	while (rs.next())
    	{
    		CommentDTO dto = new CommentDTO();

    		SqlGenerator.populateObjectFromDB(dto, rs);
    		data.add(dto);
    	}

        return data;
	}
}
