<%@page import="org.apache.log4j.Logger"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="java.util.Set"%>
<%@page import="request.RequestUtilService"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestUtilDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.link.request.ipaddress.LliLinkIpaddressService"%>
<%@page import="lli.link.request.ipaddress.LliLinkIpaddressRequestDTO"%>
<%
Logger loggerIP = Logger.getLogger("logger_ip");
LliLinkIpaddressService lliLinkIpaddressService = ServiceDAOFactory.getService(LliLinkIpaddressService.class);
LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);

long entityID = Long.parseLong(request.getParameter("entityID"));
int entityTypeID = Integer.parseInt(request.getParameter("entityTypeID"));


LliLinkIpaddressRequestDTO lliLinkIpaddressRequestDTO = (LliLinkIpaddressRequestDTO)lliLinkIpaddressService.getRequestDTOByEntity(entityID, entityTypeID);
LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID(lliLinkIpaddressRequestDTO.getLinkID());

loggerIP.debug("lliLinkDTO " + lliLinkDTO);
String back  = "../../LliLinkAction.do?entityID=" + entityID+"&entityTypeID="+EntityTypeConstant.LLI_LINK;
boolean ipEditable = true;
%>

<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>IP Address Internal FR Response Details
		</div>
		<div class="actions">
			<a  href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px;"> Back </a>
		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<form  id="fileupload" class="form-horizontal" method="post" action="../../CommonAction.do?mode=internalFrResponse" >
