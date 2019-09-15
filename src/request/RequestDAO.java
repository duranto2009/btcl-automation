package request;

import common.CommonDAO;
import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import util.DatabaseConnectionFactory;

import java.sql.ResultSet;
import java.sql.Statement;

import static util.SqlGenerator.*;
 
public class RequestDAO {
	
	Logger logger = Logger.getLogger(RequestDAO.class);
	
	public Long getRootRequestIDByEntityIDAndEntityTypeID(long entityID,int entityTypeID) throws Exception{
		String sql = "select arID from at_req where arIsDeleted = 0 and arEntityTypeID = "+entityTypeID
				+" and arEntityID = "+entityID+" and arRootRequestID is null";
		ResultSet rs =DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement().executeQuery(sql);
		return rs.next()?rs.getLong(1):null;
	}

	public void addRequest(CommonRequestDTO commonRequestDTO, String IP,  DatabaseConnection databaseConnection) throws Exception
	{
		addRequest(commonRequestDTO, IP, CommonRequestDTO.class, databaseConnection);
	}
	
	public void addRequest(CommonRequestDTO commonRequestDTO, String IP, Class classObject, DatabaseConnection databaseConnection) throws Exception{
		long expireTime = 0;
		if(commonRequestDTO.getExpireTime() == 0)
		{
			expireTime = new CommonDAO().getExpireTimeByRequestType(commonRequestDTO.getRequestTypeID());
			commonRequestDTO.setExpireTime(expireTime);
		}
		commonRequestDTO.setIP(IP);
		insert(commonRequestDTO, commonRequestDTO.getClass(), databaseConnection, true);
	}
	
	public void addVpnNewConnectionRequest(CommonRequestDTO vpnNewConnectionRequestDTO,
			DatabaseConnection databaseConnection) throws Exception{
		insert(vpnNewConnectionRequestDTO,vpnNewConnectionRequestDTO.getClass(),databaseConnection, false);
	}
	
	public void addLliNewConnectionRequest(CommonRequestDTO lliNewConnectionRequestDTO,
			DatabaseConnection databaseConnection) throws Exception{
		insert(lliNewConnectionRequestDTO,lliNewConnectionRequestDTO.getClass(),databaseConnection, true);
	}
	
	public void addVpnLinkRequest(CommonRequestDTO vpnLinkReqDTO, DatabaseConnection databaseConnection) throws Exception {
		insert(vpnLinkReqDTO, vpnLinkReqDTO.getClass(), databaseConnection, false);		
	}

	public void addLliLinkRequest(CommonRequestDTO vpnLinkReqDTO, DatabaseConnection databaseConnection) throws Exception {
		insert(vpnLinkReqDTO, vpnLinkReqDTO.getClass(), databaseConnection, true);		
	}
	
	public void addColocationRequest(CoLocationRequest coLocationRequest,DatabaseConnection databaseConnection) throws Exception{
		insert(coLocationRequest,coLocationRequest.getClass(),databaseConnection, true);
	}
//	public void closeVpnNewLinkRequest(VpnLinkCloseRequestDTO vpnLinkCloseRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnLinkCloseRequestDTO, vpnLinkCloseRequestDTO.getClass(), databaseConnection, true);
//	}
//	public void changeVpnAddressRequest(VpnAddressChangeRequestDTO vpnAddressChangeRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnAddressChangeRequestDTO,vpnAddressChangeRequestDTO.getClass(),databaseConnection, true);
//	}
//
//	public void changeBandwidthVpnLinkRequest(VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnBandWidthChangeRequestDTO, vpnBandWidthChangeRequestDTO.getClass(), databaseConnection, true);
//	}
	
//	public void changeBandwidthLliLinkRequest(LliBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnBandWidthChangeRequestDTO, vpnBandWidthChangeRequestDTO.getClass(), databaseConnection, true);
//	}
	
//	public void changeBandwidthLliLinkRequest(VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnBandWidthChangeRequestDTO, vpnBandWidthChangeRequestDTO.getClass(), databaseConnection, true);
//	}
//
//	public void closeVpnConnectionRequest(VpnConnectionCloseRequestDTO vpnConnectionCloseRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnConnectionCloseRequestDTO, vpnConnectionCloseRequestDTO.getClass(), databaseConnection, true);
//	}
//	public void changeVpnConnectionOwner(VpnConnectionOwnerChangeRequestDTO vpnConnectionOwnerChangeRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(vpnConnectionOwnerChangeRequestDTO, vpnConnectionOwnerChangeRequestDTO.getClass(), databaseConnection, false);
//	}
//	public void addCoLocationSpaceAllocationRequest(CoLocationSpaceAllocationRequest coLocationSpaceAllocationRequest,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(coLocationSpaceAllocationRequest, coLocationSpaceAllocationRequest.getClass(), databaseConnection, true);
//	}
//	public void addCoLocationRackAllocationRequest(ColocationRackAllocationRequestDTO colocationRackAllocationRequestDTO,
//			DatabaseConnection databaseConnection) throws Exception{
//		insert(colocationRackAllocationRequestDTO, colocationRackAllocationRequestDTO.getClass(), databaseConnection, true);
//	}
	public static void main(String[] args) {
	
    }
	public void addEndPointRequest(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection) throws Exception {
		insert(commonRequestDTO, commonRequestDTO.getClass(), databaseConnection, true);
	}
	
