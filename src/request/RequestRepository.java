package request;

import org.apache.log4j.Logger;
import repository.Repository;
 
public class RequestRepository implements Repository{
	Logger logger = Logger.getLogger(getClass());
/*	private static RequestRepository instance = null;
	
	private HashMap<Long,CommonRequestDTO> requestMapVsRequestID;
	private HashMap<Long,VpnAddressChangeRequestDTO> vpnAddressChangeRequestMapVsCommontReqID;
	private HashMap<Long,VpnAddressChangeRequestDTO> vpnAddressChangeRequestMapVsID;
	private HashMap<Long,VpnBandWidthChangeRequestDTO> vpnBandwidthChangeRequestMapVsCommontReqID;
	private HashMap<Long,VpnBandWidthChangeRequestDTO> vpnBandwidthChangeRequestMapVsID;
	private HashMap<Long,VpnConnectionCloseRequestDTO> vpnConnectionCloseRequestMapVsCommontReqID;
	private HashMap<Long,VpnConnectionCloseRequestDTO> vpnConnectionCloseRequestMapVsID;
	private HashMap<Long,VpnConnectionOwnerChangeRequestDTO> vpnConnectionOwnerChangeRequestMapVsCommontReqID;
	private HashMap<Long,VpnConnectionOwnerChangeRequestDTO> vpnConnectionOwnerChangeRequestMapVsID;
	private HashMap<Long,VpnLinkCloseRequestDTO> vpnLinkCloseRequestMapVsCommontReqID;
	private HashMap<Long,VpnLinkCloseRequestDTO> vpnLinkCloseRequestMapVsID;
	private HashMap<Long,VpnLinkConnectionChangeRequestDTO> vpnLinkConnectionChangeRequestMapVsCommontReqID;
	private HashMap<Long,VpnLinkConnectionChangeRequestDTO> vpnLinkConnectionChangeRequestMapVsID;
	private HashMap<Long,VpnNewConnectionRequestDTO> vpnNewConnectionRequestMapVsCommontReqID;
	private HashMap<Long,VpnNewConnectionRequestDTO> vpnNewConnectionRequestMapVsID;
	private HashMap<Long,CoLocationRequest> mapOfColocationToColocationRequestID;
	private HashMap<Long,CoLocationRequest> mapOfColocationToRequestID;
	
	
	private RequestRepository()
	{
		requestMapVsRequestID = new HashMap<Long,CommonRequestDTO>();
		vpnAddressChangeRequestMapVsCommontReqID = new HashMap<Long, VpnAddressChangeRequestDTO>();
		vpnAddressChangeRequestMapVsID = new HashMap<Long, VpnAddressChangeRequestDTO>();
		vpnBandwidthChangeRequestMapVsCommontReqID = new HashMap<Long, VpnBandWidthChangeRequestDTO>();
		vpnBandwidthChangeRequestMapVsID = new HashMap<Long, VpnBandWidthChangeRequestDTO>();
		vpnConnectionCloseRequestMapVsCommontReqID = new HashMap<Long, VpnConnectionCloseRequestDTO>();
		vpnConnectionCloseRequestMapVsID = new HashMap<Long, VpnConnectionCloseRequestDTO>();
		vpnConnectionOwnerChangeRequestMapVsCommontReqID = new HashMap<Long, VpnConnectionOwnerChangeRequestDTO>();
		vpnConnectionOwnerChangeRequestMapVsID = new HashMap<Long, VpnConnectionOwnerChangeRequestDTO>();
		vpnLinkCloseRequestMapVsCommontReqID = new HashMap<Long, VpnLinkCloseRequestDTO>();
		vpnLinkCloseRequestMapVsID = new HashMap<Long, VpnLinkCloseRequestDTO>();
		vpnLinkConnectionChangeRequestMapVsCommontReqID = new HashMap<Long, VpnLinkConnectionChangeRequestDTO>();
		vpnLinkConnectionChangeRequestMapVsID = new HashMap<Long, VpnLinkConnectionChangeRequestDTO>();
		vpnNewConnectionRequestMapVsCommontReqID = new HashMap<Long, VpnNewConnectionRequestDTO>();
		vpnNewConnectionRequestMapVsID = new HashMap<Long, VpnNewConnectionRequestDTO>();
		mapOfColocationToColocationRequestID = new HashMap<Long, CoLocationRequest>();
		mapOfColocationToRequestID = new HashMap<Long, CoLocationRequest>();
		RepositoryManager.getInstance().addRepository(this);
	}
	
	public CoLocationRequest getCoLocationRequestByRequestID(long requestID){
		return mapOfColocationToRequestID.get(requestID);
	}
	
	public CoLocationRequest getColocationRequestByColocationRequestID(long colocationRequestID){
		return mapOfColocationToColocationRequestID.get(colocationRequestID);
	}
	
	public CoLocationRequest getColocationRequestByRequestID(long requestID){
		return mapOfColocationToRequestID.get(requestID);
	}
	
	public VpnNewConnectionRequestDTO getVpnNewConnectionRequestDTOByID(long ID){
		return vpnNewConnectionRequestMapVsID.get(ID);
	}
	
	public VpnNewConnectionRequestDTO getVpnNewConnectionRequestDTOByCommonRequestID(long commonRequestID){
		return vpnNewConnectionRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public VpnLinkConnectionChangeRequestDTO getVpnLinkConnectionChangeRequestDTOByID(long ID){
		return vpnLinkConnectionChangeRequestMapVsID.get(ID);
	}
	
	public VpnLinkConnectionChangeRequestDTO getVpnLinkConnectionChangeRequestDTOByCommonRequestID(long commonRequestID){
		return vpnLinkConnectionChangeRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public VpnLinkCloseRequestDTO getCloseRequestDTOByID(long ID){
		return vpnLinkCloseRequestMapVsID.get(ID);
	}
	
	public VpnLinkCloseRequestDTO getVpnLinkCloseRequestDTOByCommonRequestID(long commonRequestID){
		return vpnLinkCloseRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	
	public VpnConnectionOwnerChangeRequestDTO getVpnConnectionOwnerChangeRequestDTOByID(long ID){
		return vpnConnectionOwnerChangeRequestMapVsID.get(ID);
	}
	
	public VpnConnectionOwnerChangeRequestDTO getVpnConnectionOwnerChangeRequestDTOByCommonRequestID(long commonRequestID){
		return vpnConnectionOwnerChangeRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public VpnConnectionCloseRequestDTO getVpnConnectionCloseRequestDTOByID(long ID){
		return vpnConnectionCloseRequestMapVsID.get(ID);
	}
	
	public VpnConnectionCloseRequestDTO getVpnConnectionCloseRequestDTOByCommonRequestID(long commonRequestID){
		return vpnConnectionCloseRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public VpnBandWidthChangeRequestDTO getVpnBandWidthChangeRequestDTOByID(long ID){
		return vpnBandwidthChangeRequestMapVsID.get(ID);
	}
	
	public VpnBandWidthChangeRequestDTO getVpnBandWidthChangeRequestDTOByCommonRequestID(long commonRequestID){
		return vpnBandwidthChangeRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public VpnAddressChangeRequestDTO getVpnAddressChangeRequestDTOByID(long ID){
		return vpnAddressChangeRequestMapVsID.get(ID);
	}
	
	public VpnAddressChangeRequestDTO getVpnAddressChangeRequestDTOByCommonRequestID(long commonRequestID){
		return vpnAddressChangeRequestMapVsCommontReqID.get(commonRequestID);
	}
	
	public CommonRequestDTO getCommonRequestDTOByCommonRequestID(long commmonRequestID){
		logger.debug("requestMapVsRequestID " + requestMapVsRequestID);
		return requestMapVsRequestID.get(commmonRequestID);
	}
	
	private void reloadAddressChangeRequestReload(boolean firstReload,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = (firstReload?  " ":" where "+getLastModificationTimeColumnName(VpnAddressChangeRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnAddressChangeRequestDTO> vpnAddressChangeRequestDTOs = (List<VpnAddressChangeRequestDTO>)getAllObjectList(VpnAddressChangeRequestDTO.class, databaseConnection, conditionString);
			
		for(VpnAddressChangeRequestDTO vpnAddressChangeRequestDTO: vpnAddressChangeRequestDTOs){	
			vpnAddressChangeRequestMapVsCommontReqID.put(vpnAddressChangeRequestDTO.getReqID(), vpnAddressChangeRequestDTO);
			vpnAddressChangeRequestMapVsID.put(vpnAddressChangeRequestDTO.getID(), vpnAddressChangeRequestDTO);
		}
		logger.debug(vpnAddressChangeRequestMapVsCommontReqID);
		logger.debug(vpnAddressChangeRequestMapVsID);
	}
	
	private void reloadVpnBandwidthChangeRequest(boolean firstReload,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = (firstReload?"":" where "+ getLastModificationTimeColumnName(VpnBandWidthChangeRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnBandWidthChangeRequestDTO> vpnBandWidthChangeRequestDTOs = (List<VpnBandWidthChangeRequestDTO>)getAllObjectList(VpnBandWidthChangeRequestDTO.class, databaseConnection, conditionString);

		for(VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO: vpnBandWidthChangeRequestDTOs){	 
			 vpnBandwidthChangeRequestMapVsCommontReqID.put(vpnBandWidthChangeRequestDTO.getReqID(), vpnBandWidthChangeRequestDTO);
			vpnBandwidthChangeRequestMapVsID.put(vpnBandWidthChangeRequestDTO.getID(), vpnBandWidthChangeRequestDTO);
		}

		logger.debug(vpnBandwidthChangeRequestMapVsCommontReqID);
		logger.debug(vpnBandwidthChangeRequestMapVsID);
	}
	
	private void reloadVpnConnectionCloseRequestReload(boolean firstReload, DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString = (firstReload?"": " where "+getLastModificationTimeColumnName(VpnConnectionCloseRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnConnectionCloseRequestDTO> vpnConnectionCloseRequestDTOs = (List<VpnConnectionCloseRequestDTO>)getAllObjectList(VpnConnectionCloseRequestDTO.class, databaseConnection, conditionString);
		
		for(VpnConnectionCloseRequestDTO vpnConnectionCloseRequestDTO: vpnConnectionCloseRequestDTOs){
			vpnConnectionCloseRequestMapVsCommontReqID.put(vpnConnectionCloseRequestDTO.getReqID(), vpnConnectionCloseRequestDTO);
			vpnConnectionCloseRequestMapVsID.put(vpnConnectionCloseRequestDTO.getID(), vpnConnectionCloseRequestDTO);
		}
		logger.debug(vpnConnectionCloseRequestMapVsCommontReqID);
		logger.debug(vpnConnectionCloseRequestMapVsID);
	}
	
	private void reloadVpnConnectionOwnerChangeRequest(boolean firstReload,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = (firstReload?"":" where "+ getLastModificationTimeColumnName(VpnConnectionOwnerChangeRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnConnectionOwnerChangeRequestDTO> vpnConnectionOwnerChangeRequestDTOs = (List<VpnConnectionOwnerChangeRequestDTO>)getAllObjectList(VpnConnectionOwnerChangeRequestDTO.class, databaseConnection, conditionString);

		for(VpnConnectionOwnerChangeRequestDTO vpnConnectionOwnerChangeRequestDTO: vpnConnectionOwnerChangeRequestDTOs){	 
			vpnConnectionOwnerChangeRequestMapVsCommontReqID.put(vpnConnectionOwnerChangeRequestDTO.getReqID(), vpnConnectionOwnerChangeRequestDTO);
			vpnConnectionOwnerChangeRequestMapVsID.put(vpnConnectionOwnerChangeRequestDTO.getID(), vpnConnectionOwnerChangeRequestDTO);
	
		}
		

		logger.debug(vpnConnectionOwnerChangeRequestMapVsCommontReqID);
		logger.debug(vpnConnectionOwnerChangeRequestMapVsID);
	}
	
	private void reloadVpnLinkCloseRequest(boolean firstReload, DatabaseConnection databaseConnection) throws Exception{
		String sql = (firstReload? "":" where "+getLastModificationTimeColumnName(VpnLinkCloseRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		ResultSet rs = getAllResultSetOfTable(VpnLinkCloseRequestDTO.class, databaseConnection, sql);
		
		String conditionString = (firstReload? "":" where "+getLastModificationTimeColumnName(VpnLinkCloseRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnLinkCloseRequestDTO> vpnLinkCloseRequestDTOs = (List<VpnLinkCloseRequestDTO>)getAllObjectList(VpnLinkCloseRequestDTO.class, databaseConnection,conditionString); 
		for(VpnLinkCloseRequestDTO vpnLinkCloseRequestDTO: vpnLinkCloseRequestDTOs){
			vpnLinkCloseRequestMapVsCommontReqID.put(vpnLinkCloseRequestDTO.getReqID(), vpnLinkCloseRequestDTO);
			vpnLinkCloseRequestMapVsID.put(vpnLinkCloseRequestDTO.getID(), vpnLinkCloseRequestDTO);
		}
		

		logger.debug(vpnLinkCloseRequestMapVsCommontReqID);
		logger.debug(vpnLinkCloseRequestMapVsID);
	}
	
	private void vpnLinkOwnerChangeRequestReload(boolean firstReload, DatabaseConnectin databaseConnection,RepositoryManager repositoryManager) throws Exception{
		
		String conditionString = (firstReload?"":" where "+ getLastModificationTimeColumnName(VpnLinkConnectionChangeRequestDTO.class)+">="+repositoryManager.lastModifyTime);
		List<VpnLinkOwern>
		
		while(rs.next()){
			VpnLinkConnectionChangeRequestDTO vpnLinkOwnerChangeRequestDTO = new VpnLinkConnectionChangeRequestDTO();
			vpnLinkConnectionChangeRequestMapVsCommontReqID.put(vpnLinkOwnerChangeRequestDTO.getRequestID(), vpnLinkOwnerChangeRequestDTO);
			vpnLinkConnectionChangeRequestMapVsID.put(vpnLinkOwnerChangeRequestDTO.getID(), vpnLinkOwnerChangeRequestDTO);
		}
	}
	
	private void reloadVpnNewConnectionRequest(boolean firstReload, DatabaseConnection databaseConnection) throws Exception{
		String conditionString = (firstReload?"": " where "+getLastModificationTimeColumnName(VpnNewConnectionRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		List<VpnNewConnectionRequestDTO> vpnNewConnectionRequestDTOs = (List<VpnNewConnectionRequestDTO>)getAllObjectList(VpnNewConnectionRequestDTO.class, databaseConnection, conditionString);
		
		for(VpnNewConnectionRequestDTO vpnNewConnectionRequestDTO: vpnNewConnectionRequestDTOs){
			vpnNewConnectionRequestMapVsCommontReqID.put(vpnNewConnectionRequestDTO.getReqID(), vpnNewConnectionRequestDTO);
			vpnNewConnectionRequestMapVsID.put(vpnNewConnectionRequestDTO.getID(), vpnNewConnectionRequestDTO);
		}
		

		logger.debug(vpnNewConnectionRequestMapVsCommontReqID);
		logger.debug(vpnNewConnectionRequestMapVsID);
	}
	private void reloadCommonRequest(boolean firstReload, DatabaseConnection databaseConnection) throws Exception{
		
		String conditionString = (firstReload? "":" where "+getLastModificationTimeColumnName(CommonRequestDTO.class)+">="+RepositoryManager.lastModifyTime);
		ResultSet rs = getAllResultSetOfTable(CommonRequestDTO.class, databaseConnection, conditionString);
		
		while(rs.next()){
			CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
			populateObjectFromDB(commonRequestDTO, rs, CommonRequestDTO.class);
			requestMapVsRequestID.put(commonRequestDTO.getReqID(), commonRequestDTO);
			long commonRequestID = commonRequestDTO.getReqID();
			
			if(vpnAddressChangeRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnAddressChangeRequestDTO vpnAddressChangeRequestDTO = vpnAddressChangeRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnAddressChangeRequestDTO, rs, CommonRequestDTO.class);
			}
			
			if(vpnBandwidthChangeRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO = vpnBandwidthChangeRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnBandWidthChangeRequestDTO, rs, CommonRequestDTO.class);
			}
			
			
			if(vpnConnectionCloseRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnConnectionCloseRequestDTO vpnConnectionCloseRequestDTO = vpnConnectionCloseRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnConnectionCloseRequestDTO, rs, CommonRequestDTO.class);
			}

			if(vpnConnectionOwnerChangeRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnConnectionOwnerChangeRequestDTO vpnConnectionOwnerChangeRequestDTO = vpnConnectionOwnerChangeRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnConnectionOwnerChangeRequestDTO, rs, CommonRequestDTO.class);
			}
			
			if(vpnLinkCloseRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnLinkCloseRequestDTO vpnLinkCloseRequestDTO = vpnLinkCloseRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnLinkCloseRequestDTO, rs, CommonRequestDTO.class);
			}
			
			if(vpnLinkConnectionChangeRequestMapVsCommontReqID.containsKey(commonRequestID)){
				VpnLinkConnectionChangeRequestDTO vpnLinkConnectionChangeRequestDTO = vpnLinkConnectionChangeRequestMapVsCommontReqID.get(commonRequestID);
				populateObjectFromDB(vpnLinkConnectionChangeRequestDTO, rs, CommonRequestDTO.class);
			}
			
		}
		logger.debug("requestMapVsRequestID "+requestMapVsRequestID);
	}
	
	public static synchronized RequestRepository getInstance(){
		if(instance == null){
			instance = new RequestRepository();
		}

		return instance;
	}
	
	
	
	public void reload(boolean firstReload){
		try{
			DatabaseConnection databaseConnection = new DatabaseConnection();
			databaseConnection.dbOpen();
			reloadVpnNewConnectionRequest(firstReload, databaseConnection);
			//reloadAddressChangeRequestReload(firstReload, databaseConnection);
			//reloadVpnConnectionCloseRequestReload(firstReload, databaseConnection);
			//reloadVpnConnectionOwnerChangeRequest(firstReload, databaseConnection);
			//reloadVpnLinkCloseRequest(firstReload, databaseConnection);
			reloadCommonRequest(firstReload, databaseConnection);
			databaseConnection.dbClose();
		}catch(Exception ex){
			logger.debug("FATAL", ex);
		}
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

*/

	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
