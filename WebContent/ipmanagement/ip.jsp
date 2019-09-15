<!DOCTYPE html>

<%@page import="hosting.HostingConstants"%>
<%@ include file="../includes/checkLogin.jsp" %>




















<html>
  <head>
    <meta charset="UTF-8">
    <title>IP Management</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
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

  </head>

  <body onload="activateMenu('ip_parent','ip','')" class="layout-skin sidebar-mini">
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
            IP Management Services
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
              
            



<style>
.kay-group > input 
{
	margin:5px;
}
</style>

		<div class="col-md-offset-2 col-lg-offset-1 col-md-6 col-sm-offset-4 col-sm-6 col-lg-10">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title" style="text-align:center;">Search</h3>
				</div>
				<div class="panel-body">
					<!-- <form class="form-horizontal"> -->
					<form name="iigForm" method="POST" action="/BTCLHosting3/ViewIig.do">
					<div class="form-horizontal">
				
					
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						  








<!-- <div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Reg. Category</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="IGREGCAT">
 <option value="-1" selected="selected">All</option>

<option value="0" >Educational</option> 

<option value="1" >Government</option> 

<option value="2" >Industrial</option> 

<option value="3" >ISP</option> 

<option value="4" >International Org</option> 

<option value="5" >Foreign Org</option> 

<option value="6" >Others</option> 

</select>
</div>
</div> -->
						  
						</div>
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						  








<!-- <div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Conn Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="IGCONNTYPE">
 <option value="-1" selected="selected">All</option>

<option value="0" >Permanent</option> 

<option value="1" >Temporary</option> 

<option value="2" >Others</option> 

</select>
</div>
</div> -->
						  
						</div>
						
						
						</div>
						

						
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						  




								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Pacakge</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<!-- <div class="input-group"> -->
										 <select name="iigBANDWIDTH" onchange="setCost()" class="form-control">
										 				 																										 				 															
				 						<option value="1" selected="selected" >4 IP Pack</option>
				 																										 				 															
				 						<option value="2">8 IP Pack</option>
				 																										 				 															
<!-- 				 						<option value="3">21-50</option>
				 																										 				 															
				 						<option value="4">51-150</option>
				 																										 				 															
				 						<option value="5">151-300</option>
				 																										 				 															
				 						<option value="6">301-500</option>				 						
				 						
				 						<option value="8">500+</option> -->
				 						</select>
									 
									<!-- <span class="input-group-addon">Mbps</span> -->
									</div>
									</div>																	
								</div>



<!-- <div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Loop Provider</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
<select class="form-control" size="1" name="IGLOOPPROVIDER">
 <option value="-1" selected="selected">All</option>

<option value="0" >BTCL</option> 

<option value="1" >Customer</option> 

</select>
</div>
</div> -->
						  
						
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						  








<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="IGSTATUS">
 <option value="-1" selected="selected">All</option>

<option value="0" >Pending</option> 

<option value="1" >Approved</option> 

<option value="2" >Paid</option> 

<option value="3" >Active</option> 

<option value="4" >Rejected</option> 

<option value="5" >Block</option> 

<option value="6" >Delete</option> 

<option value="7" >Cancelled</option> 

<option value="8" >Expired</option> 

</select>
</div>
</div>
						  
						</div>
						
						
						</div>
						

						
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						  









<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">


<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Order Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class='input-group'>
<input type = "text" name="IGSERVICESTARTTIME" value="" class="form-control"/>
<span class="input-group-addon">
<script type="text/javascript">new tcal({
	'formname' : 'iigForm',
	'controlname' : 'IGSERVICESTARTTIME'
});</script>
</span>
</div> 
</div>
</div>
						  
						</div>
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						  









<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">


<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Order Date To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class='input-group'>
<input type = "text" name="IGSERVICESTARTTIMEEND" value="25-01-2016" class="form-control"/>
<span class="input-group-addon">
<script type="text/javascript">new tcal({
	'formname' : 'iigForm',
	'controlname' : 'IGSERVICESTARTTIMEEND'
});</script>
</span>
</div> 
</div>
</div>
						  
						</div>
						
						
						</div>
						

						
						
						
						<div class="row">
						
						<div class="col-md-6 col-sm-6">
						
