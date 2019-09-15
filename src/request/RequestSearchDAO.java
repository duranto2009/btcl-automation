package request;

import static common.StringUtils.isBlank;
import static util.SqlGenerator.getAllIDList;
import static util.SqlGenerator.getColumnName;
import static util.SqlGenerator.getIDListFromSearchCriteria;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import common.ClientDTO;
import common.CommonDAO;
import common.EntityDTO;
import common.EntityTypeConstant;
import common.HierarchicalEntityService;
import common.ModuleConstants;
import common.StringUtils;
import common.constants.RequestTypeConstants;
import connection.DatabaseConnection;
import login.ColumnPermissionConstants;
import login.LoginDTO;
import permission.PermissionRepository;
import util.DateUtils;
import util.SQLInjectionEscaper;
import util.SqlGenerator;
 
@SuppressWarnings({"rawtypes", "unchecked"})
public class RequestSearchDAO {
	Logger logger = Logger.getLogger(getClass());
	
	
	public Collection getIDs(LoginDTO loginDTO,int moduleID, DatabaseConnection databaseConnection) throws Exception {
    	Collection data = new ArrayList();
    	String sql="SELECT arID, arRequestTypeID from at_req where floor(("+ getColumnName(CommonRequestDTO.class,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID + "  AND "
    	+getColumnName(CommonRequestDTO.class,"requestTypeID") + " NOT IN (60001,70001)";
    			
    	if(loginDTO.getAccountID() > 0)
    	{
    		sql += " and arClientID = " + loginDTO.getAccountID() ;    
    	}
    	else
    	{
    		sql += checkRequestToFromRestriction(loginDTO, moduleID);
    	}
    	Statement stmt =databaseConnection.getNewStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	while(rs.next())
    	{
    		data.add(rs.getString("arID"));	
    	}
    	return data;
	}
	
	public Collection getIDsWithSearchCriteria(int moduleID,Hashtable<String, String> criteriaMap, LoginDTO loginDTO, DatabaseConnection databaseConnection)  throws Exception {
		Class classObject = CommonRequestDTO.class;	  
		String clientLoginName = criteriaMap.get("arClientId");
		List<Long> clientIDList = new ArrayList<Long>();
	
		
		String toDateInputName = "arReqTimeTo";
		if(!StringUtils.isBlank(criteriaMap.get(toDateInputName))){
			criteriaMap.put(toDateInputName, ""+DateUtils.getEndTimeOfDayByDateString(criteriaMap.get(toDateInputName)));
		}
		
		
		if(!isBlank(clientLoginName)){
			Class firstClassObject = ClientDTO.class;
			String firstConditionString = " where "+getColumnName(firstClassObject, "loginName")+" like "+"'%"+SQLInjectionEscaper.escapeString(clientLoginName, true)+"%'";
			clientIDList = getAllIDList(firstClassObject, databaseConnection, firstConditionString);
			if(clientIDList.isEmpty()){
				clientIDList.add(new Long(-1));
			}
		}
		
		if(criteriaMap.get("arRequestTypeID") == null || criteriaMap.get("arRequestTypeID").equals("-1")){
			criteriaMap.put("arRequestTypeID", "");
		}
	
		//Default for Admin
		String[] keys = new String[]         {"arRequestedByAccountID", 	"arRequestedToAccountID",	"arRequestTypeID",	"arCompletionStatus",	"arReqTimeFrom",	"arReqTimeTo",	"arPriority",	"entityID"};
		String[] operators = new String[]    {		"=",					"=",						"=",	 	      	"="     	 			,">="          ,	"<="            ,"="   		,	"="};
		String[] dtoColumnNames = new String[]{"requestByAccountID",		"requestToAccountID",		"requestTypeID",	"completionStatus",  	"requestTime"  ,	"requestTime",	"priority",		"entityID"};
		String fixedCondition  = " floor(("+ getColumnName(CommonRequestDTO.class,"entityTypeID")+"/ "+EntityTypeConstant.MULTIPLIER2+") + 0.001) = "+moduleID;// + " AND "+getColumnName(CommonRequestDTO.class,"requestTypeID") + " NOT IN (60001,70001)";;
		
	
		if(!loginDTO.getIsAdmin()){
			String clientSpecificCond="";
			keys = new String[]         {"arRequestTypeID","arCompletionStatus","arReqTimeFrom","arReqTimeTo","arPriority",	"entityID"};
			operators = new String[]    { "=",	 	      "="     	 			,">="          ,"<="            ,"="   		,"="   };
			dtoColumnNames = new String[]{"requestTypeID",       "completionStatus",  "requestTime"  ,"requestTime"  ,"priority",	"entityID"};
			
			criteriaMap.remove("arClientId");
			criteriaMap.put("clientID", ""+loginDTO.getAccountID());
			
			if(criteriaMap.get("arRequestedByAccountID") == null){	
			}else if(criteriaMap.get("arRequestedByAccountID").equals(""+loginDTO.getAccountID())){
				clientSpecificCond+= " and ( arRequestedByAccountID = "+loginDTO.getAccountID()+" ) ";
			}else if (criteriaMap.get("arRequestedByAccountID").equals("-1")){
				clientSpecificCond+=" and ( "+ getColumnName(classObject, "requestByAccountID")+ " is  NULL or "+ getColumnName(classObject, "requestByAccountID")+"  <= 0 ) ";
			}else{
				criteriaMap.remove("arRequestedByAccountID");
			}
			
			if(criteriaMap.get("arRequestedToAccountID") == null){	
			}else if(criteriaMap.get("arRequestedToAccountID").equals(""+loginDTO.getAccountID())){
				clientSpecificCond+= " and ( arRequestedToAccountID = "+loginDTO.getAccountID()+" ) ";
			}else if (criteriaMap.get("arRequestedToAccountID").equals("-1")){
				clientSpecificCond+=" and ( "+ getColumnName(classObject, "requestToAccountID")+ " is  NULL or "+ getColumnName(classObject, "requestToAccountID")+"  < 0 ) ";
			}else{
				criteriaMap.remove("arRequestedToAccountID");
			}
			
			fixedCondition = fixedCondition + clientSpecificCond;
		}else {
			//Handle RequestedTo Type and RequestedBy Type
			if(criteriaMap.containsKey("arRequestedByAccountIDType") && criteriaMap.containsKey("arRequestedToAccountIDType")) {
				String requestedByOrToTypeCondition = "";
				if(criteriaMap.get("arRequestedByAccountIDType").equals("1")) {
					requestedByOrToTypeCondition += " and "+ getColumnName(classObject, "requestByAccountID") + " > 0";
				} else if(criteriaMap.get("arRequestedByAccountIDType").equals("2")) {
					requestedByOrToTypeCondition += " and ( "+ getColumnName(classObject, "requestByAccountID") + " < 0 or "+ getColumnName(classObject, "requestByAccountID") + " is  NULL )";
				} else if(criteriaMap.get("arRequestedByAccountIDType").equals("3")) {
					requestedByOrToTypeCondition += " and ( "+ getColumnName(classObject, "requestByAccountID") + " = 0 )";
				} 
				if(criteriaMap.get("arRequestedToAccountIDType").equals("1")) {
					requestedByOrToTypeCondition += " and "+ getColumnName(classObject, "requestToAccountID") + " > 0";
				} else if(criteriaMap.get("arRequestedToAccountIDType").equals("2")) {
					requestedByOrToTypeCondition += " and ( "+ getColumnName(classObject, "requestToAccountID") + " < 0 or "+ getColumnName(classObject, "requestToAccountID") + " is  NULL )";
				} 
				fixedCondition = fixedCondition + requestedByOrToTypeCondition;
			}
			
			//Admin can by default see requests to own and to NULL
			String adminConditionToIncludeNull = "";
			if(criteriaMap.get("arRequestedByAccountID") == null || criteriaMap.get("arRequestedByAccountIDType").equals("3")){		
			}else if(criteriaMap.get("arRequestedByAccountID").equals("-"+loginDTO.getUserID())){
				adminConditionToIncludeNull+=" and ( "+ getColumnName(classObject, "requestByAccountID")+ " is  NULL or "+ getColumnName(classObject, "requestByAccountID")+" = "+criteriaMap.get("arRequestedByAccountID")+" ) ";
				criteriaMap.remove("arRequestedByAccountID");
			}
			
			if(criteriaMap.get("arRequestedToAccountID") == null){	
			}else if(criteriaMap.get("arRequestedToAccountID").equals("-"+loginDTO.getUserID())){
				adminConditionToIncludeNull+=" and ( "+ getColumnName(classObject, "requestToAccountID")+ " is  NULL or "+ getColumnName(classObject, "requestToAccountID")+" = "+criteriaMap.get("arRequestedToAccountID")+" ) ";
				criteriaMap.remove("arRequestedToAccountID");
			}
			fixedCondition = fixedCondition + adminConditionToIncludeNull;
			fixedCondition +=  checkRequestToFromRestriction(loginDTO, moduleID);
		}
		
		criteriaMap.remove("isDeleted");
		criteriaMap.put("isDeleted", "0");
		
		if(!loginDTO.getIsAdmin()){
			fixedCondition += " and  ("+getColumnName(classObject, "clientID")+" = "+loginDTO.getAccountID() + " or " + getColumnName(classObject, "requestToAccountID") + " = " + loginDTO.getAccountID() + " ) ";
		}else{
			if(!isBlank(clientLoginName) ){
				fixedCondition += " and "+getColumnName(classObject, "clientID")+" in  "+StringUtils.getCommaSeparatedString(clientIDList);
			}
		}
		
		fixedCondition += generateHistoryCondition(criteriaMap, databaseConnection);
		
		fixedCondition += filterOutRequestTypeCondition(moduleID);
		
		List<Long> IDList = (List<Long>)getIDListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition, databaseConnection);
		return IDList;
	}

