package file;

import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import comment.CommentDTO;
import complain.ComplainDTO;
import connection.DatabaseConnection;
import util.ModifiedSqlGenerator;

public class FileDAO {
	
	Logger logger = Logger.getLogger(this.getClass());

	public void insert(FileDTO fileDTO, DatabaseConnection databaseConnection) throws Exception {
		util.SqlGenerator.insert(fileDTO, fileDTO.getClass(), databaseConnection, true);		
	}
	
	public void insertFile(FileDTO fileDTO) throws Exception {
		ModifiedSqlGenerator.insert(fileDTO);
	}
	public ArrayList<FileDTO> getFileByEntity(int entityTypeID, long entityID, DatabaseConnection databaseConnection) throws Exception {
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		String conditionString = " where " + getColumnName(FileDTO.class, "docEntityTypeID") + " = " + entityTypeID + " and " + getColumnName(FileDTO.class, "docEntityID") + " = " + entityID;
		fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);

		return fileDTOs;
	}
	
	public FileDTO getFilebyDocumentID(int documentID, DatabaseConnection databaseConnection) throws Exception {
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		FileDTO fileDTO = new FileDTO();

		String conditionString = " where " + getColumnName(FileDTO.class, "docID") + " = " + documentID ;
		fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);
		if(fileDTOs.size()>0)
			fileDTO = fileDTOs.get(0);


		return fileDTO;
	}
	
	public FileDTO getFileByEntityTypeAndEntity(int entityTypeID, long entityID, DatabaseConnection databaseConnection) throws Exception {
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		FileDTO fileDTO = new FileDTO();


		String conditionString = " where " + getColumnName(FileDTO.class, "docEntityTypeID") + " = " + entityTypeID + " and " + getColumnName(FileDTO.class, "docEntityID") + " = " + entityID;
		fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);
		if(fileDTOs.size()>0)
			fileDTO = fileDTOs.get(0);


		return fileDTO;
	}
	
	
	public ArrayList<FileDTO> getFileByEntityType(int entityTypeID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			String conditionString = " where " + getColumnName(FileDTO.class, "docEntityTypeID") + " = " + entityTypeID ;
			fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("FileDAO : ", ex2);
			}
			logger.debug("FileDAO : ", ex);
		} finally {
			databaseConnection.dbClose();
		}

		return fileDTOs;
	}
	public ArrayList<FileDTO> getFileByEntityType(int entityTypeID, long entityID ) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			String conditionString = " where " + getColumnName(FileDTO.class, "docEntityTypeID") + " = " + entityTypeID + " and " + getColumnName(FileDTO.class, "docEntityID") + " = " + entityID;
			fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("FileDAO : ", ex2);
			}
			logger.debug("FileDAO : ", ex);
		} finally {
			databaseConnection.dbClose();
		}

		return fileDTOs;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getFileMapByEntityType(int entityTypeID) throws Exception{
		ArrayList<FileDTO> fileDTOs = getFileByEntityType(entityTypeID);
		Map fileMap = new HashMap<Long,FileDTO>();
		
		for (FileDTO fileDTO : fileDTOs) {
			fileMap.put(fileDTO.getDocID(),fileDTO);
		}
		
		return fileMap;
	}

	public void removeFileUsingEntityTypeIDandEntity(int docEntityTypeID, long docEntityID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			String sql = "DELETE FROM at_document WHERE docEntityTypeID = "+docEntityTypeID+" AND docEntityID="+docEntityID;
			Statement statement =   databaseConnection.getNewStatement();
			statement.execute(sql);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("FileDAO : ", ex2);
			}
			logger.debug("FileDAO : ", ex);
		} finally {
			databaseConnection.dbClose();
		}
		
	}
	public boolean isProfilePictureSet(long entityID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		FileDTO fileDTO = null;
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			String conditionString = " where " + getColumnName(FileDTO.class, "docTypeID") + " = " + FileTypeConstants.CLIENT.CLIENT_PROFILE_PICTURE + " and " + getColumnName(FileDTO.class, "docEntityID") + " = " + entityID+" order by docID desc limit 0, 1";
			fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);
			if(fileDTOs.size()>0)
				fileDTO = fileDTOs.get(0);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			logger.debug("FileDAO : ", ex);
		} finally {
			databaseConnection.dbClose();
		}

		if(fileDTO!=null && StringUtils.isNotBlank(fileDTO.getDocLocalFileName())){
			return true;
		}
		
		return false;
	}
	public String getProfilePictureEntityID(long entityID) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		ArrayList<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		FileDTO fileDTO = new FileDTO();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			String conditionString = " where " + getColumnName(FileDTO.class, "docTypeID") + " = " + FileTypeConstants.CLIENT.CLIENT_PROFILE_PICTURE + " and " + getColumnName(FileDTO.class, "docEntityID") + " = " + entityID+" order by docID desc limit 0, 1";
			fileDTOs = (ArrayList<FileDTO>) getAllObjectList(FileDTO.class, databaseConnection, conditionString);
			if(fileDTOs.size()>0)
				fileDTO = fileDTOs.get(0);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			logger.debug("FileDAO : ", ex);
		} finally {
			databaseConnection.dbClose();
		}

		if(StringUtils.isBlank(fileDTO.getDocLocalFileName())){
			return "assets/layouts/layout4/img/avatar.png";
		}
		return FileTypeConstants.FINAL_UPLOAD_DIR+fileDTO.getDocLocalFileName();
	}

	public void forwardFile(int entityTypeID, long entityID, int newEntityTypeID, long newEntityID, DatabaseConnection databaseConnection) throws Exception {
		// TODO Auto-generated method stub
		FileDAO fileDAO=new FileDAO();
		ArrayList<FileDTO> fileList=fileDAO.getFileByEntity(entityTypeID, entityID, databaseConnection);

		if(fileList != null && fileList.size() > 0)
		{
			for(FileDTO fileDTO: fileList)
			{
				fileDTO.setDocEntityTypeID(newEntityTypeID);					
				fileDTO.setDocEntityID(newEntityID);
				fileDAO.insert(fileDTO, databaseConnection);
			}
		}
	}

}
