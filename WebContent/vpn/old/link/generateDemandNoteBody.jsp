<%@page import="client.ClientTypeService"%>
<%@page import="distance.DistanceService"%>
<%@page import="costConfig.TableDTO"%>
<%@page import="common.RegistrantTypeConstants"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="vpn.bill.PdfUtil"%>
<%@page import="vpn.bill.BillUtil"%>
<%@page import="common.CommonDAO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="common.RequestFailureException"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.DatabaseConnectionFactory"%>
<%@page import="costConfig.CostConfigService"%>
<%@page import="vpn.link.api.APIUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="vpnCommonChargeConfig.VpnCommonChargeConfigService"%>
<%@page import="vpnCommonChargeConfig.CommonChargeDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="util.SqlGenerator"%>
<%@page import="vpn.link.VpnFRResponseExternalDTO"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="java.util.List"%>
<%@page import="common.ModuleConstants"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="util.SOP"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="java.util.HashMap, java.util.Map"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="login.LoginDTO"%>

<%
	Logger logger = Logger.getLogger(this.getClass());
	VpnLinkService vpnLinkService = new VpnLinkService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	String clientID = request.getParameter( "clientID" );
	String vpnLinkID = request.getParameter( "vpnLinkID" );
	
	VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID( Long.parseLong( vpnLinkID ));
	
	logger.debug( "Vpn Link DTO: " + vpnLinkDTO );
	
	VpnNearEndDTO nearEndDTO = vpnLinkService.getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
	
	logger.debug( "Vpn Near End DTO: " + nearEndDTO );
	
	VpnFarEndDTO farEndDTO = vpnLinkService.getFarEndByFarEndID( vpnLinkDTO.getFarEndID() );
	
	logger.debug( "Vpn Far End DTO: " + farEndDTO );
	
	int newEstablishedEndPoint = 0;
	
	if( !vpnLinkDTO.isNearEndReused() && nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL )
		newEstablishedEndPoint++;
	if( farEndDTO.getParentEndPointID() == 0 && farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL )
		newEstablishedEndPoint++;
	
	int nearEndLoopDistance = 0;
	if (nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
	{
		nearEndLoopDistance = VpnLinkService.getLocalLoopDistance(vpnLinkDTO);
	}
	int farEndLoopDistance = 0;
	if (farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
	{
		farEndLoopDistance = VpnLinkService.getRemoteLoopDistance(vpnLinkDTO);
	}
	
	double layer3Multipler = 1.0;
	
	
	CommonChargeDTO commonCharge = new VpnCommonChargeConfigService().getCurrentActiveCommonChargeDTO();
	
	if( vpnLinkDTO.getLayerType() == EndPointConstants.LAYER_TYPE_3 )
		layer3Multipler = 1 + commonCharge.getRatioL3L2()/100.0;
	
	int nearEndOfcCharge = 0;
	double nearEndOfcChargeFull = 0;
	if (nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
	{
		nearEndOfcCharge = 1000;
	}
	/* 
	if( nearEndLoopDistance > 500 )
			nearEndOfcChargeFull = ( 1000 + ( nearEndLoopDistance - 500 ) * commonCharge.getOFChargePerMeter( nearEndDTO.getDistrictID() ) ) * numOfCore * layer3Multipler;
		else
			nearEndOfcChargeFull = 1000 * numOfCore;
		if( nearEndOfcChargeFull < 1000 ) 
			nearEndOfcChargeFull = 1000;
	}
	 */
	nearEndOfcChargeFull = vpnLinkService.getOFCCost(vpnLinkDTO, nearEndDTO, commonCharge);
	 
	if( vpnLinkDTO.isNearEndReused() ){
		nearEndOfcCharge = 0;
		nearEndOfcChargeFull = 0;
	}
	
	int farEndOfcCharge = 0;
	double farEndOfcChargeFull = 0;
	if (farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
	{
		farEndOfcCharge = 1000;
	}
	/* 	if( farEndLoopDistance > 500 )
			farEndOfcChargeFull = ( 1000 + ( farEndLoopDistance - 500 ) * commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ) * numOfCore * layer3Multipler;
		else
			farEndOfcChargeFull = 1000 * numOfCore;
		if( farEndOfcChargeFull < 1000 ) 
			farEndOfcChargeFull = 1000;
	} */	
	farEndOfcChargeFull = vpnLinkService.getOFCCost(vpnLinkDTO, farEndDTO, commonCharge);
	
	double totalOFCoreCharge = 0; 
	double totalOFCoreChargeFull = 0;
	if ( nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL )
	{
		totalOFCoreCharge += nearEndOfcCharge;
		totalOFCoreChargeFull += nearEndOfcChargeFull;
	}
	if ( farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL )
	{
		totalOFCoreCharge += farEndOfcCharge;
		totalOFCoreChargeFull += farEndOfcChargeFull;
	}	

	double ofcInstallationCharge = commonCharge.getOpticalFiberInstallationCharge() * newEstablishedEndPoint * layer3Multipler;

	double bandwidthConnectionCharge = commonCharge.getChargePerPoint() * layer3Multipler;

	double distanceBetweenDistricts = vpnLinkDTO.getPopToPopDistance();
	
	if( distanceBetweenDistricts <= 0 ) {
		
		VpnNearEndDTO nearEndDTOForDistance = vpnLinkService.getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
		VpnFarEndDTO farEndDTOForDistance = vpnLinkService.getFarEndByFarEndID( vpnLinkDTO.getFarEndID() );
		
		distanceBetweenDistricts = ServiceDAOFactory.getService(DistanceService.class).getDistanceBetweenTwoLocation( nearEndDTOForDistance.getUnionID(), farEndDTOForDistance.getUnionID() );
	}

	double bandwidth = vpnLinkDTO.getVpnBandwidth();
	int bandwidthType = vpnLinkDTO.getVpnBandwidthType();
	
	if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
		bandwidth *= 1024; //Converting to MB
	
	int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID( ModuleConstants.Module_ID_VPN, vpnLinkDTO.getClientID() );
		
	TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, categoryID );
	double bandwidthCharge = tableDTO.getCostByRowValueAndColumnValue(bandwidth, distanceBetweenDistricts) * layer3Multipler;
	

	ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(Long.parseLong(clientID));
	
	ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientDTO.getClientID(), ModuleConstants.Module_ID_VPN );
	try {
%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Generate Demand Note Vpn Link </div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
	
		<form id="demandNoteForm" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do">
			
			<input id="requestTypeID" name="requestTypeID" value="60111" type="hidden">
			<input id="entityTypeID" name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK %>" type="hidden">
			<input id="entityID" name="entityID" value="<%=vpnLinkID %>" type="hidden">
			
			<input id="rootEntityID" name='rootEntityID' type='hidden' value="<%=vpnLinkID %>">
			<input id="rootEntityTypeID" name='rootEntityTypeID' type='hidden'  value="<%=EntityTypeConstant.VPN_LINK %>">
			
			<input id="clientID" name="clientID" value="<%=clientID %>" type="hidden">
			<input id="requestToAccountID" name="requestToAccountID" value="<%=clientID %>" type="hidden">
			
			<input id="description" name="description" value="Demand Note generated (System generated message)" type="hidden" />
			
			<input id="actionName" name="actionName" class="actionName" type="hidden"
				   value="/VpnLinkAction.do?entityID=<%=vpnLinkID %>&entityTypeID=<%=EntityTypeConstant.VPN_LINK %>" />
				   
			<input type="hidden"  placeholder="No of OF core" class="form-control" name="noOfOFCore" value="<%=nearEndDTO.getCoreType() %>" readonly="readonly" required>
			<input type="hidden"  placeholder="VPN distance (km)" class="form-control" name="vpnDistance" id="vpnDistance" value="" required>
			<input type="hidden"  placeholder="Local loop length Near (m)" class="form-control" name="localLoopNear" id="localLoopNear" value="<%=nearEndLoopDistance %>" required >
			<input type="hidden"  placeholder="Local loop length Far (m)" class="form-control" name="localLoopFar" id="localLoopFar" value="<%=farEndLoopDistance %>" required >
			
			<div class="form-body">
				
			   	<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					
					<div class="col-sm-4">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="<%=clientDTO.getName() %>" required readonly="readonly">
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=clientID %>" required>
					</div>
					
					<div class="col-sm-2">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=6&entityID=<%=clientID %>"> View Client Details </a>
					</div>
					
				</div>
				
			
				<div class="form-group">
				
					<label for="cnName" class="col-sm-3 control-label">Link Name</label>
					
					<div class="col-sm-4">
						<input type="text"  placeholder="Link Name" class="linkName form-control" name="linkName" value="<%=vpnLinkDTO.getName() %>" required readonly="readonly">
						<input type="hidden" class="form-control" name="linkID" value="<%=vpnLinkID %>" required>
					</div>
					
					<div class="col-sm-2">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/VpnLinkAction.do?entityID=<%=vpnLinkDTO.getID() %>&entityTypeID=<%=EntityTypeConstant.VPN_LINK %>&getMode=details">View Link Details</a>
					</div>
					
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>OTC:</b> </span> </div>
				
				<div class="form-group">
				
					<label for="bwConnectionCharge" class="col-sm-3 control-label">BW Connection Charge (Tk): </label>
					<div class="col-sm-4">
						<input type="number" step="1"  placeholder="BW Connection Charge" class="form-control"
								name="bwConnectionCharge" id="bwConnectionCharge" value="<%=bandwidthConnectionCharge%>" readonly="readonly" required />
					</div>
				</div>
				
				<div class="form-group">
				
					<label for="ofcInstallationCharge" class="col-sm-3 control-label">OFC Installation Charge (Tk): </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OFC Installation Charge" class="form-control" 
							name="ofcInstallationCharge" id="ofcInstallationCharge" value="<%=ofcInstallationCharge%>" readonly="readonly" required />
					</div>
				</div>
				
				<%if( nearEndDTO.getOfcProviderTypeID() == 1 ){ %>
				
					<input type="hidden" name="localEndOFCProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="ofcLayingCost" class="col-sm-3 control-label">OFC Laying Cost Local Loop (Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="OFC Laying Cost" class="form-control" 
								name="ofcLayingCost_local" id='ofcLayingCost_local' value="0"  min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="establishmentCost" class="col-sm-3 control-label">Establishment Cost Local Loop (Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Establishment Cost" class="form-control" 
								name="establishmentCost_local" id="establishmentCost_local" value="0" min="0" readonly="readonly" required />
						</div>
					</div>
					
				<%}
				if( farEndDTO.getOfcProviderTypeID() == 1 ) {%>
				
					<input type="hidden" name="remoteEndOFCProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="ofcLayingCost" class="col-sm-3 control-label">OFC Laying Cost Remote Loop (Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="OFC Laying Cost" class="form-control" 
								name="ofcLayingCost_remote" id='ofcLayingCost_remote' value="0"  min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="establishmentCost" class="col-sm-3 control-label">Establishment Cost Remote Loop (Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Establishment Cost" class="form-control" 
								name="establishmentCost_remote" id="establishmentCost_remote" value="0" min="0" readonly="readonly" required />
						</div>
					</div>
				<%} %>
				
				<%if( nearEndDTO.getTerminalDeviceProvider() == 1 ){ %>
				
					<input type="hidden" name="localEndProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="mediaConverterPiece" class="col-sm-3 control-label">Media Converter Local End (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Media Converter Piece" class="form-control" 
								name="mediaConverterPiece_local" id="mediaConverterPiece_local" value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="mediaConverterPrice" class="col-sm-3 control-label">Media Converter Local End (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Media Converter Price" class="form-control" 
								name="mediaConverterPrice_local" id='mediaConverterPrice_local' value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="sfpModulePiece" class="col-sm-3 control-label">SFP Module Local End (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="SFP Module Piece" class="form-control"
								 name="sfpModulePiece_local" id="sfpModulePiece_local" value="0" min="0" required>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="sfpModulePrice" class="col-sm-3 control-label">SFP Module Local End (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="SFP Module Price" class="form-control" 
								name="sfpModulePrice_local" id="sfpModulePrice_local" value="0" min="0" required>
						</div>
					</div>
				<%} 
				if( farEndDTO.getTerminalDeviceProvider() == 1 ){%>
					
					<input type="hidden" name="remoteEndProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="mediaConverterPiece" class="col-sm-3 control-label">Media Converter Remote End (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Media Converter Piece" class="form-control" 
								name="mediaConverterPiece_remote" id="mediaConverterPiece_remote" value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="mediaConverterPrice" class="col-sm-3 control-label">Media Converter Remote End (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="Media Converter Price" class="form-control" 
								name="mediaConverterPrice_remote" id='mediaConverterPrice_remote' value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="sfpModulePiece" class="col-sm-3 control-label">SFP Module Remote End (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="SFP Module Piece" class="form-control"
								 name="sfpModulePiece_remote" id="sfpModulePiece_remote" value="0" min="0" required>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="sfpModulePrice" class="col-sm-3 control-label">SFP Module Remote End (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number" step="1" placeholder="SFP Module Price" class="form-control" 
								name="sfpModulePrice_remote" id="sfpModulePrice_remote" value="0" min="0" required>
						</div>
					</div>
				<% }%>
				
				
				<div class="form-group">
					
					<label for="others" class="col-sm-3 control-label">Others: </label>
					<div class="col-sm-4">
						<input type="number" step="1" placeholder="Other cost" class="form-control"
							 name="others" id='others' value="0" min="0" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="securityCharge" class="col-sm-3 control-label">Security Deposit: </label>
					<div class="col-sm-4">
						<%
							//double securityCharge = bandwidthCharge;
							double securityCharge = bandwidthCharge + Math.round( totalOFCoreChargeFull );
							if( clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON ){
								securityCharge = 0.0;
							}
						%>
						<input type="number" step="1" placeholder="Security Charge" class="form-control" 
							name="securityCharge" id='securityCharge' value="<%=securityCharge %>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>MRC:</b> </span> </div>
				
				<div class="form-group">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label"> </label>
					<div class="col-md-6">
						<label class="checkbox"> 
							<span><input type="checkbox" id="useMinOFCCharge" name="useMinOFCCharge" ></span> 
							Use minimum OFC Charge 
						</label>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Local end loop OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="localEndOFCCharge" id="localEndOFCCharge"	value="<%=nearEndOfcChargeFull %>" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Remote end loop OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="farEndOFCCharge" id="farEndOFCCharge" value="<%=farEndOfcChargeFull %>" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Total OFC charge: </label>
					<div class="col-sm-4">
						<input type="number" step="1" placeholder="OF core charge" class="form-control" name="OFCoreCharge" id="OFCoreCharge" 
								value="<%=Math.round( totalOFCoreChargeFull )%>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="bwCharge" class="col-sm-3 control-label">Bandwidth charge: </label>
					<div class="col-sm-4">
						<input type="number" placeholder="Bandwith Charge" class="form-control" name="bwCharge" id="bwCharge" 
							value="<%=bandwidthCharge %>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="noOfYear" class="col-sm-3 control-label">Month: </label>
					<div class="col-sm-4">
						<input class="form-control" name="month" value="1" id="month" min="1" type="number" />
					</div>
				</div>
				
				<br/>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>Summary:</b> </span> </div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Grand Total (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="grandTotal" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Percentage Discount(%): </label>
					<div class="col-sm-4">
						<input type="number" step="1" class="form-control" id="discountPercentage" name="discountPercentage" min="0" max="100" value="0"/>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Discount (Tk): </label>
					<div class="col-sm-4">
						<input type="number" step="1" class="form-control" id="discount" name="discount" min="0" value="0"/>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Total Payable (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="totalPayable" readonly="readonly" />
						<!-- <h4 class="odometer" id="totalPayable"></h4> -->
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">VAT (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="vat" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Net Payable (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="netPayable" readonly="readonly" />
						<!-- <h4 class="odometer netPayable" id="netPayable"></h4> -->
					</div>
				</div>
			</div>
			
			<div class="form-actions text-center">
			
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl" type="submit" >Submit</button>
				
			</div>
			
		</form>
	</div>
</div>

<%} catch (Exception ex) {
	
	logger.debug("General Error " + ex);
}
%>
<script type="text/javascript">
	
	var context = "<%=request.getContextPath() %>";
	var vpnLinkID = "<%=vpnLinkID %>";
	var localOFCChargeFull = "<%=Math.round( nearEndOfcChargeFull ) %>";
	var remoteOFCChargeFull = "<%=Math.round( farEndOfcChargeFull ) %>";
	var totalOFCChargeFull = "<%=Math.round( totalOFCoreChargeFull ) %>";
	
	var nearEndNotReused = <%= !vpnLinkDTO.isNearEndReused() && nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL %>;
	var farEndNotReused = <%= farEndDTO.getParentEndPointID() == 0 && farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL %>;
</script>
<script src="${context}assets/scripts/vpn/link/generateDemandNote.js" type="text/javascript"></script>
