<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="common.ClientDTO"%>
<%

	int farEndLoopDistance = (Integer)request.getAttribute( "farEndLoopDistance" );
	
	Double farEndLoopCharge = (Double)request.getAttribute( "farEndLoopCharge" );
	
	Double bandwidthUpgradationCharge = (Double)request.getAttribute( "bandwidthUpgradationCharge" );
	
	Double bandwidthCharge = (Double)request.getAttribute( "bandwidthCharge" );
	
	Double securityCharge = (Double)request.getAttribute( "securityCharge" );
	
	long lliLinkID = (Long)request.getAttribute( "lliLinkID" );
	long clientID = (Long)request.getAttribute( "clientID" );
	
	ClientDTO clientDTO = (ClientDTO)request.getAttribute( "clientDTO" );
	LliLinkDTO lliLinkDTO = (LliLinkDTO)request.getAttribute( "lliLinkDTO" );
	
	int coreType = (Integer)request.getAttribute("farEndCoreType");
	
	double existingSecurityMoney = lliLinkDTO.getSecurityMoney();
	
	double additionalSecurityCharge = securityCharge - existingSecurityMoney;


%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Bandwidth Upgrade</div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
	
		<form id="demandNoteForm" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do">
			
			<input id="requestTypeID" name="requestTypeID" value="<%=LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE %>" type="hidden">
			<input id="entityTypeID" name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK %>" type="hidden">
			<input id="entityID" name="entityID" value="<%=lliLinkID %>" type="hidden">
				
			<input id="rootEntityID" name='rootEntityID' type='hidden' value="<%=lliLinkID %>">
			<input id="rootEntityTypeID" name='rootEntityTypeID' type='hidden'  value="<%=EntityTypeConstant.LLI_LINK %>">
			
			<input id="clientID" name="clientID" value="<%=clientID %>" type="hidden">
			<input id="requestToAccountID" name="requestToAccountID" value="<%=clientID %>" type="hidden">
			
			<input id="description" name="description" value="Demand Note generated (System generated message)" type="hidden" />
			
			<input id="actionName" name="actionName" class="actionName" type="hidden"
				   value="/LliLinkAction.do?entityID=<%=lliLinkID %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>" />
				   
			<input type="hidden"  placeholder="No of OF core" class="form-control" name="noOfOFCore" value="<%=coreType%>" readonly="readonly" required>
			<input type="hidden"  placeholder="LLI distance (km)" class="form-control" name="lliDistance" id="lliDistance" value="" required>
			<input type="hidden"  placeholder="Local loop length Far (m)" class="form-control" name="localLoopFar" id="localLoopFar" value="<%=farEndLoopDistance %>" required >
			
			<div class="form-body">
				
			   	<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					
					<div class="col-sm-6">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="<%=clientDTO.getName() %>" required>
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=clientID %>" required>
					</div>
					
					<div class="col-sm-2">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=7&entityID=<%=clientID %>" required> View Client Details </a>
					</div>
					
				</div>
				
			
				<div class="form-group">
				
					<label for="cnName" class="col-sm-3 control-label">Connection Name</label>
					
					<div class="col-sm-6">
						<input type="text"  placeholder="Connection  Name" class="linkName form-control" name="linkName" value="<%=lliLinkDTO.getName() %>" required>
						<input type="hidden" class="form-control" name="linkID" value="<%=lliLinkID %>" required>
					</div>
					
					<div class="col-sm-2">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/LliLinkAction.do?entityID=<%=lliLinkDTO.getID() %>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>">View Link Details</a>
					</div>
					
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>OTC:</b> </span> </div>
				
				<div class="form-group">
					
					<label for="bwUpgradationCharge" class="col-sm-3 control-label">Bandwidth Upgradation charge: </label>
					<div class="col-sm-4">
						<input type="number" placeholder="Bandwidth Upgradation Charge" value="<%=bandwidthUpgradationCharge%>" class="form-control" name="bwUpgradationCharge" id="bwUpgradationCharge" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="securityCharge" class="col-sm-3 control-label">Security Charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Security Charge" class="form-control" name="securityCharge" id="securityCharge" value="<%=securityCharge %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="existingSecurityMoney" class="col-sm-3 control-label">Existing Security Money: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Existing Security Money" class="form-control" name="existingSecurityMoney" id="existingSecurityMoney" value="<%=existingSecurityMoney %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="additionalSecurityCharge" class="col-sm-3 control-label">Additional Security Charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Additional Security Charge" class="form-control" name="additionalSecurityCharge" id="additionalSecurityCharge" value="<%=additionalSecurityCharge %>" required readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group"> <span class="col-sm-3 text-right"> <b>MRC:</b> </span> </div>
				
				<div class="form-group" style="display:none">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label"> OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="farEndOFCCharge" id="farEndOFCCharge" 
								value="0" readonly="readonly" />
					</div>
				</div>
				
				<div class="form-group" style="display:none">
					
					<label for="OFCoreCharge" class="col-sm-3 control-label">Total OFC charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="OF core charge" class="form-control" name="OFCoreCharge" id="OFCoreCharge" 
								value="0" readonly="readonly" required>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="bwCharge" class="col-sm-3 control-label">Bandwidth charge: </label>
					<div class="col-sm-4">
						<input type="number"  placeholder="Bandwith Charge" class="form-control" name="bwCharge" id="bwCharge" 
							value="<%=bandwidthCharge %>" readonly="readonly" required>
					</div>
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
						<input type="number" class="form-control" id="discount" name="discount" value="0"/>
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

<script type="text/javascript">
	
	var context = "<%=request.getContextPath() %>";
	var lliLinkID = "<%=lliLinkID %>";
</script>
<script src="${context}assets/scripts/lli/link/generateDemandNote.js" type="text/javascript"></script>
