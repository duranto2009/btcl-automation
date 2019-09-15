<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="common.ClientDTO"%>
<%

	int nearEndLoopDistance = (Integer)request.getAttribute( "nearEndLoopDistance" );
	int farEndLoopDistance = (Integer)request.getAttribute( "farEndLoopDistance" );
	
	Double nearEndLoopCharge = (Double)request.getAttribute( "nearEndLoopCharge" );
	Double farEndLoopCharge = (Double)request.getAttribute( "farEndLoopCharge" );
	
	Double bandwidthUpgradationCharge = (Double)request.getAttribute( "bandwidthUpgradationCharge" );
	
	Double bandwidthCharge = (Double)request.getAttribute( "bandwidthCharge" );
	
	Double securityCharge = (Double)request.getAttribute( "securityCharge" );
	Double prevBWCharge = (Double)request.getAttribute( "prevBWCharge");
	Double newBWCharge = (Double)request.getAttribute( "newBWCharge");
	
	long vpnLinkID = (Long)request.getAttribute( "vpnLinkID" );
	long clientID = (Long)request.getAttribute( "clientID" );
	
	ClientDTO clientDTO = (ClientDTO)request.getAttribute( "clientDTO" );
	VpnLinkDTO vpnLinkDTO = (VpnLinkDTO)request.getAttribute( "vpnLinkDTO" );
	
	int coreType = (Integer)request.getAttribute("farEndCoreType");
	
	double existingSecurityMoney = prevBWCharge;
	
	double additionalSecurityCharge = securityCharge - existingSecurityMoney;


%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Bandwidth Upgrade</div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
	
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do">
			
			<input id="requestTypeID" name="requestTypeID" value="<%=VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE %>" type="hidden">
			
			<input id="entityTypeID" name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK %>" type="hidden">
			<input id="entityID" name="entityID" value="<%=vpnLinkID %>" type="hidden">
			
			<input id="rootEntityID" name='rootEntityID' type='hidden' value="<%=vpnLinkID %>">
			<input id="rootEntityTypeID" name='rootEntityTypeID' type='hidden'  value="<%=EntityTypeConstant.VPN_LINK %>">
			
			<input id="clientID" name="clientID" value="<%=clientID %>" type="hidden">
			<input id="requestToAccountID" name="requestToAccountID" value="<%=clientID %>" type="hidden">
			
			<input id="description" name="description" value="Demand Note generated (System generated message)" type="hidden" />
			
			<input id="actionName" name="actionName" class="actionName" type="hidden"
				   value="/VpnLinkAction.do?entityID=<%=vpnLinkID %>&entityTypeID=<%=EntityTypeConstant.VPN_LINK %>" />
				   
			<input type="hidden"  placeholder="No of OF core" class="form-control" name="noOfOFCore" value="<%=coreType%>" readonly="readonly" required>
			<input type="hidden"  placeholder="VPN distance (km)" class="form-control" name="vpnDistance" id="vpnDistance" value="" required>
			<input type="hidden"  placeholder="Local loop length Near (m)" class="form-control" name="localLoopNear" id="localLoopNear" value="<%=nearEndLoopDistance %>" required >
			<input type="hidden"  placeholder="Local loop length Far (m)" class="form-control" name="localLoopFar" id="localLoopFar" value="<%=farEndLoopDistance %>" required >
			
			<div class="form-body">
				
			   	<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					
					<div class="col-sm-6">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="<%=clientDTO.getName() %>" required>
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=clientID %>" required>
					</div>
					
					<div class="col-sm-2">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=6&entityID=<%=clientID %>" required> View Client Details </a>
					</div>
					
				</div>
				
			
				<div class="form-group">
				
					<label for="cnName" class="col-sm-3 control-label">Link Name</label>
					
					<div class="col-sm-6">
						<input type="text"  placeholder="Link  Name" class="linkName form-control" name="linkName" value="<%=vpnLinkDTO.getName() %>" required>
						<input type="hidden" class="form-control" name="linkID" value="<%=vpnLinkID %>" required>
					</div>
					
					<div class="col-sm-2">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/VpnLinkAction.do?entityID=<%=vpnLinkDTO.getID() %>&entityTypeID=<%=EntityTypeConstant.VPN_LINK %>">View Link Details</a>
					</div>
					
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>OTC:</b> </span> </div>
				
				<div class="form-group">
					
					<label for="bwUpgradationCharge" class="col-sm-3 control-label">Bandwidth upgradation charge: </label>
					<div class="col-sm-4">
						<input type="number" placeholder="Bandwith Upgradation Charge" value="<%=bandwidthUpgradationCharge%>" class="form-control" name="bwUpgradationCharge" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="securityCharge" class="col-sm-3 control-label">New Security Charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Security Charge" class="form-control" name="securityCharge" id="securityCharge" value="<%=newBWCharge %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="existingSecurityMoney" class="col-sm-3 control-label">Existing Security Money: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Existing Security Money" class="form-control" id='existingSecurityCharge' name="existingSecurityMoney" value="<%=existingSecurityMoney %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="additionalSecurityCharge" class="col-sm-3 control-label">Additional Security Charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Additional Security Charge" class="form-control" id='additionalSecurityCharge' name="additionalSecurityCharge" value="<%=additionalSecurityCharge %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>MRC:</b> </span> </div>
				
		
				<div class="form-group" style="display:none">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Local end loop OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="localEndOFCCharge" id="localEndOFCCharge" 
								value="<%=nearEndLoopCharge %>" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group" style="display:none">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Remote end loop OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="farEndOFCCharge" id="farEndOFCCharge" 
								value="<%=farEndLoopCharge %>" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group" style="display:none">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Total OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="OFCoreCharge" id="OFCoreCharge" 
								value="<%=(nearEndLoopCharge + farEndLoopCharge) %>" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="bwCharge" class="col-sm-3 control-label">Bandwidth charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Bandwith Charge" class="form-control" name="bwCharge" id="bwCharge" 
							value="<%=newBWCharge %>" readonly="readonly" required>
					</div>
				</div>
				
