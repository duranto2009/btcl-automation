<%@page import="util.ServiceDAOFactory"%>
<%@page import="websecuritylog.WebSecurityLogService"%>
<%@page import="websecuritylog.WebSecurityLogDTO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-map-marker "></i>Web Security Log Summary
		</div>
	</div>
	<div class="portlet-body form">
		
		<!-- User -->
		<div class="row">
			<div class="col-md-6">
				<h4 style="font-weight: bold; margin-left: 10px">User Wise</h4>
			</div>
		</div>
		<div class="row" style="margin-bottom: 10px;">
			<div class="col-md-6">
				<div class="col-md-6">
					<select class="form-control" id="user-table-attempt-type">
							<option value="0" selected="selected">All Attempt</option>
							<option value="1">Invalid Login</option>
							<option value="2">CSRF</option>
							<option value="3">Referer</option>
						</select>
				</div>
				<div class="col-md-6">
					<select class="form-control" id="user-table-attempt-time">
							<option value="0" selected="selected">Today</option>
							<option value="1">Since Yesterday</option>
							<option value="7">Last 7 Days</option>
							<option value="30">Last 30 Days</option>
							<option value="365">Last Year</option>
							<option value="-1">Custom</option>
						</select>
				</div>
			</div>
			<div class="col-md-6">
				<div class="col-md-12" style="padding-right: 10%">
					<div id="user-custom-datepicker" style="display:none">
						<div class="col-md-5">
							<input type="text" id="user-custom-datepicker-start" class="datepicker form-control" placeholder="Starting From" required>
						</div>
						<div class="col-md-5">
							<input type="text" id="user-custom-datepicker-end" class="datepicker form-control" placeholder="Ending In" required>
						</div>
						<div class="col-md-2">
							<button id="user-custom-datepicker-submit" class="btn btn-default">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<!-- table -->
			<div class="col-md-6">
				<div class="row">
<!-- 					<h4 class="text-center">Counter</h4> -->
				</div>
					<div class="col-md-12">
						<div class="table-responsive">
							<table id="user-table" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th class="text-center">User Name</th>
										<th class="text-center">Count</th>
										<th class="text-center">Last Attempt Time</th>
									</tr>
								</thead>
								<tbody>
									<tr><td colspan='3' class='text-center'>Loading...</td></tr>
								</tbody>
							</table>
						</div>
				</div>
			<!-- charts -->
			
			</div>
			<div class="col-md-6">
<!-- 				<h4 class="text-center">Chart</h4> -->
				<div style="width:100%" id='user-chart'></div>
			</div>
		</div>
		
		<!-- IP -->
		<div class="row">
			<div class="col-md-6">
				<h4 style="font-weight: bold; margin-left: 10px">IP Wise</h4>
			</div>
		</div>
		<div class="row" style="margin-bottom: 10px;">
			<div class="col-md-6">
				<div class="col-md-6">
					<select class="form-control" id="ip-table-attempt-type">
							<option value="0" selected="selected">All Attempt</option>
							<option value="1">Invalid Login</option>
							<option value="2">CSRF</option>
							<option value="3">Referer</option>
						</select>
				</div>
				<div class="col-md-6">
					<select class="form-control" id="ip-table-attempt-time">
							<option value="0" selected="selected">Today</option>
							<option value="1">Since Yesterday</option>
							<option value="7">Last 7 Days</option>
							<option value="30">Last 30 Days</option>
							<option value="365">Last Year</option>
							<option value="-1">Custom</option>
						</select>
				</div>
			</div>
			<div class="col-md-6">
				<div class="col-md-12" style="padding-right: 10%">
					<div id="ip-custom-datepicker" style="display:none">
						<div class="col-md-5">
							<input type="text" id="ip-custom-datepicker-start" class="datepicker form-control" placeholder="Starting From">
						</div>
						<div class="col-md-5">
							<input type="text" id="ip-custom-datepicker-end" class="datepicker form-control" placeholder="Ending In">
						</div>
						<div class="col-md-2">
							<button id="ip-custom-datepicker-submit" class="btn btn-default">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<!-- table -->
			<div class="col-md-6">
				<div class="row">
<!-- 					<h4 class="text-center">Counter</h4> -->
				</div>
					<div class="col-md-12">
						<div class="table-responsive">
							<table id="ip-table" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th class="text-center">IP Address</th>
										<th class="text-center">Count</th>
										<th class="text-center">Location</th>
										<th class="text-center">Last Attempt Time</th>
									</tr>
								</thead>
								<tbody>
									<tr><td colspan='3' class='text-center'>Loading...</td></tr>
								</tbody>
							</table>
						</div>
				</div>
			<!-- charts -->
			
			</div>
			<div class="col-md-6">
<!-- 				<h4 class="text-center">Chart</h4> -->
				<div style="width:100%" id='ip-chart'></div>
			</div>
		</div>
		
		
	</div>
</div>


<script src="${pluginsContext}highcharts/highcharts.js" type="text/javascript"></script>
<script src="${pluginsContext}highcharts/data.js" type="text/javascript"></script>
<script src="${pluginsContext}highcharts/exporting.js" type="text/javascript"></script>

<script>
function drawUserChart(){
	Highcharts.chart('user-chart', {
		exporting: {
	        buttons: {
	            contextButton: {
	                enabled: false
	            }    
	        }
	    },
	    credits: false,
	    legend: false,
		data: {
	        table: 'user-table',
	        endColumn:1,
	        switchRowsAndColumns: true
	    },
	    chart: {
	        type: 'column'
	    },
	    title: {
	        text: null
	    },
	    yAxis: {
	        allowDecimals: false,
	        title: {
	            text: 'Attempt Count by User'
	        }
	    },
	    tooltip: {
	        formatter: function () {
	            return '<b>' + this.series.name + '</b><br/>' +
	                this.point.y + ' ' + this.point.name.toLowerCase();
	        }
	    }
	});
}

