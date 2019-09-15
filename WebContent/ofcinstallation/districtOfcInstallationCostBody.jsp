
<%@page import="util.ServiceDAOFactory"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="vpn.ofcinstallation.*"%>


<%
	
	DistrictOfcInstallationService districtOfcInstallationService = (DistrictOfcInstallationService)ServiceDAOFactory.getService(DistrictOfcInstallationService.class);
	List<DistrictOfcInstallationDTO> districtInstallationCostDTOs = districtOfcInstallationService.getAllDistrictOfcInstallationCosts();
	
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-map-marker "></i>LLI OFC Installation Cost
		</div>
	</div>
	
	<!-- Jami -->
	<div id="commonChargeEntryBody" 
		 class="portlet-body form"
		 style="padding:10px !important;">	
		<form id="commonChargeEntryForm"
			  class="form-horizontal" 
			  action="../lli/configuration/add-new-ofc-install-cost.do"
			  method="POST">

            <div class="form-group">
                <label class="col-md-3 control-label">One Time Cost</label>
                <div class="col-md-2">
                    <input type="number" min="0" id="oneTimeCost" name="oneTimeCost" class="form-control">
                </div>

                <div class="col-md-3"><span>Taka</span></div>
            </div>
			  
			<div class="form-group">
				
				<label class="col-md-3 control-label">Common Charge
				</label>
				
				<div class="col-md-2">
					<input type="number"
						   id="commonChargeCost"
	                       name="commonChargeCost"
						   class=" form-control" 
						   step="any"/>
				</div>
				
				<div class="col-md-3">
					<span>Taka</span>
				</div>
					
			</div>
			
			<div class="form-group">
				
				<label class="col-md-3 control-label" >For First
				</label>
				
				<div class="col-md-2">
					<input type="number" 
						   id="perMeter"
							name="perMeter"
						   class=" form-control" 
						   step="any"/>
				</div>
				
				<div class="col-md-3">
					<span>Meter</span>
				</div>
			</div>
				
			<div class="form-group">
				
				<label class="col-sm-3 control-label">From Date<!-- <span class="required" aria-required="true">*</span> -->
				</label>
				
				<div class="col-sm-2">
					<input type="text"
							id="updateDate"
						   name="dateOfUpdate" 
						   class="form-control datepicker">
				</div>			
				
				<div class="col-sm-3">
					<button class="btn btn-submit-btcl" 
							type="submit" 
							name="saveButton">Save
					</button>
				</div>		
			</div>
		</form>
	</div>
				<!-- jami -->
	<div class="portlet-body form">
		<form id="districtOfcInstallationCostUpdateForm" 
			  action="../DistrictOfcInstallation/UpdateDistrictOfcInstallationCost.do"
			  method="post"
			  class="form-horizontal">
			<div class="form-body">
				<!-- <div class="form-group">
					<label class="col-md-3 control-label" >Common Change</label>
					<div class="col-md-6">
						<input type="number" id="commonChangeCost" class=" form-control" step="any"/>
					</div>
				</div> -->
			
			</div>
			<div class="form-body">
				<div class="row">
					<%
						if (districtInstallationCostDTOs != null)
							for (int i = 0; i < districtInstallationCostDTOs.size(); i++) {
					%>
					<div class="col-xs-6 col-sm-4 col-md-4 col-lg-3 pull-left"
						style="padding: 5px">
						<div class="col-md-6">
							<label class="control-label col-md-12"><%=districtInstallationCostDTOs.get(i).getDistrictName()%>
							</label>
						</div>
						<div class="col-md-6">
						 <input id="<%=districtInstallationCostDTOs.get(i).getId()%>"
								class="form-control installation-cost col-md-12" 
								type="number" 
								step="any"
								name="installationcost_in_<%=districtInstallationCostDTOs.get(i).getDistrictID()%>"
								value="<%=districtInstallationCostDTOs.get(i).getInstallationCost()%>">
						</div>
					</div>
					<%
						}
					%>
				</div>
			</div>
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-md-offset-4 col-md-8">
						<a class="btn btn-cancel-btcl" 
						   type="button"
							href="<%=request.getHeader("referer")%>">Back</a>
						<button class="btn btn-submit-btcl" 
								type="submit" 
								name="B2">Update
						</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">

	function refreshView(data){
		var array=data.payload;
		$.each(array, function(index, value){
			var name = "installationcost_in_"+value.districtID;
			var cost = value.installationCost;
			$("input[name='installationcost_in_"+value.districtID+"']").val(cost);
		});

	}

	function ResponseCallback(data) {
		console.log(data);
		if (data['responseCode'] == 1) {
			toastr.success(data['msg']);
		} else {
			toastr.error(data['msg']);
		}
	}

	$(document).ready(function() {
	    getCommonCost();

    	$("#districtOfcInstallationCostUpdateForm").on("submit", function(event) {
			event.preventDefault();
			var url = $(this).attr('action');
			var param = $(this).serialize();

			callAjax(url, param, ResponseCallback, "POST");
		});


		$("#commonChargeEntryForm").on("submit", function(event) {
			event.preventDefault();
			var url = $(this).attr('action');
			var param = $(this).serialize();
			console.log(param);
			var otc = $("#oneTimeCost").val();
			param['oneTimeCost']  = otc;
			callAjax(url, param, ResponseCallback, "POST");
		});


		$("#commonChangeCost").on("keyup", function(){
			if($(this).val()==""){
				callAjax("../DistrictOfcInstallation/RefreshInstallationCosts.do", {}, refreshView, "GET");
			}
			else{
				$('.installation-cost').each(function(){
					$(this).val($("#commonChangeCost").val());
				})
			}
		});

	});

	function getCommonCost() {
	    var url = "../lli/configuration/get-latest-cost-by-date.do";
	    var currentTime = new Date().getTime();
	    callAjax(url, {date: currentTime}, getCommonCostCallback, 'GET');
    }

    function getCommonCostCallback(data) {
        var cost=data.payload;
        if (cost == null){
            return;
        }
        var curDate = new Date(cost.applicableFrom);
        var date = curDate.getDate();
        var month = curDate.getMonth()+1;
        var year = curDate.getFullYear();
        var oneTimeCost = cost.oneTimeCost;
        console.log(date);
        $("#commonChargeCost").val(cost.fiberCost);
        $("#oneTimeCost").val(oneTimeCost);
        $("#perMeter").val(cost.fiberLength);
        $("#updateDate").val(date+'/'+month+'/'+year);
    }
</script>










