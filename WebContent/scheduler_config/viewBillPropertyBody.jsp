<%@page import="vpn.VpnMonthlyBillProperty"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="common.UniversalDTOService"%>
<%@page import="util.TimeConverter"%>
<link href="../assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="../assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />
<link href="../assets/global/plugins/clockface/css/clockface.css" rel="stylesheet" type="text/css" />
<%
	UniversalDTOService vpnMonthDtoService = ServiceDAOFactory.getService(UniversalDTOService.class);
	VpnMonthlyBillProperty vpnMonthlyBillProperty  = vpnMonthDtoService.get(VpnMonthlyBillProperty.class);
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
		<div class="actions">
			<button class="btn btn-default" id="edit">Edit</button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal" id="tableForm" method="POST" action="../SchedulerVpn/updateBillProperty.do">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">From</label>
					<div class="col-sm-6">
						<input type="text" class="form-control toRemove"
						name="fromTime" value="<%=TimeConverter.getMeridiemTime(vpnMonthlyBillProperty.getFromDate())%>">
						<div class="input-group date form_datetime toShow" style="display:none">
							<input type="text" class="form-control" name="fromDate">
							
							<span class="input-group-btn">
								<button class="btn default date-set" type="button">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>	
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">To</label>
					<div class="col-sm-6">
						<input type="text" class="form-control toRemove"
						name="toTime" value="<%=TimeConverter.getMeridiemTime(vpnMonthlyBillProperty.getToDate())%>">
						
						<div class="input-group date form_datetime toShow" style="display:none">
							<input type="text" class="form-control" name="toDate">
							<span class="input-group-btn">
								<button class="btn default date-set" type="button">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>
					</div>
				</div>
			</div>
			<div class="form-actions text-center toShow">
				<button class="btn btn-submit-btcl" type="submit" id="submitbtn">Submit</button>
			</div>
			
		</form>
	</div>
</div>
<script>
	$(document).ready(function(){
		$('input').prop('readonly', true);
		
		$('#edit').click(function(){
			$('input').prop('readonly', false);
			$('.toShow').show();
			$('.toRemove').remove();
			return false;
		});
		
		
	});
	
</script>
<script src="../assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/clockface/js/clockface.js" type="text/javascript"></script>
<script src="../assets/global/scripts/app.min.js" type="text/javascript"></script>
<script src="../assets/pages/scripts/components-date-time-pickers.min.js" type="text/javascript"></script>