	private String generateHistoryCondition(Hashtable<String, String> criteriaMap, DatabaseConnection databaseConnection) throws Exception {
		String name="", description="", fromDateString="", toDateString="";
		long fromDate=0, toDate=0;
		SimpleDateFormat sdf=new  SimpleDateFormat("dd/MM/yyyy");
		if (criteriaMap.get("arClientID")!=null && criteriaMap.get("arClientID").length()>0){
			name=criteriaMap.get("arClientID");
		}
		if (criteriaMap.get("arDescription")!=null && criteriaMap.get("arDescription").length()>0){
			description=criteriaMap.get("arDescription");
		}
		if (criteriaMap.get("arReqTimeFrom")!=null && criteriaMap.get("arReqTimeFrom").length()>0){
			fromDateString=criteriaMap.get("arReqTimeFrom");
			fromDate = sdf.parse(fromDateString).getTime();
		}
		if (criteriaMap.get("arReqTimeTo")!=null && criteriaMap.get("arReqTimeTo").length()>0){
			toDateString=criteriaMap.get("arReqTimeTo");
			toDate = Long.parseLong(toDateString);
		}
		
		
		String historyCondition="";
		if (!name.equalsIgnoreCase("")) {
			CommonDAO commonDAO = new CommonDAO();
			ArrayList<Long> clientIDs = commonDAO.getClientIDsFromName(databaseConnection, name);
			ArrayList<Long> userIDs = commonDAO.getUserIDsFromName(databaseConnection, name);
			if (clientIDs.size() > 0 || userIDs.size() > 0) {
				historyCondition+= (" and " + getColumnName(CommonRequestDTO.class, "requestByAccountID") + " IN ( ");
				int i = 0;
				for (long userID : userIDs) {
					i++;
					historyCondition+= (" " + userID + " ");
					if (userIDs.size() != i) {
						historyCondition+= (" , ");
					}
				}
				i = 0;
				for (long clientID : clientIDs) {
					i++;
					historyCondition+= (" " + clientID + " ");
					if (clientIDs.size() != i) {
						historyCondition+= (" , ");
					}
				}
				historyCondition+= (" ) ");
			}
		}
		if (!description.equalsIgnoreCase("")) {
			historyCondition+= (" and " + getColumnName(CommonRequestDTO.class, "description") + " like '%" + description + "%' ");
		}
		
		historyCondition += generateEntityIDAndEntityTypeIDCondition(criteriaMap);
		
		if (fromDate > 0 && toDate > 0) {
			historyCondition+= (" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " BETWEEN " + fromDate + " AND " + toDate + 86400000);
		} else if (fromDate > 0 && toDate == 0) {
			historyCondition+= (" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " >= " + fromDate);
		} else if (fromDate == 0 && toDate > 0) {
			historyCondition+= (" and " + getColumnName(CommonRequestDTO.class, "requestTime") + " <= " + (toDate + 86400000));
		}
		return historyCondition;
	}
	private Boolean isCriteriaIncludedInCriteriaMap(String criteria, Hashtable<String, String> criteriaMap) {
		return criteriaMap.get(criteria)!=null && criteriaMap.get(criteria).length()>0;
	}
	private String generateEntityIDAndEntityTypeIDCondition(Hashtable<String, String> criteriaMap) throws Exception {
		String entityIDAndEntityTypeIDCondition = "";
		
		if(isCriteriaIncludedInCriteriaMap("entityID", criteriaMap)) {
			
			List<HashMap<String, String>> entityDetailsList = new HierarchicalEntityService().getChildEntityListByParentEntityIDAndEntityTypeID(Long.parseLong(criteriaMap.get("entityID")), Integer.parseInt(criteriaMap.get("entityTypeID")));
			for(int iterator =0;iterator<entityDetailsList.size();iterator++) {
				entityIDAndEntityTypeIDCondition += iterator == 0 ? " and ( " : " or ";
				entityIDAndEntityTypeIDCondition += " ( " + getColumnName(CommonRequestDTO.class, "entityID") + " = " + entityDetailsList.get(iterator).get("entityID") +
						" and " + getColumnName(CommonRequestDTO.class, "entityTypeID") + " = " + entityDetailsList.get(iterator).get("entityTypeID") + " ) ";
				entityIDAndEntityTypeIDCondition += iterator == entityDetailsList.size()-1 ? " ) " : "";
			}
		}
		criteriaMap.remove("entityID");
		return entityIDAndEntityTypeIDCondition;
	}

	private String filterOutRequestTypeCondition(int moduleID) throws Exception {
		String requestTypeFilteringCondition = "";
		if(RequestTypeConstants.filteredOutRequestTypeIDByModuleID.get(moduleID).size() >0) {
			requestTypeFilteringCondition = " and " + getColumnName(CommonRequestDTO.class, "requestTypeID") + " not in ";
			requestTypeFilteringCondition += StringUtils.getCommaSeparatedString(RequestTypeConstants.filteredOutRequestTypeIDByModuleID.get(moduleID));
		}
		return requestTypeFilteringCondition;
	}
	
	private String checkRequestToFromRestriction(LoginDTO loginDTO, int moduleID) throws Exception {
		String reqeustSearchRestriction = "";
		if(loginDTO.getColumnPermission(moduleID * ModuleConstants.MULTIPLIER + 100))
		{
			
		}
		else
		{
			reqeustSearchRestriction += " and (" + getColumnName(CommonRequestDTO.class, "requestByAccountID") + " = " + -loginDTO.getUserID() + " or " + getColumnName(CommonRequestDTO.class, "requestToAccountID") + " = " + -loginDTO.getUserID() + ")";
		}
		return reqeustSearchRestriction;
	}

	public Collection getDTOs(Collection recordIDs, DatabaseConnection databaseConnection) throws Exception {
    	String sql = "SELECT * from at_req ";
    	if (recordIDs.size() > 0){
    		sql += " where ";
    		for (int i = 0; i < recordIDs.size(); i++){
    			sql += " arID = " + ( (ArrayList) recordIDs).get(i);
    			if (i <= recordIDs.size() - 2){
    				sql += " or ";
    			}
    		}
    	}
    	
    	Collection data = new ArrayList();
    	Statement stmt = databaseConnection.getNewStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	while (rs.next()){
    		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
    		SqlGenerator.populateObjectFromDB(commonRequestDTO, rs);
    		
    		EntityDTO entityDTO = new CommonDAO().getEntityDTOByEntityIDAndEntityTypeID(commonRequestDTO.getEntityTypeID(), commonRequestDTO.getEntityID(), databaseConnection);
    		if(entityDTO != null)commonRequestDTO.setEntityName(entityDTO.getName());
    		logger.debug("commonRequestDTO.getEntityName() " + commonRequestDTO.getEntityName());
    		data.add(commonRequestDTO);
    	}


        return data;
	}

}
