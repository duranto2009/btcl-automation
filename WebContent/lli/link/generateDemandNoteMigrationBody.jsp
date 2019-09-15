<%@page import="client.ClientTypeService"%>
<%@page import="costConfig.TableDTO"%>
<%@page import="common.StringUtils"%>
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
<%@page import="lli.link.LliFarEndDTO"%>
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
<%@page import="login.LoginDTO"%>

<%
	Logger logger = Logger.getLogger(this.getClass());
	
	LliLinkService lliLinkService = new LliLinkService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	String clientID = request.getParameter( "clientID" );
	String lliLinkID = request.getParameter( "lliLinkID" );
	
	LliLinkDTO lliLinkDTO = new LliLinkDTO();
	LliFarEndDTO farEndDTO = new LliFarEndDTO();
	
	if( !StringUtils.isBlank( lliLinkID ) )
		lliLinkDTO = lliLinkService.getLliLinkByLliLinkID( Long.parseLong( lliLinkID ));
	
	if( lliLinkDTO.getID() != null && lliLinkDTO.getID() > 0 ){
	
		farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
	}
	
	int newEstablishedEndPoint = 0;

	if( farEndDTO.getID() > 0 && farEndDTO.getParentEndPointID() == 0 )
		newEstablishedEndPoint++;
	
	int farEndLoopDistance = 0;
	
	if( lliLinkDTO.getID() != null && lliLinkDTO.getID() > 0 ){
		
		farEndLoopDistance = lliLinkService.getRemoteLoopDistance( lliLinkDTO );
	}
	
	LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getLatestDTO();
	
	BigDecimal OFCoreCharge = new BigDecimal( farEndLoopDistance).multiply( new BigDecimal(commonCharge.getOFChargePerMeter() ) );
	
	double totalOFCoreCharge = OFCoreCharge.setScale( 2 ).doubleValue();
	
	double ofcInstallationCharge = commonCharge.getOpticalFiberInstallationCharge() * newEstablishedEndPoint;
	
	double bandwidthConnectionCharge = commonCharge.getChargePerPoint();
	
	double distanceBetweenDistricts = 0;
	double bandwidthCharge = 0;
	
	if( lliLinkDTO.getID() != null && lliLinkDTO.getID() > 0 ){
		
		int year = EndPointConstants.yearConnectionTypeMapping.get( lliLinkDTO.getConnectionType() );
		
		double bandwidth = lliLinkDTO.getLliBandwidth();
		int bandwidthType = lliLinkDTO.getLliBandwidthType();
		
		if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
	bandwidth *= 1024; //Converting to MB
		
		int categoryID = new ClientTypeService().getClientCategoryByModuleIDAndClientID( EntityTypeConstant.LLI_LINK / EntityTypeConstant.MULTIPLIER2, lliLinkDTO.getClientID());
		TableDTO tableDTO = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_LLI, categoryID);
		bandwidthCharge = tableDTO.getCostByRowValueAndColumnValue(bandwidth, year);
		
	}
	
	ClientDTO clientDTO = new ClientDTO();
	
	if( !StringUtils.isBlank( clientID ) )
		clientDTO = AllClientRepository.getInstance().getClientByClientID( Long.parseLong( clientID ) );
	try{
%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Bill Migration </div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
	
		<form id="demandNoteForm" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do">
			
			<input id="requestTypeID" name="requestTypeID" value="<%=LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE %>" type="hidden">
			
			<input id="entityTypeID" name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK %>" type="hidden">
			<input id="entityID" name="entityID" value="<%=lliLinkID %>" type="hidden">
			
			<input id="rootEntityID" name='rootEntityID' type='hidden' value="<%=lliLinkID %>">
			<input id="rootEntityTypeID" name='rootEntityTypeID' type='hidden'  value="<%=EntityTypeConstant.LLI_LINK %>">
			
			<input id="clientID" name="clientID" value="<%=clientID %>" type="hidden">
			<input id="requestToAccountID" name="requestToAccountID" value="<%=clientID %>" type="hidden">
			
			<input id="description" name="description" value="Demand Note generated (System generated message)" type="hidden" />
			
			<input id="actionName" name="actionName" class="actionName" type="hidden"
				   value="/LliLinkAction.do?entityID=<%=lliLinkID %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>" />
				   
			<input type="hidden" name="expireTime" value="1" />
 			<input type="hidden"  placeholder="No of OF core" class="form-control" name="noOfOFCore" value="<%=lliLinkDTO.getCoreType() %>" readonly="readonly" required>
			<input type="hidden"  placeholder="LLI distance (km)" class="form-control" name="lliDistance" id="lliDistance" value="" required>
			<input type="hidden"  placeholder="Local loop length Far (m)" class="form-control" name="localLoopFar" id="localLoopFar" value="<%=farEndLoopDistance %>" required >
			
			<div class="form-body">
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label" style="color:blue"> Select Bill Type <span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<input type="radio" name="otc_mrc" value="OTC" checked></span> Demand Note
							</label> 
							<label class="radio-inline"> <span>
								<input type="radio" name="otc_mrc" value="MRC"></span> Monthly Bill
							</label>
						</div>
					</div>
				</div>
				
				
			   	<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
				
					<% String clientName = "";
					   if( clientDTO.getClientID() > 0 )
						   clientName = clientDTO.getName();
					%>
					
					<div class="col-sm-4">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="<%=clientName %>" required>
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=clientID %>" required>
					</div>
					
					<div class="col-sm-2">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=7&entityID=<%=clientID %>" required> View Client Details </a>
					</div>
					
				</div>
				
			
				<div class="form-group">
				
					<label for="cnName" class="col-sm-3 control-label">Connection Name</label>
					
					<% String linkName = "";
					   if( lliLinkDTO.getID() != null && lliLinkDTO.getID() > 0 )
					   		linkName = lliLinkDTO.getName();
					%>
					
					<div class="col-sm-4">
						<input type="text"  placeholder="Connection  Name" class="linkNameMigration form-control" name="linkName" value="<%=linkName %>" required>
						<input type="hidden" class="form-control" name="linkID" value="<%=lliLinkID %>" required>
					</div>
					
					<div class="col-sm-2">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/LliLinkAction.do?entityID=<%=lliLinkDTO.getID() %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>&getMode=details">View Connection Details</a>
					</div>
					
					<br/>
					<br/>
					
					<div class="form-group">
						
						<label for="activationDateFrom" class="col-sm-3 control-label">Activation Date From: </label>
						<div class="col-sm-4">
							<input name="activationDateFrom" class="form-control datepicker" id="activationDateFrom" value="" autocomplete="off" type="text">
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="activationDateTo" class="col-sm-3 control-label">Activation Date To: </label>
						<div class="col-sm-4">
							<input name="activationDateTo" class="form-control datepicker" id="activationDateTo" value="" autocomplete="off" type="text">
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="lastPaymentDate" class="col-sm-3 control-label">Last Payment Date: </label>
						<div class="col-sm-4">
							<input name="lastPaymentDate" class="form-control datepicker" id="lastPaymentDate" value="" autocomplete="off" type="text">
						</div>
					</div>
					
				</div>
				
				<div id="OTC_Div" >
				
					<div class="form-group"> <span class="col-sm-3 text-right"> <b>OTC:</b> </span> </div> 
					
					<div class="clearfix"></div>
					
					<div class="form-group">
					
						<label for="bwConnectionCharge" class="col-sm-3 control-label">BW Connection Charge (Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="BW Connection Charge" class="form-control"
									name="bwConnectionCharge" id="bwConnectionCharge" value="0" min="0" required />
						</div>
					</div>
					
					<div class="form-group">
					
						<label for="ofcInstallationCharge" class="col-sm-3 control-label">OFC Installation Charge (Tk): </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="OFC Installation Charge" class="form-control" 
								name="ofcInstallationCharge" id="ofcInstallationCharge" value="0" min="0" required />
						</div>
					</div>
					
					<%if( farEndDTO.getOfcProviderTypeID() == 1 ) {%>
					
						<input type="hidden" name="remoteEndOFCProvidedByBTCL" value="true" />
						
						<div class="form-group">
						
							<label for="ofcLayingCost" class="col-sm-3 control-label">OFC Laying Cost Remote Loop (Tk): </label>
							<div class="col-sm-4">
								<input type="number"  placeholder="OFC Laying Cost" class="form-control" 
									name="ofcLayingCost_remote" id='ofcLayingCost_remote' value="0"  min="0" required />
							</div>
						</div>
						
						<div class="form-group">
						
							<label for="establishmentCost" class="col-sm-3 control-label">Establishment Cost Remote Loop (Tk): </label>
							<div class="col-sm-4">
								<input type="number"  placeholder="Establishment Cost" class="form-control" 
									name="establishmentCost_remote" id="establishmentCost_remote" value="0" min="0" step="any" required />
							</div>
						</div>
					<%} %>
					
					<% if( farEndDTO.getTerminalDeviceProvider() == 1 ){%>
						
						<input type="hidden" name="remoteEndProvidedByBTCL" value="true" />
						
						<div class="form-group">
						
							<label for="mediaConverterPiece" class="col-sm-3 control-label">Media Converter Remote End (Piece): </label>
							
							<div class="col-sm-4">
								<input type="number"  placeholder="Media Converter Piece" class="form-control" 
									name="mediaConverterPiece_remote" id="mediaConverterPiece_remote" value="0" required />
							</div>
						</div>
						
						<div class="form-group">
							
							<label for="mediaConverterPrice" class="col-sm-3 control-label">Media Converter Remote End (Price Tk): </label>
							<div class="col-sm-4">
								<input type="number"  placeholder="Media Converter Price" class="form-control" 
									name="mediaConverterPrice_remote" id='mediaConverterPrice_remote' value="0" required />
							</div>
						</div>
						
						<div class="form-group">
						
							<label for="sfpModulePiece" class="col-sm-3 control-label">SFP Module Remote End (Piece): </label>
							
							<div class="col-sm-4">
								<input type="number"  placeholder="SFP Module Piece" class="form-control"
									 name="sfpModulePiece_remote" id="sfpModulePiece_remote" value="0" required>
							</div>
						</div>
						
						<div class="form-group">
							
							<label for="sfpModulePrice" class="col-sm-3 control-label">SFP Module Remote End (Price Tk): </label>
							<div class="col-sm-4">
								<input type="number"  placeholder="SFP Module Price" class="form-control" 
									name="sfpModulePrice_remote" id="sfpModulePrice_remote" value="0" required>
							</div>
						</div>
					<% }%>
					
					
					<div class="form-group">
						
						<label for="others" class="col-sm-3 control-label">Others: </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="Other cost" class="form-control"
								 name="others" id='others' value="0" required>
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="securityCharge" class="col-sm-3 control-label">Security Deposit: </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="Security Charge" class="form-control" 
							name="securityCharge" id='securityCharge' value="0" required>
						</div>
					</div>
				
				</div>
				
				<div id="MRC_Div">
				
					<div class="form-group"> <span class="col-sm-3 text-right"> <b>MRC:</b> </span> </div>
					
					<div class="form-group">
						
						<label for="farEndOFCCharge" class="col-sm-3 control-label">OFC charge: </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="OF core charge" class="form-control" name="farEndOFCCharge" id="farEndOFCCharge" 
									value="0" min="0" />
						</div>
					</div>
					
					<div class="form-group" style="display:none">
						
						<label for="OFCoreCharge" class="col-sm-3 control-label">Total OFC charge: </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="OF core charge" class="form-control" name="OFCoreCharge" id="OFCoreCharge" 
									value="0"  min="0" required readonly />
						</div>
					</div>
					
					<div class="form-group">
						
						<label for="bwCharge" class="col-sm-3 control-label">Bandwidth charge: </label>
						<div class="col-sm-4">
							<input type="number"  placeholder="Bandwith Charge" class="form-control" name="bwCharge" id="bwCharge" 
								value="0" min="0" required>
						</div>
					</div>
					
					<input class="form-control" name="month" value="1" id="month" type="hidden" />
				
				</div>
				
				<br/>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>Summary:</b> </span> </div>
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Grand Total (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control odometer" id="grandTotal" readonly="readonly" />
						<!-- <h4 class="odometer" id="grandTotal"></h4> -->
					</div>
				</div>
				
				
				<div class="form-group">
					
					<label for="totalPayable" class="col-sm-3 control-label">Discount (Tk): </label>
					<div class="col-sm-4">
						<input type="number" class="form-control" id="discount" name="discount" value="0" min="0" step="any" />
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
	var lliLinkID = "<%=lliLinkID %>";
</script>
<script src="${context}assets/scripts/lli/link/generateDemandNote.js" type="text/javascript"></script>
<script>

	$("input[name='otc_mrc']").click( function(e){
		
		var value = e.target.defaultValue;
		
		if( value == "OTC" ){
			
			$("#MRC_Div").show();
			$("#OTC_Div").show();
		}
		else if( value == "MRC" ){
			
			$("#MRC_Div").show();
			$("#OTC_Div").hide();
			
			$('#OTC_Div').find(':input').each(function() {
			    
				if(this.type == 'number'){
			     
					$(this).val("0");
			    }
			});
		}
	});
	
	$("#localEndOFCCharge, #farEndOFCCharge").on( "input", function(){
		
		var localCharge = parseFloat( $("#localEndOFCCharge").val() );
		var farCharge = parseFloat( $("#farEndOFCCharge").val() );
		
		var total = 0.0;
		if( localCharge ) total += localCharge;
		if( farCharge ) total += farCharge;
		
		$("#OFCoreCharge").val( total );
	});
</script>

<script>

	$(document).ready(function(){
		$("#linkHyperlink").hide();
		$("#clientHyperLink").hide();
		
	    $('.datepicker').datepicker({
           	orientation: "top",
            autoclose: true,
            format: 'dd/mm/yyyy',
            todayBtn: 'linked',
            todayHighlight:	true
        });
	})
	
</script>
	
