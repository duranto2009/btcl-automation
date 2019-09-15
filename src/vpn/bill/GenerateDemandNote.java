package vpn.bill;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenerateDemandNote extends DispatchAction {

//	public ActionForward upgradeLink( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
//
//		String clientID = request.getParameter( "clientID" );
//		String vpnLinkID = request.getParameter( "vpnLinkID" );
//		long vpnLinkId = -1;
//		long clientId = -1;
//
//
//		try{
//
//			clientId = Long.parseLong( clientID );
//			vpnLinkId = Long.parseLong( vpnLinkID );
//		}
//		catch( Exception e ){
//
//			throw new RequestFailureException( "Malformed Vpn link id or client id is given" );
//		}
//
//		VpnLinkService vpnLinkService = new VpnLinkService();
//
//		vpnLinkId = Long.parseLong( vpnLinkID );
//
//		VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID(Long.parseLong( vpnLinkID ));
//
//		if( vpnLinkDTO == null )
//			throw new RequestFailureException( "No Vpn Link found with given vpn link id" );
//
//		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( Long.parseLong( clientID ) );
//
//		if( clientDTO == null )
//			throw new RequestFailureException( "No Client found with given client id" );
//
//		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientDTO.getClientID(), ModuleConstants.Module_ID_VPN );
//
//		CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
//		commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK);
//		commonRequestDTO.setEntityID(vpnLinkId);
//		CommonRequestDTO bottom = new RequestUtilService().getBottomRequestDTOsByEntity(commonRequestDTO).iterator().next();
//
//		VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO = vpnLinkService.getVpnBandwidthChangeRequestByRootRequestID( bottom.getRootReqID() );
//
//		double layer3Multiplier = 1.0;
//
//		CommonChargeDTO commonCharge = new VpnCommonChargeConfigService().getCurrentActiveCommonChargeDTO();
//
//		if( vpnLinkDTO.getLayerType() == EndPointConstants.LAYER_TYPE_3 ) {
//			layer3Multiplier = 1 + commonCharge.getRatioL3L2() / 100.0;
//		}
//
//
//		int localLoopDistance = 0;
//		double localLoopCharge = 0;
//		int remoteLoopDistance = 0;
//		double remoteLoopCharge = 0;
//		VpnNearEndDTO nearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
//		VpnFarEndDTO farEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
//
//
//		localLoopCharge = vpnLinkService.getOFCCost(vpnLinkDTO, nearEndDTO, commonCharge);
//		remoteLoopCharge = vpnLinkService.getOFCCost(vpnLinkDTO, farEndDTO, commonCharge);
//
//		int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID( ModuleConstants.Module_ID_VPN, vpnLinkDTO.getClientID() );
//		double distanceBetweenDistricts = vpnLinkDTO.getPopToPopDistance();
//
//		if( distanceBetweenDistricts <= 0 ) {
//			distanceBetweenDistricts = new DistanceService().getDistanceBetweenTwoLocation( nearEndDTO.getUnionID(), farEndDTO.getUnionID() );
//		}
//
//		double bandwidth = vpnBandWidthChangeRequestDTO.getNewBandwidth();
//		int bandwidthType = vpnBandWidthChangeRequestDTO.getNewBandwidthType();
//
//		if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
//			bandwidth *= 1024; //Converting to MB
//
//		TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, categoryID);
//		double newBandwidthCharge = tableDTO.getCostByRowValueAndColumnValue(bandwidth, distanceBetweenDistricts ) * layer3Multiplier;
//
//		//double newBandwidthCharge = new CostConfigService().getCostLLI( bandwidth, year, lliLinkDTO.getClientID() );
//
//		double oldBandwidth = vpnLinkDTO.getVpnBandwidth();
//		int oldBandwidthType = vpnLinkDTO.getVpnBandwidthType();
//
//		if( oldBandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
//			oldBandwidth *= 1024; //Converting to MB
//
//		//double oldBandwidthCharge = new CostConfigService().getCostLLI( oldBandwidth, year, lliLinkDTO.getClientID() );
//
//		tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, categoryID );
//		double oldBandwidthCharge = tableDTO.getCostByRowValueAndColumnValue( oldBandwidth, distanceBetweenDistricts ) * layer3Multiplier;
//
//		double updatedBandwidthCharge = newBandwidthCharge - oldBandwidthCharge;
//
//		request.setAttribute( "nearEndLoopDistance", localLoopDistance );
//		request.setAttribute( "farEndLoopDistance", remoteLoopDistance );
//
//		request.setAttribute( "nearEndLoopCharge", localLoopCharge );
//		request.setAttribute( "farEndLoopCharge", remoteLoopCharge );
//
//		request.setAttribute( "clientDTO", clientDTO );
//		request.setAttribute( "vpnLinkID", vpnLinkId );
//
//		request.setAttribute( "clientID", clientId );
//		request.setAttribute( "vpnLinkDTO", vpnLinkDTO );
//
//		request.setAttribute("nearEndCoreType", nearEndDTO.getCoreType());
//		request.setAttribute("farEndCoreType", farEndDTO.getCoreType());
//
//		request.setAttribute( "bandwidthUpgradationCharge", commonCharge.getUpgradationCharge() );
//
//		request.setAttribute( "bandwidthCharge", updatedBandwidthCharge );
//		request.setAttribute( "prevBWCharge", oldBandwidthCharge);
//		request.setAttribute( "newBWCharge", newBandwidthCharge);
//
//		double securityCharge = updatedBandwidthCharge;
//
//		if( clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON ){
//			securityCharge = 0.0;
//		}
//
//		request.setAttribute( "securityCharge", securityCharge );
//
//		return new ActionForward( "/vpn/link/generateDemandNoteBandwidthChange.jsp" );
//		//return mapping.findForward( upgradeLinkDemandNotePage );
//	}
	/*
	public ActionForward upgradeLinkLLI( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		String clientID = request.getParameter( "clientID" );
		String lliLinkID = request.getParameter( "lliLinkID" );
		long lliLinkId = -1;
		long clientId = -1;
		
		try{
			
			clientId = Long.parseLong( clientID );
			lliLinkId = Long.parseLong( lliLinkID );
		}
		catch( Exception e ){
			
			throw new RequestFailureException( "Malformed Lli link id or client id is given" );
		}
		
		LliLinkService lliLinkService = new LliLinkService();
		
		lliLinkId = Long.parseLong( lliLinkID );
			
		LliBandWidthChangeRequestDTO lliBandWidthChangeRequestDTO = new LliLinkService().getLatestBandwidthChangeRequestByEntityAndEntityTypeID( lliLinkId, EntityTypeConstant.LLI_LINK );
		
		LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID( Long.parseLong( lliLinkID ));

		if( lliLinkDTO == null )
			throw new RequestFailureException( "No Lli Link found with given lli link id" );
		
		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( Long.parseLong( clientID ) );
		
		if( clientDTO == null )
			throw new RequestFailureException( "No Client found with given client id" );
		
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientDTO.getClientID(), ModuleConstants.Module_ID_LLI );
		
		lli.configuration.LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getCurrentActiveLLI_FixedCostConfigurationDTO();
		
		
		int remoteLoopDistance = 0;
		double remoteLoopCharge = 0;
		
		LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID(lliLinkDTO.getFarEndID());
		
		remoteLoopCharge = lliLinkService.getOFCCost(lliLinkDTO, farEndDTO, commonCharge);
		
		int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID( ModuleConstants.Module_ID_LLI, lliLinkDTO.getClientID() );
		int year = EndPointConstants.yearConnectionTypeMapping.get( lliLinkDTO.getConnectionType() );
		
		double bandwidth = lliBandWidthChangeRequestDTO.getNewBandwidth();
		int bandwidthType = lliBandWidthChangeRequestDTO.getNewBandwidthType();
		
		if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
			bandwidth *= 1024; //Converting to MB
		
		TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_LLI, categoryID);
		double newBandwidthCharge = tableDTO.getCostByRowValueAndColumnValue(bandwidth, year);
		
		//double newBandwidthCharge = new CostConfigService().getCostLLI( bandwidth, year, lliLinkDTO.getClientID() );
		
		double oldBandwidth = lliLinkDTO.getLliBandwidth();
		int oldBandwidthType = lliLinkDTO.getLliBandwidthType();
		
		if( oldBandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
			oldBandwidth *= 1024; //Converting to MB
		
		//double oldBandwidthCharge = new CostConfigService().getCostLLI( oldBandwidth, year, lliLinkDTO.getClientID() );
		
		tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_LLI, categoryID);
		double oldBandwidthCharge = tableDTO.getCostByRowValueAndColumnValue( oldBandwidth, year );
		
		double bandwidthCharge = newBandwidthCharge - oldBandwidthCharge;
		
		request.setAttribute( "farEndLoopDistance", remoteLoopDistance );
		
		request.setAttribute( "farEndLoopCharge", remoteLoopCharge );
		
		request.setAttribute( "clientDTO", clientDTO );
		request.setAttribute( "lliLinkID", lliLinkId );
		
		request.setAttribute( "clientID", clientId );
		request.setAttribute( "lliLinkDTO", lliLinkDTO );
		request.setAttribute("farEndCoreType", farEndDTO.getCoreType());
		
		//request.setAttribute( "bandwidthUpgradationCharge", commonCharge.get() );
		
		request.setAttribute( "bandwidthCharge", bandwidthCharge );
		
		double securityCharge = newBandwidthCharge;
		
		if( clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON ){
			securityCharge = 0.0;
		}
		
		request.setAttribute( "securityCharge", securityCharge );
		
		return new ActionForward( "/lli/link/generateDemandNoteBandwidthChange.jsp" );
	}
	*/
	public ActionForward shiftLink( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		String vpnLinkID =  request.getParameter( "vpnLinkID" );
		String clientID = request.getParameter( "clientID" );
		return new ActionForward( "/vpn/link/generateDemandNoteLinkShift.jsp?vpnLinkID" + vpnLinkID + "&clientID" + clientID );
	}
}
