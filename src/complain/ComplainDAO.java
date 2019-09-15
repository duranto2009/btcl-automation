package complain;

import static util.SqlGenerator.getAllIDList;
import static util.SqlGenerator.getAllObjectList;
import static util.SqlGenerator.getColumnName;
import static util.SqlGenerator.getIDListFromSearchCriteria;
import static util.SqlGenerator.getLastModificationTimeColumnName;
import static util.SqlGenerator.getObjectByID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import login.LoginDTO;
import util.SqlGenerator;
import util.TimeConverter;
import comment.CommentDTO;
import common.ClientDTO;
import common.EntityTypeConstant;
import org.apache.log4j.Logger;
import common.bill.BillDTO;
import complain.ComplainSubjectDTO;
import connection.DatabaseConnection;
import crm.CrmComplainDTO;

public class ComplainDAO {
	Logger logger = Logger.getLogger(getClass());
	public Collection<?> getAllComplainSubject() throws Exception{
		Collection<?> complainSubjectDTOs = new ArrayList<ComplainSubjectDTO>();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.dbOpen();
		complainSubjectDTOs = (Collection<?>) SqlGenerator.getAllObjectList(ComplainSubjectDTO.class, databaseConnection);
		databaseConnection.dbClose();
		
		return complainSubjectDTOs;
	}
	
	public HashMap<Integer,ComplainSubjectDTO> getAllComplainSubjectMap() throws Exception{
		ArrayList<ComplainSubjectDTO> complainSubjectDTOs = (ArrayList<ComplainSubjectDTO>)getAllComplainSubject();
		HashMap<Integer,ComplainSubjectDTO> allComplainSubjectMap = new HashMap<Integer,ComplainSubjectDTO>();
		
		for (ComplainSubjectDTO complainSubjectDTO : complainSubjectDTOs) {
			allComplainSubjectMap.put(complainSubjectDTO.getCsID(),complainSubjectDTO);
		}
		
		return allComplainSubjectMap;
	}
	
