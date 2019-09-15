<%@page import="java.util.*, vpnCommonChargeConfig.*,java.text.*"%>
<%
	CommonChargeDTO dto = (CommonChargeDTO) request.getAttribute("latestDTO");
	CommonChargeDTO active = (CommonChargeDTO) request.getAttribute( "activeCharge" );
	List<CommonChargeDTO> allCharges = (List<CommonChargeDTO>) request.getAttribute("allCharges");
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-cogs"></i> Update Common Charge
		</div>
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
		
		<form role="form" action="" class="form-horizontal" mehtod="POST">	
			
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<h3 class="col-sm-offset-4" style="padding-left:15px"> Active Charge </h3>
					</div>
				</div>
				
				<div class="col-sm-offset-4" style="padding-left:15px"><h3>Activation Date : <%=active.getActivationDateStr() %></h3></div>
				
				<div class="form-group">
					
					<label for="chargePerPoint" class="col-sm-4  control-label">Bandwidth Connection Charge</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=active.getChargePerPoint() %>" readonly="readonly">
					</div>
					
				</div>
				
				<div class="form-group">
					<label for="ratioL3L2" class="col-sm-4  control-label">Percentage of Extra charge for Layer 3 (%)</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=active.getRatioL3L2() %>" readonly="readonly">
					</div>
				</div>
				
				<div class="form-group">
				
					<label for="chargePerPoint" class="col-sm-4  control-label"> Optical Fiber installation charge </label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=active.getOpticalFiberInstallationCharge() %>" readonly="readonly">
					</div>
					
				</div>
					
				<div class="form-group">
					
					<label for="chargePerPoint" class="col-sm-4  control-label"> OF Charge per meter </label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=active.getOFChargePerMeter() %>" readonly="readonly">
					</div>
					
				</div>
				
				<div class="form-group">
					<label for="shiftingCharge" class="col-sm-4  control-label">Shifting Charge</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value=<%=active.getShiftingCharge()%> readonly="readonly">
					</div>
				</div>
	
	
				<div class="form-group">
					<label for="upgradationCharge" class="col-sm-4  control-label">Upgradation Charge</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value=<%=active.getUpgradationCharge()%> readonly="readonly">
					</div>
				</div>
			</div>
		
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<h3> Charges that will be activated in future </h3>
					</div>
				</div>
			<% for( CommonChargeDTO commonCharge: allCharges ){ %>
				
				<div class="col-sm-offset-4" style="padding-left:15px"><h3>Activation Date : <%=commonCharge.getActivationDateStr() %></h3></div>
				
				<div class="form-group">
					
					<label for="chargePerPoint" class="col-sm-4  control-label">Bandwidth Connection Charge</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=commonCharge.getChargePerPoint() %>" readonly="readonly">
					</div>
					
				</div>
				
				<div class="form-group">
					<label for="ratioL3L2" class="col-sm-4  control-label">Percentage of Extra charge for Layer 3 (%)</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=commonCharge.getRatioL3L2() %>" readonly="readonly">
					</div>
				</div>
				
				<div class="form-group">
				
					<label for="chargePerPoint" class="col-sm-4  control-label"> Optical Fiber installation charge </label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=commonCharge.getOpticalFiberInstallationCharge() %>" readonly="readonly">
					</div>
					
				</div>
					
				<div class="form-group">
					
					<label for="chargePerPoint" class="col-sm-4  control-label"> OF Charge per meter </label>
					<div class="col-sm-3">
						<input type="text" class="form-control" value="<%=commonCharge.getOFChargePerMeter() %>" readonly="readonly">
					</div>
					
				</div>
				
				<div class="form-group">
					<label for="shiftingCharge" class="col-sm-4  control-label">Shifting Charge</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value=<%=commonCharge.getShiftingCharge()%> readonly="readonly">
					</div>
				</div>
	
	
				<div class="form-group">
					<label for="upgradationCharge" class="col-sm-4  control-label">Upgradation Charge</label>
	
					<div class="col-sm-3">
						<input type="text" class="form-control" value=<%=commonCharge.getUpgradationCharge()%> readonly="readonly">
					</div>
				</div>
			<%} %>
			</div>
		</form>

		<form id="newCostForm" role="form" action="../UpdateCommonChargeConfig.do" class="form-horizontal" mehtod="POST">

			<div class="form-body">

				<div class="form-group">

					<label for="chargePerPoint" class="col-sm-4  control-label">Bandwidth
						Connection Charge</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="chargePerPoint"
							name="chargePerPoint" placeholder="Bandwidth Connection Charge"
							required>
					</div>

				</div>

				<div class="form-group">
					<label for="ratioL3L2" class="col-sm-4  control-label">Percentage
						of Extra charge for Layer 3 (%)</label>

					<div class="col-sm-3">
						<input type="text" class="form-control" id="ratioL3L2"
							name="ratioL3L2"
							placeholder="Percentage of Extra charge for Layer 3" required>
					</div>
				</div>

				<div class="form-group">

					<label for="chargePerPoint" class="col-sm-4  control-label">
						Optical Fiber installation charge </label>
					<div class="col-sm-3">
						<input type="text" class="form-control"
							id="opticalFiberInstallationCharge"
							name="opticalFiberInstallationCharge"
							placeholder="Optical Fiber installation charge" required>
					</div>

				</div>

				<div class="form-group">

					<label for="chargePerPoint" class="col-sm-4  control-label">
						OF Charge per meter </label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="OFChargePerMeter"
							name="OFChargePerMeter" placeholder="OF Charge per meter"
							required>
					</div>

				</div>

				<div class="form-group">
					<label for="shiftingCharge" class="col-sm-4  control-label">Shifting
						Charge</label>

					<div class="col-sm-3">
						<input type="text" class="form-control" id="shiftingCharge"
							name="shiftingCharge" placeholder="Give shifting charge" required>
					</div>
				</div>

				<div class="form-group">
					<label for="upgradationCharge" class="col-sm-4  control-label">Upgradation
						Charge</label>

					<div class="col-sm-3">
						<input type="text" class="form-control" id="upgradationCharge"
							name="upgradationCharge" placeholder="Give upgradation charge"
							required>
					</div>
				</div>
				

				<div class="form-group">
					<label for="activationDate" class="col-sm-4  control-label">Activation Date</label>
					<div class="input-group date col-sm-3">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input type="text" name="activationDate" class="form-control pull-right datepicker">
					</div>
				</div>
				
			</div>

			<div class="form-actions">
				<div class="row">
					<div class="col-md-offset-4 col-md-8">
						<input type="reset" class="btn btn-btcl btn-reset-btcl" value="Reset">
						<input type="submit" class="btn btn-btcl btn-submit-btcl" value="Update Configuration">
					</div>
				</div>
			</div>
		</form>
		
		<div style="width:100%; text-align: center;">
			<button type="button" class="btn btn-submit-btcl" id="newCostBtn">Configure New Cost</button>
		</div>
	</div>
</div>
<script>
    $(document).ready(function() {
		
    	$("#newCostForm").hide();
    	
		$('.datepicker').datepicker({
             orientation: "bottom",
             format: 'dd/mm/yyyy',
             autoclose: true
	    });
		
		$("#newCostBtn").on( "click", function(){
			
			$("#newCostForm").show();
			$(this).hide();
		});
    });
</script>