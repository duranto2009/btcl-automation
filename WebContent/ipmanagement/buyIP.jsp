 
 
 













<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<%@ include file="../includes/checkLogin.jsp" %>

<html>
  <head>
    <meta charset="UTF-8" />
    <title>IP Management</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'/>
    <!-- Bootstrap 3.3.4 -->
    <link href="../assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
	
    <!-- Theme style -->
    <link href="../assets/css/layout.css" rel="stylesheet" type="text/css" />
    <link href="../assets/css/skin.css" rel="stylesheet" type="text/css" />
	<link href="../assets/css/custom.css" rel="stylesheet" type="text/css" />
	
	 <!-- REQUIRED JS SCRIPTS -->
    <script src="../assets/js/jQuery-2.1.4.min.js"></script>
    <script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="../assets/js/app.min.js" type="text/javascript"></script>
    <script src="../assets/js/util.js" type="text/javascript"></script>
    
     <script type="text/javascript" src="../scripts/datetimepicker.js"></script> 
     
     <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css"/>

	
	<script type="text/javascript">
		$(document).ready(function(){
			var clicked = false;
			$('#ip_submenu').hide();
			$('.ip-address').click(function(){
				if(clicked == false){
					$(this).css('padding-right','0');
					$('#ip_submenu').show();
					clicked = true;
				}else{
					$(this).css('padding-right','15px');
					$('#ip_submenu').hide();
					clicked = false;
				}
				
			});
		});
	</script>
	
	<style type="text/css">
		
		.btn {
    
    border: 0 none;
    font-weight: 700;
    letter-spacing: 1px;
    text-transform: uppercase;
    height: 40px;
}
 
.btn:focus, .btn:active:focus, .btn.active:focus {
    outline: 0 none;
}
 
.btn-primary {
    background: #47d147;
    color: #ffffff;
}
 
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open > .dropdown-toggle.btn-primary {
    background: #29a329;
}
 
.btn-primary:active, .btn-primary.active {
    background: #007299;
    box-shadow: none;
}

.panel {
	border: 0 none;
}
.panel-body{
border: 0 none;
}

.panel-primary > .panel-heading {
    background-color: #337ab7;
    border-color: #aeeaae;
    color: #fff;
}
		</style>
<script>
function setCost()
{
	var f = document.forms[1];

	var e = f.iigBANDWIDTH;
	
	if(e.options[e.selectedIndex].value == '0')
	{			
		f.iigCost.value = '960';
	}
	else if(e.options[e.selectedIndex].value == '1')
	{			
		f.iigCost.value = '720';
	}	
	else if(e.options[e.selectedIndex].value == '2')
	{			
		f.iigCost.value = '660';
	}	
	else if(e.options[e.selectedIndex].value == '3')
	{			
		f.iigCost.value = '600';
	}	
	else if(e.options[e.selectedIndex].value == '4')
	{			
		f.iigCost.value = '540';
	}	
	else if(e.options[e.selectedIndex].value == '5')
	{			
		f.iigCost.value = '460';
	}	
	else if(e.options[e.selectedIndex].value == '6')
	{			
		f.iigCost.value = '400';
	}	
	
}

</script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body onload="activateMenu('ip_parent','ip','');" class="layout-skin sidebar-mini">
    <div class="wrapper">

       <%@ include file="../includes/responsive/top-header.jsp" %>
            
      <%if(loginDTO.getUserID() > 0){%>      
      <%@ include file="../includes/responsive/left-menu.html" %>
      <%}else{%>
      <%@ include file="../includes/responsive/left-menu_client.html" %>
      <%}%>
      
      <div class="content-wrapper">
        <section class="content-header">
          <h1>
            Apply for IP Management Service
            <!-- <small>Optional description</small> -->
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
            <li class="active">Here</li>
          </ol>
        </section>

	
		
		
		
        <!-- Main content -->
        <section class="content">
			<div class="row">
			<div class="col-lg-offset-3 col-lg-6">
				<div class="panel panel-success">
					
						<div class="panel-heading" style="text-align: center; font-size: 14pt; background-color: #aeeaae;"><div class="div_title">Request Form</div></div>
					
					<div class="panel-body">
						
							<form name="iigForm" method="POST" action="/BTCLHosting3/AddIig.do" class="form-horizontal" onsubmit="return validate();">
 							 
								