<!-- 				<div class="form-group"> -->
					
<!-- 					<label for="discount" class="col-sm-3 control-label">Discount (Tk): </label> -->
<!-- 					<div class="col-sm-4"> -->
<!-- 						<input type="number" step="1" class="form-control" id="discount" name="discount" value="0"/> -->
<!-- 					</div> -->
<!-- 				</div> -->
				
				
				<!-- alskdfhaksdfhaksdjfh -->
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
				
				<!-- kicchu korar nai :( copy paste korte korte pagol hoye jawa lagbe -->
				<input type="hidden" name="month" value="1" id="month" min="1" type="number" />
				<!-- asldkfhsdakfhkh -->
				
				<!-- asdlkfaldkfhaldksfhaklasdasdffh -->
				
				
			</div>
			
			<div class="form-actions text-center">
			
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl"type="submit" >Submit</button>
				
			</div>
			
		</form>
	</div>
</div>

<script type="text/javascript">
	
	var context = "<%=request.getContextPath() %>";
	var vpnLinkID = "<%=vpnLinkID %>";
<%-- 	var localOFCChargeFull = "<%=Math.round( nearEndOfcChargeFull ) %>"; --%>
<%-- 	var remoteOFCChargeFull = "<%=Math.round( farEndOfcChargeFull ) %>"; --%>
<%-- 	var totalOFCChargeFull = "<%=Math.round( totalOFCoreChargeFull ) %>"; --%>
	
<%-- 	var nearEndNotReused = <%= !vpnLinkDTO.isNearEndReused() && nearEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL %>; --%>
<%-- 	var farEndNotReused = <%= farEndDTO.getParentEndPointID() == 0 && farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL %>; --%>
	
	
</script>
<script src="${context}assets/scripts/vpn/link/generateDemandNoteForBWChange.js" type="text/javascript"></script>
