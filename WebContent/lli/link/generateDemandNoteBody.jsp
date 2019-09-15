<%@page import="requestMapping.Service"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="ipaddress.IpBlock"%>
<%@page import="ipaddress.IpAddressService"%>
<%@page import="client.ClientTypeService"%>
<%@page import="costConfig.TableDTO"%>
<%@page import="common.RegistrantTypeConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.bill.*"%>
<%@page import="common.CommonDAO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="common.RequestFailureException"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.DatabaseConnectionFactory"%>
<%@page import="costConfig.CostConfigService"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="lli.configuration.LLICostConfigurationService"%>
<%@page import="lli.configuration.LLIFixedCostConfigurationDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="util.SqlGenerator"%>
<%@page import="lli.link.LliFRResponseExternalDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="java.util.List"%>
<%@page import="common.ModuleConstants"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="util.SOP"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="java.util.HashMap,java.util.Map"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Logger logger = Logger.getLogger(this.getClass());
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);

	String clientID = request.getParameter( "clientID" );
	String lliLinkID = request.getParameter( "lliLinkID" );
	
	ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( Long.parseLong( clientID ) );
	ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( clientDTO.getClientID(), ModuleConstants.Module_ID_LLI );
	
	LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID( Long.parseLong( lliLinkID ));
	LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
	
	
	int bandwidthType = lliLinkDTO.getLliBandwidthType();
	double bandwidthCharge = 0;
	double bandwidth = ( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB ) ? lliLinkDTO.getLliBandwidth() * 1024 : lliLinkDTO.getLliBandwidth();
		
	int newEstablishedEndPoint = 0; 
	
	if( farEndDTO.getParentEndPointID() == 0 && farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL )
		newEstablishedEndPoint++;

	int farEndLoopDistance = LliLinkService.getRemoteLoopDistance( lliLinkDTO );
	
	int numOfCore = farEndDTO.getCoreType();
	
	LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getCurrentActiveLLI_FixedCostConfigurationDTO();
	
	int farEndOfcCharge = 0;
	double farEndOfcChargeFull = 0;
	if (farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL) {
		farEndOfcCharge = 1000;
	}

	farEndOfcChargeFull = lliLinkService.getOFCCost(lliLinkDTO, farEndDTO, commonCharge);
	boolean isClientEligibleToBeChargedLessInTemporaryConnection = lliLinkService.isClientEligibleToBeChargedLessInTemporaryConnection(lliLinkDTO, clientDetailsDTO, bandwidth);
	if(isClientEligibleToBeChargedLessInTemporaryConnection){
		farEndOfcChargeFull /= 2;
	}
	
	logger.debug("commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ) " + commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ); 
	logger.debug("farEndOfcChargeFull " + farEndOfcChargeFull);
	
	double totalOFCoreCharge = 0; 
	double totalOFCoreChargeFull = 0;
	if (farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL)
	{
		totalOFCoreCharge += farEndOfcCharge;
		totalOFCoreChargeFull += farEndOfcChargeFull;
	}	
	
	double ofcInstallationCharge = commonCharge.getOpticalFiberInstallationCharge() * newEstablishedEndPoint;
	
	double bandwidthConnectionCharge = commonCharge.getChargePerPoint();

	int year = EndPointConstants.yearConnectionTypeMapping.get( lliLinkDTO.getConnectionType() );
	
		
	double fiveYearBandwidth = lliLinkDTO.getFiveYearBandwidth();
	int fiveYearBandwidthType = lliLinkDTO.getFiveYearBandwidthType();
	double fiveYearBandwidthCharge = 0;
	if( fiveYearBandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
		fiveYearBandwidth *= 1024;
	
	int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID( EntityTypeConstant.LLI_LINK / EntityTypeConstant.MULTIPLIER2, lliLinkDTO.getClientID());
		
	TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_LLI, categoryID);
	bandwidthCharge = tableDTO.getCostByRowValueAndColumnValue( bandwidth - fiveYearBandwidth, year );
	
	if( fiveYearBandwidth == 0 )
		fiveYearBandwidthCharge = 0;
	else
		fiveYearBandwidthCharge = tableDTO.getCostByRowValueAndColumnValue( fiveYearBandwidth, 5 );
		
	bandwidthCharge += fiveYearBandwidthCharge;
	
	if( bandwidthCharge < 0 )
		throw new Exception( "No suitable cost chart found " );
	
	
	
	
	
	List<IpBlock> ipList = ServiceDAOFactory.getService(IpAddressService.class).getIPAddressByEntityID(lliLinkDTO.getID());
	
	int numOfAdditionalIP = 0;
	
	logger.debug( "Ip list size - " + ipList.size() );
	
	for(IpBlock ipBlock: ipList)
	{
		if( ipBlock.getUsageType() == InventoryConstants.USAGE_ADDITIONAL )
		{ 
	numOfAdditionalIP += ipBlock.getBlockSize();
		}
	}
	
	logger.debug( "No of additional ip - " + numOfAdditionalIP );
	
	double ipAddressCost = 0.0;
	
	if( numOfAdditionalIP <= 4 )
		ipAddressCost = numOfAdditionalIP * 800;
	else{
		
		ipAddressCost = ( ( numOfAdditionalIP - 4 ) * 200 + 4 * 800 );
	}
	
	logger.debug( "Ip address cost - " + ipAddressCost );
	
	double ipAddressCostTemp = BillUtil.getCostForIpAddress( numOfAdditionalIP );
	
	if( ipAddressCostTemp == ipAddressCost )
		ipAddressCost = ipAddressCostTemp;
	try{
%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Generate Demand Note LLI Connection </div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
	
		<form id="demandNoteForm" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do">
			
			<input id="requestTypeID" name="requestTypeID" value="<%=LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE %>" type="hidden">
			
			<input id="entityTypeID" name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK %>" type="hidden">
			<input id="entityID" name="entityID" value="<%=lliLinkID %>" type="hidden">
			
			<input id="rootEntityID" name='rootEntityID' type='hidden' value="<%=lliLinkID %>">
			<input id="rootEntityTypeID" name='rootEntityTypeID' type='hidden'  value="<%=EntityTypeConstant.LLI_LINK %>">
			
			<input id="clientID" name="clientID" value="<%=clientID %>" type="hidden">
			<input id="requestToAccountID" name="requestToAccountID" value="<%=clientID %>" type="hidden">
			
			<input id="description" name="description" value="Demand Note generated (System generated message)" type="hidden" />
			
			<input id="actionName" name="actionName" class="actionName" type="hidden"
				   value="/LliLinkAction.do?entityID=<%=lliLinkID %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>" />
				   
			<input type="hidden"  placeholder="No of OF core" class="form-control" name="noOfOFCore" id="noOfOFCore" value="<%=farEndDTO.getCoreType() %>" readonly="readonly" required>
			<input type="hidden"  placeholder="LLI distance (km)" class="form-control" name="lliDistance" id="lliDistance" value="" required>
			<input type="hidden"  placeholder="Local loop length Far (m)" class="form-control" name="localLoopFar" id="localLoopFar" value="<%=farEndLoopDistance %>" required >
			
			<div class="form-body">
				
			   	<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					
					<div class="col-sm-4">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="<%=clientDTO.getName() %>" required readonly="readonly">
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=clientID %>" required>
					</div>
					
					<div class="col-sm-2">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=7&entityID=<%=clientID %>"> View Client Details </a>
					</div>
					
				</div>
				
			
				<div class="form-group">
				
					<label for="cnName" class="col-sm-3 control-label"> Connection Name </label>
					
					<div class="col-sm-4">
						<input type="text"  placeholder="Connection  Name" class="linkName form-control" name="linkName" value="<%=lliLinkDTO.getName() %>" required readonly="readonly">
						<input type="hidden" class="form-control" name="linkID" value="<%=lliLinkID %>" required>
					</div>
					
					<div class="col-sm-2">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/LliLinkAction.do?entityID=<%=lliLinkDTO.getID() %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>&getMode=details">View Connection Details</a>
					</div>
					
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>OTC:</b> </span> </div>
				
				<div class="form-group">
				
					<label for="bwConnectionCharge" class="col-sm-3 control-label">BW Connection Charge (Tk): </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="BW Connection Charge" class="form-control"
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
				
				
				<% if( farEndDTO.getOfcProvider() == 1 ) {%>
				
					<input type="hidden" name="remoteEndOFCProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="ofcLayingCost" class="col-sm-3 control-label">OFC Laying Cost (Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="OFC Laying Cost" class="form-control non-neg" 
								name="ofcLayingCost_remote" id='ofcLayingCost_remote' value="0"  min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="establishmentCost" class="col-sm-3 control-label">Establishment Cost (Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="Establishment Cost" class="form-control" 
								name="establishmentCost_remote" id="establishmentCost_remote" value="0" min="0" readonly="readonly" required />
						</div>
					</div>
				<%} %>
				
				
				<% if( farEndDTO.getTerminalDeviceProvider() == 1 ){%>
					
					<input type="hidden" name="remoteEndProvidedByBTCL" value="true" />
					
					<div class="form-group">
					
						<label for="mediaConverterPiece" class="col-sm-3 control-label">Media Converter (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number"  placeholder="Media Converter Piece" class="form-control non-neg-int" 
								name="mediaConverterPiece_remote" id="mediaConverterPiece_remote" value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="mediaConverterPrice" class="col-sm-3 control-label">Media Converter (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="Media Converter Price" class="form-control non-neg" 
								name="mediaConverterPrice_remote" id='mediaConverterPrice_remote' value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="sfpModulePiece" class="col-sm-3 control-label">SFP Module (Piece): </label>
						
						<div class="col-sm-4">
							<input type="number"  placeholder="SFP Module Piece" class="form-control non-neg-int"
								 name="sfpModulePiece_remote" id="sfpModulePiece_remote" value="0" min="0" required>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="sfpModulePrice" class="col-sm-3 control-label">SFP Module (Price Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="SFP Module Price" class="form-control non-neg" 
								name="sfpModulePrice_remote" id="sfpModulePrice_remote" value="0" min="0" required>
						</div>
					</div>
				<% }%>
				
				
				<div class="form-group">
					
					<label for="others" class="col-sm-3 control-label">Others: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Other cost" class="form-control non-neg"
							 name="others" id='others' value="0" min="0" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="securityCharge" class="col-sm-3 control-label">Security Deposit: </label>
					<div class="col-sm-4">
					
						<%
							double securityCharge = bandwidthCharge;
						
							if( clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON ||
								lliLinkDTO.getConnectionType() == EndPointConstants.CONNECTION_TYPE_TEMPORARY_ ){
								
								securityCharge = 0.0;
							}
						%>
						<input type="number"  placeholder="Security Charge" class="form-control" 
						name="securityCharge" id='securityCharge' value="<%=securityCharge %>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="others" class="col-sm-3 control-label">IP Address Cost: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Other cost" class="form-control"
							 name="ipAddressCost" id='ipAddressCost' value="<%=ipAddressCost %>" readonly step="any" required />
					</div>
				</div>
				
				<div class="form-group"><span class="col-sm-3 text-right"><b>MRC:</b></span></div>
				<div class="form-group">
					<label for="OFCoreCharge" class="col-sm-3 control-label"> </label>
					<div class="col-md-6">
						<label class="checkbox"><span><input type="checkbox" id="useMinOFCCharge" name="useMinOFCCharge" ></span>Use minimum OFC Charge</label>
					</div>
				</div>
				
				<div class="form-group">
					<label for="OFCoreCharge" class="col-sm-3 control-label"> OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control"	name="farEndOFCCharge" id="farEndOFCCharge" value="<%=farEndOfcChargeFull%>" readonly="readonly" />
					</div>
				</div>
				
				<input placeholder="OF core charge" class="form-control" name="OFCoreCharge" id="OFCoreCharge" value="<%=Math.round( totalOFCoreChargeFull) %>" readonly="readonly" required type="hidden" > 
				
				
				<div class="form-group">
					<label for="bwCharge" class="col-sm-3 control-label"> Bandwidth charge: </label>
					<div class="col-sm-4">
					<%
						if(isClientEligibleToBeChargedLessInTemporaryConnection){
							bandwidthCharge /= 2;
						}
					%>
						<input type="number"  placeholder="Bandwith Charge" class="form-control" name="bwCharge" id="bwCharge" 
							value="<%=bandwidthCharge %>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="noOfYear" class="col-sm-3 control-label">Month: </label>
					<div class="col-sm-4">
					<%
						String readOnlyMonth = "";
						if( lliLinkDTO.getConnectionType() == EndPointConstants.CONNECTION_TYPE_TEMPORARY_ )
							readOnlyMonth = "readonly";
					%>
						<input class="form-control pos-int-month" name="month" value="1" id="month" min="1"  type="number" step="1" <%=readOnlyMonth %> />
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
						<input type="number" class="form-control non-neg" id="discount" name="discount" value="0" min="0"/>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Total Payable (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="totalPayable" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">VAT (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="vat" readonly="readonly" />
						<!-- <h4 class="odometer" id="vat"></h4> -->
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
			
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl"type="submit" >Submit</button>
				
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
	var lliLinkID = "<%=lliLinkID %>";
	
	var remoteOFCChargeFull = "<%=Math.round( farEndOfcChargeFull ) %>";
	var totalOFCChargeFull = "<%=Math.round( totalOFCoreChargeFull ) %>";
	
	var farEndNotReused = <%= farEndDTO.getParentEndPointID() == 0 && farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL %>;
	
	function isInt(value){
		return !isNaN(value) && ( 
	  			function(x) { 
					return (x | 0) === x; 
				}
	  		)(parseFloat(value));	
	}
	function posIntMonth(x){
		if(isInt(x) && parseInt(x) >= 1){
			return true;
		}else {
			toastr.error('Month count can not be negative, decimal or zero');	
			return false;
		}
	}
	function nonNegInt(x){
		if(isInt(x) && parseInt(x)>0){
			return true;
		}else {
			toastr.error('Provide a non-negative integer');
			return false;
		}
	}
	function nonNeg(x){
		if(isInt(x) && parseInt(x)>0){
			return true;
		}else {
			toastr.error('Provide a non-negative integer');
			return false;
		}
	}
	function validate(){
		var x, div;
		$('.pos-int-month').each(function(index){
			x = $(this).val();
			div = $(this).closest('.form-group');
			if($(div).hasClass('has-error')){
				$(div).removeClass('has-error');
			}
			if(!posIntMonth(x)){
				$(div).addClass('has-error');
				return false;
			}
		});
		$('.non-neg-int').each(function(idx){
			x = $(this).val();
			div = $(this).closest('form-group');
			if($(div).hasClass('has-error')){
				$(div).removeClass('has-error');
			}
			if(!nonNegInt(x)){
				$(div).addClass('has-error');
				return false;
			}
		});
		$('.non-neg').each(function(idx){
			x = $(this).val();
			div = $(this).closest('form-group');
			if($(div).hasClass('has-error')){
				$(div).removeClass('has-error');
			}
			if(!nonNeg(x)){
				$(div).addClass('has-error');
				return false;
			}
		});
		return true;
	}
	$(document).ready(function(){
		$('.pos-int-month').on('focusout', function(){
			var x = $(this).val();
			return posIntMonth(x);	
		});
		
		$('.non-neg-int').on('focusout', function(){
			var x = $(this).val();
			return nonNegInt(x);
			
		});
		$('.non-neg').on('focusout', function(){
			var x = $(this).val();
			return nonNeg(x);
		});
		$('#updateBtn').on('submit', function(){
			return validate();
		});
		
	});
</script>
<script src="${context}assets/scripts/lli/link/generateDemandNote.js" type="text/javascript"></script>