<!-- <portlet-div data-title="LLI Link Additional IP Address Internal Feasibility Report" data-title-icon="fa fa-plus" data-action="../../CommonAction.do?mode=internalFrResponse"> -->
			<input type='hidden' name='requestTypeID' value="<%=request.getParameter("requestTypeID")%>"> 
			<input type='hidden' name='entityTypeID' value="<%=request.getParameter("entityTypeID")%>">
			<input type='hidden' name='entityID' value="<%=request.getParameter("entityID")%>">
			<input type="hidden" name="clientID" value="<%=lliLinkDTO.getClientID()%>" >
			<input type='hidden' name='requestToAccountID' value="<%=request.getParameter("requestTo")%>">
			<input type='hidden' name='rootEntityTypeID' value="<%=request.getParameter("entityTypeID")%>">
			<input type='hidden' name='rootEntityID' value="<%=request.getParameter("entityID")%>">			
			<input name="actionName" value="LliLinkAction.do?entityID=<%=request.getParameter("entityID")%>&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>" class="actionName" type="hidden">
	<div class="row">
		<div class="col-md-12">
			<div class="table-responsive">
				<table class="table table-bordered  table-striped">
					<thead>
						<tr>
							<th colspan="3" class="text-center">
								<h1> Link Details </h1>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">Client Name</th>
							<td colspan="2"><%=AllClientRepository.getInstance().getClientByClientID(lliLinkDTO.getClientID()).getLoginName()%></td>
						</tr>
						<tr>
							<th scope="row">Connection Name</th>
							<td colspan="2"><%=lliLinkDTO.getName()%></td>
						</tr>
						<tr>
							<th>Additional IP Address Count</th>
							<td colspan=2>
							<%=lliLinkIpaddressRequestDTO.getNewAdditionalRequestedIpCount()==null ?'-':
								lliLinkIpaddressRequestDTO.getNewAdditionalRequestedIpCount()%>
							</td>
						</tr>
						<tr style="display:none">
							<th>Availability</th>
							<td>
								<div class="radio-list" style="display: inline-block;">
									<label class="radio-inline"> 
										<span> <input type="radio" name="bandwidthAvailablity" value="0" > </span>  Not Available
									</label>
									<label class="radio-inline"> 
										<span> <input type="radio" name="bandwidthAvailablity" value="1"  checked  ></span>  Available
									</label>
								</div>
							</td>
						</tr>
						<tr id="bandWidthComment" style="display:none">
							<th scope="row">Bandwidth Non-Availability Reason<span style="color:red" aria-required="true"> * </span></th>
							<td>
								<div class=row>
									<div class="col-sm-6">
										<textarea class="form-control" rows="3" name="bandWidthComment"
											placeholder="Comments..."></textarea>
									</div>
								</div>
							</td>
						</tr>
						
						
						
						
						
						<tr id="additionalIpAvailableCount" style="display:none">
							<th scope="row">Available Additional IP Address Count<span style="color:red" aria-required="true"> * </span></th>
							<td>
								<%if(ipEditable){%>
								<div class=row>
									<div class=col-sm-6>
										<select class="form-control inside-table" name="additionalIpAvailableCount">
											<option value="0">0</option>
											<option value="2">2</option>
											<option value="4">4</option>
											<option value="8">8</option>
											<option value="16">16</option>
											<option value="32">32</option>
											<option value="64">64</option>
											<option value="128">128</option>
											<option value="256">256</option>
										</select>	
									</div>
								</div>
								
								<%}else{%>
								-
								<%} %>
							</td>
						</tr>
						<tr id="additionalIpUnavailabilityReason" style="display:none">
							<th scope="row">Additional IP Address Unavailability Reason<span style="color:red" aria-required="true"> * </span></th>
							<td>
								<%if(ipEditable){%>
								<div class=row>
									<div class="col-sm-6">
										<textarea class="form-control inside-table" rows="3" id='reason' name="additionalIpUnavailabilityReason"
										placeholder="Comments..."></textarea>
									</div>
								</div>
								<%}else{%>
								-
								<%} %>
							</td>
						</tr>
						
						<tr class=additionalIpAvailableTR style=display:none>
							<th scope="row">Available Additional IP Blocks<span style="color:red" aria-required="true"> * </span></th>
							<td>
							<%if(ipEditable){%>
							<div class=row>
								<div class=col-sm-6>
									<table class='table table-striped' style='text-align:center'>
										
										<tbody>
											<tr>
												<td><input class=form-control name=starting-ip-additional></td>
												<td style=text-align:center><span>to</span>
												<td><input class=form-control name=ending-ip-additional></td>
											</tr>
											<tr>
												<td style=text-align:left>
													<button class='btn btn-primary btn-ip-add' data-id=additional>Add</button>
													<button class='btn btn-danger btn-ip-remove' data-id=additional>Remove</button>
												</td>
											</tr>
										</tbody>
									</table>
								
								</div>
							</div>
								
							<%} else {%>
							-
							<%}%>
							</td>
						</tr>
						<tr id="additionalIpAvailableBlockID" style="display:none">
							<th scope="row">Available Additional IP Blocks<span style="color:red" aria-required="true"> * </span></th>
							<td>
							<%if(ipEditable){%>
							<div class=row>
								<div class=col-sm-6>
									<select  style="width:100%;border:1px solid #c2cad8;padding: 6px 12px;" class=selectIP name="additionalIpAvailableBlockID" multiple>
									
									</select>
								</div>
							</div>
								
								<%}else{%>
								-
								<%} %>
							</td>
						</tr>
						
						<tr>
							<th scope="row">Description</th>
							<td>${lliLink.linkDescription}</td>
						</tr>
					</tbody>
				</table>
			</div>	
		</div>
	</div>
	
	
	<input type=hidden name=id value=<%=lliLinkIpaddressRequestDTO.getId()%>>
	
<!-- </portlet-div> -->
			<div class="form-actions text-center">
					<%-- <input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
					<input type="hidden" name="divisionID" value="<%=divisionID%>" /> --%>
					<button class="btn btn-reset-btcl" type="reset">Cancel</button>
					<input id="updateBtn" class="btn btn-submit-btcl" name="action" type="submit" value="Submit"/>
			</div>
</form>
</div>
</div>