<!-- 								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Distance</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<div class="input-group">
										 <select name="iigBANDWIDTH" onchange="setCost()" class="form-control">
										 				 																										 				 															
				 						<option value="1" selected="selected" >1-50</option>
				 																										 				 															
				 						<option value="2">51 - 100</option>
				 																										 				 															
				 						<option value="3">101-200</option>
				 																										 				 															
				 						<option value="4">201-300</option>
				 																										 				 															
				 						<option value="5">300+</option>
				 																										 				 															
				 						</select>
									 
									<span class="input-group-addon">KM</span>
									</div>
									</div>																	
								</div> -->
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">IP</label>
									
									<div class="col-md-8 col-sm-8 col-xs-8 col-lg-8">
									<input class="form-control" />
									</div>																	
								</div>								
						</div>
						
						<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Records Per Page</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="text" class="form-control" name="RECORDS_PER_PAGE" id="" placeholder="">
							</div>
						</div>
						</div>						
						
						</div>
						<div class="form-group">
							<div class="col-lg-offset-5 col-sm-offset-4 col-xs-offset-4 col-md-8 col-sm-8 col-lg-10">
								<div class="input-group kay-group">
									<input type="hidden" value="yes" name="search">
									<input type="reset"  class="btn btn-default" value="Reset">
									<input type="submit" class="btn btn-search" value="Search">
								</div>
							</div>
						</div>
<!-- 						<div class="form-group">
							<div class="col-md-offset-4 col-sm-offset-4 col-xs-offset-4 col-md-8 col-sm-8 col-xs-8">						
						 	<caption class="text-center">
								<input type="submit" class="btn btn-success" onclick="document.forms[2].actionType.value=3" value="Activate" name="Activate">
								<input type="submit" class="btn btn-danger" onclick="document.forms[2].actionType.value=4" value="Reject" name="Reject">
								<input type="submit" class="btn btn-warning" onclick="document.forms[2].actionType.value=5" value="Block" name="Block">
								<input type="button" class="btn btn-primary" value="Download" name="Download">
							</caption>
						</div>
						</div> -->
						
						</div>
					</form>
				</div>
			</div>
		</div>	
		
				<!-- Pagination -->
		<div class="col-md-offset-4 col-md-4 col-sm-offset-4 col-sm-4 table-pagination">	
			<center>		
				<form name="iigForm" method="POST" action="/BTCLHosting3/ViewIig.do">
				  <table >
					<tbody>
						<tr>
						  <td id="td_arrow">
							<a href="../ViewIig.do?id=first"><i class="fa fa-backward fa-2x"></i></a>
							<a href="../ViewIig.do?id=previous"><i class="fa fa-caret-left fa-2x"></i></a>
							<a href="../ViewIig.do?id=next"><i class="fa fa-caret-right fa-2x"></i></a>
							<a href="../ViewIig.do?id=last"><i class="fa fa-forward fa-2x"></i></a>
						  </td>
						  <td>
							&nbsp;Page&nbsp;
						  </td>
						  <td>
							<input type="text" size="2" value="1" name="pageno" style="text-align:right;font-family:verdana;color:blue;font-weight:bold;">&nbsp;
						  </td>
						  <td>
							of&nbsp;1&nbsp;
						  </td>
						  <td>
							<input type="hidden" value="yes" name="go">
							<input type="submit" value="Go" class="btn btn-primary">
						  </td>
						</tr>
					</tbody>
				  </table>
				</form>
			</center>	
		</div><!-- /.Pagination -->

		
		<!-- Search Result Table -->
		<div class="col-md-12 search-table-div">	
			<div class="table-responsive">			
				<form name="iigForm" method="POST" action="/BTCLHosting3/ChangeIIGStatus.do" onsubmit="return decide();">
				
					<input type="hidden" value="" name="actionType">
					<input type="hidden" value="" name="type">
					
					<table id="search_table" class="table table-bordered table-striped">
						<caption class="text-center">
								<%if(loginDTO.getUserID() > 0){%>
									<input type="submit" class="btn btn-success" name="Approve" value="Approve" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_APPROVED%>"/>
                                    <input type="submit" class="btn btn-danger" name="Reject" value="Reject" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_REJECT%>"/>
								    <input type="submit" class="btn btn-success2" name="Activate" value="Activate" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_ACTIVE%>"/>                                    
                                    <input type="submit" class="btn btn-warning" name="Block" value="Block" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_BLOCK%>"/>													
                                    <%}else{%>
                                    <input type="submit" class="btn btn-primary" name="Pay" value="Pay" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_PAID%>"/>
                                    <input type="submit" class="btn btn-warning" name="Cancel" value="Cancel" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_CANCEL%>"/>                                                        
                                    <%} %>
						</caption>
						<thead>
							 
								<tr align="center">
                                     <td class="td_viewheader" valign="top" width="128" height="25">
                                         Invoice No.
                                     </td>
                                                                                          
                                     <td class="td_viewheader" valign="top" width="128" height="25">
                                         Package
                                     </td>
                                     <td class="td_viewheader" valign="top" width="128" height="25">
                                         Total No of IP
                                     </td>
                                    <!--  <td class="td_viewheader" valign="top" width="128" height="25">
                                         Initial Commit Period (Months)
                                     </td> -->