	public void addRequestStatus(CommonRequestStatusDTO commonRequestStatusDTO, DatabaseConnection databaseConnection) throws Exception{
		insert(commonRequestStatusDTO, commonRequestStatusDTO.getClass(), databaseConnection, true);
	}
	public void addVpnNewConnectionRequestApprove(CommonRequestDTO commonRequestDTO, DatabaseConnection databaseConnection) throws Exception {
		insert(commonRequestDTO, commonRequestDTO.getClass(), databaseConnection, true);
		
	}
	public void updateVpnLinkRequest(CommonRequestDTO vpnLinkReqDTO, DatabaseConnection databaseConnection) throws Exception{
		updateEntity(vpnLinkReqDTO, CommonRequestDTO.class, databaseConnection, true,false);//(commonRequestDTO, commonRequestDTO.getClass(), databaseConnection, true);

		
	}
	public void updateRequestStatus(CommonRequestStatusDTO commonRequestStatusDTO,
			DatabaseConnection databaseConnection)   throws Exception {
		updateEntity(commonRequestStatusDTO,CommonRequestStatusDTO.class , databaseConnection, true,false);
		
	}
	
	public void updateRequestStatus(CommonRequestStatusDTO commonRequestStatusDTO,
			DatabaseConnection databaseConnection, boolean isPersonal)   throws Exception {
		if(isPersonal){
			updateEntity(commonRequestStatusDTO,CommonRequestStatusDTO.class , databaseConnection, true,false);
		}
		else{
			String sql = "UPDATE at_req_state SET " + getColumnName(CommonRequestStatusDTO.class, "requestTypeID") + " = " + commonRequestStatusDTO.getRequestTypeID()+ 
					" , " + getColumnName(CommonRequestStatusDTO.class, "state") + " = " + commonRequestStatusDTO.getState() + 
					" , " + getColumnName(CommonRequestStatusDTO.class, "time") + " = " + commonRequestStatusDTO.getTime()  + 
					" WHERE "+  getColumnName(CommonRequestStatusDTO.class, "requestID") + " = " + commonRequestStatusDTO.getRequestID() +
					" AND " + getColumnName(CommonRequestStatusDTO.class, "entityTypeID") + " = " + commonRequestStatusDTO.getEntityTypeID() + 
					" AND " + getColumnName(CommonRequestStatusDTO.class, "entityID") + " = " + commonRequestStatusDTO.getEntityID();
			logger.debug(" Update at_req_state " + sql);
			Statement stmt = databaseConnection.getNewStatement();
			stmt.executeUpdate(sql);
					
		}
		
	}
	/*
	public CommonRequestDTO getLatestCommonRequestDTOByEntityIDAndEntityTypeIDAndRequestTypeID(long entityID
			,int entityTypeID,int requestTypeID) throws Exception{
		return
	}*/
	
}