<!-- 								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Registration Category</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<select name="iigREGCAT" class="form-control"><option value="0" selected="selected">Educational</option>
									  	
									  	<option value="1">Government</option>
									  	
									  	<option value="2">Industrial</option>
									  	
									  	<option value="3">ISP</option>
									  	
									  	<option value="4">International Org</option>
									  	
									  	<option value="5">Foreign Org</option>
									  	
									  	<option value="6">Others</option></select>
									</div>
								</div>
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Connection Type</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<select name="iigCONNTYPE" class="form-control"><option value="0" selected="selected">Permanent</option>
									  	
									  	<option value="1">Temporary</option>
									  	
									  	<option value="2">Others</option></select>
									</div>
								</div>
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Loop Provider</label>
									<div class="col-md-8 col-sm-8 col-xs-8" >
										 <select name="iigLOOPPROVIDER" class="form-control"><option value="0" selected="selected">BTCL</option>
									  	
									  	<option value="1">Customer</option></select>
									</div>
								</div> -->
								
<!-- 								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Size</label>
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<div class="input-group">
										 <select name="iigINITPERIOD" class="form-control"><option value="1">1</option> 
				 															
											<option value="2">2</option> 
				 															
											<option value="3">3</option> 
				 															
											<option value="4">4</option> 
				 															
											<option value="5">5</option> 
				 															
											<option value="6">6</option> 
				 															
											<option value="7">7</option> 
				 															
											<option value="8">8</option> 
				 															
											</select>	
																			
									<span class="input-group-addon">( U )</span>
									
									</div>
									</div>
									
								</div> -->
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Package</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<div>
										 <select name="iigBANDWIDTH" onchange="setCost()" class="form-control">										 				 																										 				 													
				 						<option value="1" selected="selected" >4 IP Pack</option>				 																										 				 															
				 						<option value="2">8 IP Pack</option>				 																										 				 													
				 						</select>
									 
									<!-- <span class="input-group-addon">Mbps</span> -->
									</div>
									</div>																	
								</div>
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Additional IP</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<div class="input-group">
										 <select name="iigBANDWIDTH" onchange="setCost()" class="form-control">
									<%for(int p = 0; p < 20; p++){ %>																										 				 															
				 						<option value="<%=p%>" ><%=p%></option>				 																										 				 															
										<%} %>													 				 															
				 						</select>
									 
									<span class="input-group-addon"> 200 tk each</span>
									</div>
									</div>																	
								</div>								
								
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Network Name</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<input class="form-control"  name="hpNAME" value="" />
									</div>
								</div>
								
<!-- 								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Local End Address Desctiption</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<input class="form-control"  name="hpNAME" value="" />
									</div>
								</div>
								
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Remote End Address</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<input class="form-control"  name="hpNAME" value="" />
									</div>
								</div>
								
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Remote End Address Desctiption</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<input class="form-control"  name="hpNAME" value="" />
									</div>
								</div> -->
								
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Cost</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<div class="input-group">
										 <input type="text" name="iigCOST" value="3200" readonly="readonly" class="form-control">									 
									<span class="input-group-addon">tk</span>
									</div>
									</div>
									
									
								</div>								
								
<!-- 								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Service Start Time</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<div class='input-group'>
											<input type='text' value="" name="iigSERVICESTARTTIME" id="iigSERVICESTARTTIME"
												class="form-control" /> <span class="input-group-addon">
												<a href="javascript:NewCal('iigSERVICESTARTTIME','ddmmyyyy',false,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date"></a>
											</span>
										</div>
										<script type="text/javascript">
											new tcal(
													{
														'formname' : 'hostingPackageForm',
														'controlname' : 'ORTIMEEND'
													});
										</script>
										
									</div>
								</div> -->
								
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Service Start Date</label>
									<div class="col-md-8 col-sm-8 col-xs-8"> 
								<div class='input-group'>
								<input type = "text" name="iigSERVICESTARTTIME" class="form-control"/>
								<span class="input-group-addon">
								<script type="text/javascript">new tcal({
									'formname' : 'iigForm',
									'controlname' : 'iigSERVICESTARTTIME'
								});</script>
								</span>
								</div> 
								</div>
								</div>								

								<div class="form-group">
									<div
										class="col-md-offset-4 col-sm-offset-4 col-xs-offset-4 col-md-8 col-sm-8 col-xs-8">
										<div class="input-group">
											
										    <input class="btn btn-default"  type="reset" value="Reset">
										    
										    <input class="btn btn-success"  type="submit" value="Submit">
										</div>
									</div>
								</div>
								
								

							</form>
							
					</div>
				</div>
			</div>





			</div>
        </section>
        
         
          
        
		
      </div><!-- /.content-wrapper -->
	  
	     <!-- Main Footer -->
      <footer class="main-footer">
        <div class="pull-right hidden-xs">
          
        </div>
        <strong>Copyright &copy; 2016 <a href="#">REVE Systems Ltd.</a>.</strong> All rights reserved.
      </footer>
         
	 
    
      

    </div><!-- ./wrapper -->

  </body>
</html>





									
								