<!--                                      <td class="td_viewheader" valign="top" width="128" height="25">
                                         Loop Provider
                                     </td>
                                     <td class="td_viewheader" valign="top" width="128" height="25">
                                         Start Time
                                     </td> -->
                                 <!-- <td class="td_viewheader" valign="top" width="128" height="25">BandWidth (Mbps)</td> -->
                                     <td class="td_viewheader" valign="top" width="128" height="25">
                                         Order Date
                                     </td>                                                
                                 <td class="td_viewheader" valign="top" width="128" height="25">Status</td>
                                 <td class="td_viewheader" valign="top" width="128" height="25">Cost</td>
                                                              
                                                                                     
                                     
                                     <td class="td_viewheader" valign="top" width="128" height="25">Select</td>                                            
                            </tr>							 
							 
						</thead>
						<tbody>

							  
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>54</a></td>
                                                        <td  width="128" height="18" >4 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >6</td>                                                   
                                                        <td  width="128" height="18" >13-01-2016</td>                                                                                                                
                                                        <td  width="128" height="18" >Approved</td>
                                                        <td  width="128" height="18" >10000.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>63</a></td>
                                                        <td  width="128" height="18" >8 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >8</td>                                                   
                                                        <td  width="128" height="18" >13-01-2016</td>                                                                                                                
                                                        <td  width="128" height="18" >Approved</td>
                                                        <td  width="128" height="18" >12000.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>67</a></td>
                                                        <td  width="128" height="18" >4 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >6</td>                                                   
                                                        <td  width="128" height="18" >2-01-2016</td>                                                                                                                
                                                        <td  width="128" height="18" >Pending</td>
                                                        <td  width="128" height="18" >8000.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>70</a></td>
                                                        <td  width="128" height="18" >4 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >4</td>                                                   
                                                        <td  width="128" height="18" >13-01-2014</td>                                                                                                                
                                                        <td  width="128" height="18" >Paid</td>
                                                        <td  width="128" height="18" >4200.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>73</a></td>
                                                        <td  width="128" height="18" >8 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >10</td>                                                   
                                                        <td  width="128" height="18" >13-01-2016</td>                                                                                                                
                                                        <td  width="128" height="18" >Approved</td>
                                                        <td  width="128" height="18" >20000.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>
                                                    <tr class="td_viewdata1">
                                                        
                                                        <td  width="100" height="18" ><a target="_blank" href='../hosting/viewBill.jsp?orderID=54'>77</a></td>
                                                        <td  width="128" height="18" >4 IP Pack</td>                                                                                                                
                                                        <td  width="128" height="18" >5</td>                                                   
                                                        <td  width="128" height="18" >13-01-2016</td>                                                                                                                
                                                        <td  width="128" height="18" >Approved</td>
                                                        <td  width="128" height="18" >10000.0</td>
                                                        
                                                        

                                                        <td align="center" width="100" height="18" >
                                                        <input type="checkbox" name="deleteIDs" value="54"></input>
                                                        </td>                                                                                                                 
                                                    </tr>                                                    
                                                    
                                                    
                                                    
                                                        
                                                        

                                                    						  

						</tbody>
					</table>
				</form>	
			</div>
		</div><!-- /.Search Result Table -->

		</div>
        </section>
		
      </div><!-- /.content-wrapper -->
	  
     <!--4. ADD FOOTER FILE-->
        <!-- Main Footer -->
      <footer class="main-footer">
        <div class="pull-right hidden-xs">
          
        </div>
        <strong>Copyright &copy; 2016 <a href="#">REVE Systems Ltd.</a>.</strong> All rights reserved.
      </footer>

    </div><!-- ./wrapper -->

  </body>
</html>