<script>
	$(document).ready(function(){
		$("input[name=bandwidthAvailablity]").click(function(){
			if($(this).val()=='0'){
				$('#bandWidthComment').show();
			}else{
				$('#bandWidthComment').hide();
			}
		})
		
		$("#isAvailable").trigger("change");
		
		function getIpBlockCallBack(data){
			$("select[name=newAdditionalIpAddressBlockID]").html("<option>Select Range</option>");
			$.each(data, function(index, value){
				$("select[name=newAdditionalIpAddressBlockID]").append(("<option value='"+value.blockID+"'>"+value.from+" - "+value.to+"</option>"));
			});
		}
		$("select[name=newAdditionalFeasibleIpCount]").change(function(){
			formData = {};
			formData.divisionID = 0;
			formData.essentialBlockSize = $(this).val();
			ajax(context+"lli/inventory/ipAddress/getSuggestionForEssentialIpBlock.do", formData, getIpBlockCallBack, "GET", [$(this)]);
		});
		
		
		if ('${lliLink.additionalIPCount}' != '0'){
			$("#additionalIpAvailableCount").val(0);
			$("#additionalIpAvailableCount").show();
			$("#additionalIpUnavailabilityReason").show();
			

			$("select[name=additionalIpAvailableCount]").change(function(){
				if($("select[name=additionalIpAvailableCount]").val()=='0'){
					$("#additionalIpUnavailabilityReason").show();
					$("#additionalIpAvailableBlockID").hide();
					$(".additionalIpAvailableTR").hide();
				}else{
					
					$("#additionalIpUnavailabilityReason").hide();
					$("#additionalIpAvailableBlockID").show();
					$(".additionalIpAvailableTR").show();
				}
			});
			
			$("#mandatoryIpAvailableBlockID td select").change(function(){
				$("select[name=additionalIpAvailableCount]").trigger("change");
			});
		}else{
			$("#isAdditionalEmpty").val(0);
		}
	});
	
	$("#fileupload").on('submit', function(){
		selectAllOptions();	
		
		if($("select[name=additionalIpAvailableCount]").val()=='0'){
			if($('#reason').val().length==0){
				toastr.error("Give unavailability reason");
				$('#reason').closest('div').addClass('has-error');
				return false;		
			}
		}
		return true;
	});
	function selectAllOptions() {
		var form = document.forms[0]; 
	  	var select2 = form[name="additionalIpAvailableBlockID"];
	  	if(select2) {
		  	var len2 = select2.length;
		  	for(i = 0; i < len2; i++) {
		     	select2.options[i].selected = true;
		   	}
	  	}
	   	return true;
	}
	$('.btn-ip-add').on('click', function () {
		
		var select,selectCount, startingInput, endingInput, startingIP, endingIP, ipFromTo, dataID;
		dataID = $(this).attr('data-id');
		startingInput = $('input[name=starting-ip-'+dataID+ ']');
		endingInput = $('input[name=ending-ip-' + dataID+']');
		startingIP = $(startingInput).val();
		endingIP = $(endingInput).val();
		octetsOfStart = startingIP.split(".");
		octetsOfEnd = endingIP.split(".");
		
		if(parseInt(octetsOfStart[3]) % 4 !== 0 ) {
			toastr.error("Starting IP must be start with multiple of 4 in the last octet");
			return false;
		}
		ipFromTo = startingIP + "-" + endingIP;
		if(dataID === 'essential'){
			select = 'mandatoryIpAvailableBlockID';
			selectCount = 'mandatoryIpAvailableCount';
		}else if(dataID === 'additional'){
			select = 'additionalIpAvailableBlockID';
			selectCount = 'additionalIpAvailableCount';
		}
		
		if($('select[name="' + select + '"] option').length < parseInt($('select[name=' + selectCount +']').val())){
			$('select[name="'+select+'"]').append($('<option>', {
			    value: ipFromTo,
				text: ipFromTo
			}) );
			$(startingInput).val('');
			$(endingInput).val('');	
		}else {
			toastr.error("count of ip blocks can not be more than the requested.");
		}
		
		return false;
	});
	
	$('.btn-ip-remove').on('click', function (){
		var select, dataID, selectedIdx, form;
		dataID = $(this).attr('data-id');
		form = document.forms[0];
		if(dataID === 'essential') {
			select = 'mandatoryIpAvailableBlockID';
		}else if(dataID === 'additional'){
			select = 'additionalIpAvailableBlockID';
		}
		selectedIdx = form[name=select].selectedIndex;
		if(selectedIdx != -1 ) {
			form[name=select].options.remove(selectedIdx);
		}
		return false;
	});
	
</script>