function drawIpChart(){
	Highcharts.chart('ip-chart', {
		exporting: {
	        buttons: {
	            contextButton: {
	                enabled: false
	            }    
	        }
	    },
	    credits: false,
	    legend: false,
		data: {
	        table: 'ip-table',
	        endColumn:1,
	        switchRowsAndColumns: true
	    },
	    chart: {
	        type: 'column'
	    },
	    title: {
	        text: null
	    },
	    yAxis: {
	        allowDecimals: false,
	        title: {
	            text: 'Attempt Count by IP'
	        }
	    },
	    tooltip: {
	        formatter: function () {
	            return '<b>' + this.series.name + '</b><br/>' +
	                this.point.y + ' ' + this.point.name.toLowerCase();
	        }
	    }
	});
}



$(document).ready(function() {
	
	function refreshUserCounts(data){
		var response="";
		if(data.length<1){
			response+="<tr><td colspan='3' class='text-center'>No Data</td></tr>";
		}
		else{
			$.each(data ,function(index, item){
				response+="<tr><td>"+item.username+ "</td><td>"+item.count+"</td><td>"+item.lastAttemptTime+"</td></tr>";
			})
		}
		$("#user-table tbody").html(response);
		drawUserChart();
	}
	

	function refreshIpCounts(data){
		$("#ip-table tbody").html("");
		var response="";
		
		if(data.length<1){
			response+="<tr><td colspan='3' class='text-center'>No Data</td></tr>";
		}
		else{
			var length=data.length;
			
			$.each(data ,function(index, item){
	
				/*$.get("http://www.geoplugin.net/json.gp?ip="+item.ipAddress+"&"+"callback=?", function(result){
				   //response data are now in the result variable
				   alert(result);
				});*/
				
				 $.getJSON('https://ipapi.co/'+item.ipAddress+'/json', function(data){
					 var  row="<tr><td>"+item.ipAddress+"</td><td>"+item.count+"</td><td>"+data.city +", "+ data.country_name+"</td><td>"+item.lastAttemptTime+"</td></tr>";
					 $("#ip-table tbody").append(row);
					 
					 if(index==(length-1)){ // total 20 data
						 drawIpChart();
					 }
				 });
				
			})
		}
		//$("#ip-table tbody").html(response);
		 //$("#ip-table tbody").html(response);
	}
	
	var url=context+'GetAllAttempts.do';
	var formUserData={};
	var formIpData={};
	
	$(document).on("click", "#user-custom-datepicker-submit", function(){
		formUserData.start = $("#user-custom-datepicker-start").val();
		formUserData.end = $("#user-custom-datepicker-end").val();
		callAjax(url,formUserData,refreshUserCounts, "GET");
		$("#user-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
	});
	
	$(document).on("click", "#ip-custom-datepicker-submit", function(){
		formIpData.start = $("#ip-custom-datepicker-start").val();
		formIpData.end = $("#ip-custom-datepicker-end").val();
		callAjax(url,formIpData,refreshIpCounts, "GET");
		$("#ip-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
	});
	
	$("#user-table-attempt-time").on("change", function(){
		formUserData.attemptCategory=$("#user-table-attempt-type").val();
		
		$("#user-custom-datepicker-start").val("");
		$("#user-custom-datepicker-end").val("");
		
		if($(this).val()!='-1'){
			$("#user-custom-datepicker").css("display", "none");
			formUserData.start=$("#user-table-attempt-time").val();
			formUserData.end='-1';
			callAjax(url,formUserData,refreshUserCounts, "GET");
			$("#user-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
		}else{
			$("#user-custom-datepicker").css("display", "block");
		}
		
	});
	$("#user-table-attempt-type").on("change", function(){
		formUserData.attemptCategory=$(this).val();
		formUserData.start=$("#user-table-attempt-time").val();
		callAjax(url,formUserData,refreshUserCounts, "GET");
		$("#user-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
	});
	
	$("#ip-table-attempt-time").on("change", function(){
		formIpData.attemptCategory=$("#ip-table-attempt-type").val();
		
		$("#ip-custom-datepicker-start").val("");
		$("#ip-custom-datepicker-end").val("");
		
		if($(this).val()!='-1'){
			$("#ip-custom-datepicker").css("display", "none");
			formIpData.start=$("#ip-table-attempt-time").val();
			formIpData.end='-1';
			callAjax(url,formIpData,refreshIpCounts, "GET");
			$("#ip-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
		}else{
			$("#ip-custom-datepicker").css("display", "block");
		}
		
	});
	$("#ip-table-attempt-type").on("change", function(){
		formIpData.attemptCategory=$(this).val();
		formIpData.start=$("#ip-table-attempt-time").val();
		callAjax(url,formIpData,refreshIpCounts, "GET");
		$("#ip-table tbody").html("<tr><td colspan='3' class='text-center'>Loading...</td></tr>");
	});
	
	function init(){
		formUserData.attemptCategory=0;
		formUserData.start=0;
		formUserData.end='-1';
		formUserData.mode="user";
		formIpData.attemptCategory=0;
		formIpData.start=0;
		formIpData.end='-1';
		formIpData.mode="ip";
		
		callAjax(url,formUserData, refreshUserCounts, "GET");
		callAjax(url,formIpData, refreshIpCounts, "GET");
	}
	init();
});

// name="webSecurityLogDTO" input="/websecuritylog/webSecurityLogCharts.jsp"
</script>