	public Collection<?> getAllComplain() throws Exception{
		Collection<?> complainDTOs = new ArrayList<ComplainDTO>();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.dbOpen();
		complainDTOs = (Collection<?>) SqlGenerator.getAllObjectList(ComplainDTO.class, databaseConnection);
		databaseConnection.dbClose();
		
		return complainDTOs;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getAllComplainMap() throws Exception{
		ArrayList<ComplainDTO> complainDTOs = (ArrayList<ComplainDTO>)getAllComplain();
		Map allComplainMap = new HashMap<Long,ComplainDTO>();
		
		for (ComplainDTO complainDTO : complainDTOs) {
			allComplainMap.put(complainDTO.getID(),complainDTO);
		}
		
		return allComplainMap;
	}

	public void addComplain(ComplainDTO complainDTO, DatabaseConnection databaseConnection) throws Exception {
		SqlGenerator.insert(complainDTO, complainDTO.getClass(), databaseConnection, false);		
	}

	public void addComplainHistory(ComplainHistoryDTO complainHistoryDTO, DatabaseConnection databaseConnection) throws Exception {
		SqlGenerator.insert(complainHistoryDTO, complainHistoryDTO.getClass(), databaseConnection, false);	
	}

	public Collection<?> getComplainHistoryByComplainID(long complainID, LoginDTO loginDTO) throws Exception {
		String conditionString = " where "+getColumnName(ComplainHistoryDTO.class, "complainID")+" = "+complainID + " order by "+getColumnName(ComplainHistoryDTO.class, "lastModificationTime")+" asc";
		Collection<?> complainHistoryDTOs = new ArrayList<ComplainHistoryDTO>();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.dbOpen();
		complainHistoryDTOs = (Collection<?>) SqlGenerator.getAllObjectList(ComplainHistoryDTO.class, databaseConnection,conditionString);
		databaseConnection.dbClose();
		
		return complainHistoryDTOs;
	}
	
	public Map getComplainHistoryMap(ArrayList<ComplainHistoryDTO> complainHistoryDTOs) throws Exception{
		Map complainMapByComplainID = new HashMap<Long,ComplainHistoryDTO>();
		
		for (ComplainHistoryDTO complainHistoryDTO : complainHistoryDTOs) {
			complainMapByComplainID.put(complainHistoryDTO.getID(),complainHistoryDTO);
		}
		
		return complainMapByComplainID;
	}

	public Collection<Long> getComplainIDs(LoginDTO loginDTO, int moduleID, DatabaseConnection databaseConnection) throws Exception {
		Collection<Long> complainIDs = new ArrayList<Long>();
		
		Class classObject = ComplainDTO.class;
		String conditionString = " where "+ getColumnName(classObject,"moduleID")+" = "+moduleID;
		if(!loginDTO.getIsAdmin()){
			conditionString += " and "+getColumnName(classObject, "clientID")+" = "+loginDTO.getAccountID();
		}
		
		return getAllIDList(classObject, databaseConnection, conditionString);
	}

	public Collection<ComplainDTO> getComplainDTOsByIDList (Collection recordIDs,int moduleID, DatabaseConnection databaseConnection) throws Exception {
		Collection<ComplainDTO> complainDTOs = new ArrayList<ComplainDTO>();
	
		for (long id : (ArrayList<Long>) recordIDs) {
			complainDTOs.add((ComplainDTO) getObjectByID(ComplainDTO.class, id, databaseConnection));
		}
		return complainDTOs;
	}
	
	public Collection<Long> getComplainIDsFromSearchCriteria(LoginDTO loginDTO, Hashtable<String, String>  criteriaMap,int moduleID,
			DatabaseConnection databaseConnection) throws Exception {
		
		if(!loginDTO.getIsAdmin()){
			criteriaMap.remove("clientID");
			criteriaMap.put("clientID", ""+loginDTO.getAccountID());
		}
		
		String[] keys = new String[]          {"chStatus","creationTimeFrom","creationTimeTo","cmID","cmPriority","moduleID","clientID","clientName"};
		String[] operators = new String[]     {"="       ,">="              ,"<="            ,"="   ,"="         ,"="       ,"="	   ," IN "};
		String[] dtoColumnNames = new String[]{"status"  ,"creationTime"    ,"creationTime"  ,"ID"  ,"priority"  ,"moduleID","clientID","clientID"};
		Class classObject = ComplainDTO.class;
		String fixedCondition = "";// " floor(("+ getColumnName(classObject,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition, databaseConnection);
		
		
		return IDList;
	
	}

	/*public Collection<Long> getComplainIDsFromSearchCriteria(LoginDTO loginDTO, Hashtable p_searchCriteria,int moduleID,
			DatabaseConnection databaseConnection) throws Exception {

			Collection<Long> result = new ArrayList<Long>();
			HashMap<Long,Long>complainIDMap = new HashMap<Long, Long>();
			String sql = "SELECT distinct chComplainID,chID from at_complaint_history ";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (p_searchCriteria != null) {
				Enumeration keys = p_searchCriteria.keys();
				if (p_searchCriteria.size() > 0) {
					int j = 0;
					for (int i = 0; i < p_searchCriteria.size(); i++) {
						String key = (String) keys.nextElement();
						String value = (String) p_searchCriteria.get(key);
						if (key.startsWith("cm") || key.startsWith("cr") || key.equals("mode")  )
							continue;

						if (value == null || value.trim().equals("") || value.trim().equals("-1")) {
							continue;
						}
						if (key.equals("clientName")) {
							ArrayList<Long> vpnClientIDs = getVpnClientIDsFromName(databaseConnection,value);
							if (vpnClientIDs.size() > 0) {
								 
									String columnNameOfClientID = getColumnName(ComplainHistoryDTO.class, "accountID");
									if (j == 0){
										sql+=" where ";
									}
									else {
										sql+= " and ";
									}
									sql +=  columnNameOfClientID + " in (";
									int size = vpnClientIDs.size();
									for (int index=0;index<size-1;index++) {
										
										sql+=vpnClientIDs.get(index)+",";
									}
									sql+= vpnClientIDs.get(size-1);
									
									sql+=") ";
								
								 j++;
							}
							else 
							{
								return result;
							}
							
							continue;
						}
						if(key.equals("moduleID")){
							continue;
						}

						if (j == 0)
							sql += " where " + key + " like '%" + value + "%'";
						else
							sql += " and " + key + " like '%" + value + "%'";
						j++;

					}
					if(!loginDTO.getIsAdmin()){
						if(j == 0)
							sql += " Where " + getColumnName(ComplainHistoryDTO.class, "accountID") + " = " + loginDTO.getAccountID(); 
						else
							sql += " and " + getColumnName(ComplainHistoryDTO.class, "accountID") + " = " + loginDTO.getAccountID(); 
					}
				}
			}
			logger.debug("the sql for first search: "+sql);
			
			Statement stmt = databaseConnection.getNewStatement();
			ResultSet rs = stmt.executeQuery(sql);
			String whereClause ="";
			
			whereClause += "(";
		       while(rs.next())
		       {
		    	   if(checkIfLatestPair(rs.getLong("chComplainID"), rs.getLong("chID"), databaseConnection)){
		        whereClause += rs.getLong("chComplainID");
		        if(!rs.isLast())
		        {
		         whereClause += ",";
		        }
		        
		        complainIDMap.put(rs.getLong("chComplainID"), rs.getLong("chComplainID"));
		    	   }
		    	   }
		    	if(whereClause.endsWith(",")) whereClause= whereClause.substring(0, whereClause.length()-1);
		       whereClause += ")";
		       
			if(complainIDMap.keySet().size()==0)return result;

			sql = "SELECT cmID from at_complaint ";

			if (p_searchCriteria != null) {
				@SuppressWarnings("rawtypes")
				Enumeration keys = p_searchCriteria.keys();
				if (p_searchCriteria.size() > 0) {
					int j = 0;
					int k = 0;
					for (int i = 0; i < p_searchCriteria.size(); i++) {
						String key = (String) keys.nextElement();
						String value = (String) p_searchCriteria.get(key);
						if (key.startsWith("ch") || key.startsWith("cl") ){
							continue;
						}
						else if (value == null || value.trim().equals("") || value.trim().equals("-1") || key.equals("mode")) {
							continue;
						}
						else if(key.equals("moduleID")){
							continue;
						}
						else  if(key.equals("creationTimeFrom"))
		                {
		                	  String columnName = getColumnName(ComplainDTO.class, "creationTime");
							               	
							  if(value == null || value.length() == 0)continue;
		                      if(j>0)
		                      {
		                      	sql += " and ";                        	
		                      } 
		                      
		                      else
		                      {
		                    	  sql += " where ";
		                      }
		                     
		                      sql +=  columnName+" >= " + sdf.parse(value).getTime();
		                      
		                      j++;
		                    
		                      continue;
		                 }
		                 else if(key.equals("creationTimeTo"))
		                 {
		                	 String columnName = getColumnName(ComplainDTO.class, "creationTime");
								
		                  	if(value == null || value.length() == 0)continue;
		                      if(j>0)
		                      {
		                      	sql += " and ";
		                      }
		                      else
		                      {
		                    	  sql += " where ";
		                      }
		                  	
		                      sql +=  columnName+" <= "+ (sdf.parse(value).getTime() + 86400000L);  
		                      j++;
		                      continue;
		                 }
						
						logger.debug(key);
						if (j == 0)
							sql += " where " + key + " like '%" + value + "%'";
						else
							sql += " and " + key + " like '%" + value + "%'";
						j++;

					}
					
					if(whereClause.length()>2){
						if(j>0)
						sql+= " and cmID in " + whereClause;
						else{
							sql+= " where cmID in " + whereClause;
						}
					}
					
					if(!loginDTO.getIsAdmin()){
							sql += " and " + getColumnName(ComplainDTO.class, "clientID") + " = " + loginDTO.getAccountID()+" and "+ getColumnName(ComplainDTO.class, "moduleID") + " = " + moduleID; 
					}else{
							sql += " and "+ getColumnName(ComplainDTO.class, "moduleID") + " = " + moduleID;
					}
				}
			}
			logger.debug("sql is : " + sql);
			
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				long id = rs.getLong("cmID");
			
					result.add(id);
		
		
			}

			return result;
			}*/
	
	private boolean checkIfLatestPair(long complainID, long historyID, DatabaseConnection databaseConnection) throws Exception {
		String complainIDColumnName = getColumnName(ComplainHistoryDTO.class,"complainID");
		String timeColumnName =  getColumnName(ComplainHistoryDTO.class,"time");
		String conditionString = " where "+complainIDColumnName + " = " + complainID + " order by " + timeColumnName+" desc";
		ArrayList <ComplainHistoryDTO> historyDTOs =	(ArrayList<ComplainHistoryDTO>) getAllObjectList(ComplainHistoryDTO.class, databaseConnection, conditionString);
		if(historyDTOs.get(0).getID()==historyID)return true;
		return false;
	}

	public ArrayList<Long> getVpnClientIDsFromName(DatabaseConnection databaseConnection, String name) throws Exception {
		ArrayList<Long> VpnClientIDs = new ArrayList<Long>();

		
			String clientNameColumn = getColumnName(ClientDTO.class, "loginName");
			String conditionString = " where " + clientNameColumn + " like '%" + name + "%' ";
			ArrayList<ClientDTO> VpnClients = (ArrayList<ClientDTO>) getAllObjectList(ClientDTO.class,
					databaseConnection, conditionString);
			for (ClientDTO dto : VpnClients) {
				VpnClientIDs.add(dto.getClientID());
			}
		

		return VpnClientIDs;
	}

	private List<CrmComplainDTO> getComplainDTOListByEmployeeID(long employeeID) throws Exception{
		// implement
		return null;
	}

}