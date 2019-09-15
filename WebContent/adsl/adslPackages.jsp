
<!DOCTYPE html>




<%@page import="hosting.HostingConstants"%>
<%@ include file="../includes/checkLogin.jsp" %>


















<html>
  <head><base href="http://localhost:8580/BTCLHosting3/hostingpackage/hostingPackage.jsp">
    <meta charset="UTF-8">
    <title>BTCL | ADSL Packages</title>
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

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body onload="activateMenu('webhosting_parent','webhosting','package')" class="layout-skin sidebar-mini">
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
            ADSL Packages
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
					<form name="hostingPackage2Form" method="POST" action="/BTCLHosting3/ViewHostingpackage2.do">
					<div class="form-horizontal">
				
					
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Name</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
							    <input type="text" class="form-control" id="" placeholder="" name="hpNAME"
					    >
								
							</div>
						</div>
						
						</div>
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Cost</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
							    <input type="text" class="form-control" id="" placeholder="" name="hpCOST"
					    >
								
							</div>
						</div>
						
						</div>
						
						
						</div>
						

						
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						  




<tr >



<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Payment Inverval</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="HPCOSTTYPE">
 <option value="-1" selected="selected">All</option>

<option value="1" >Monthly</option> 
<option value="2" >Yearly</option> 

</select>
</div>
</div>
						  
						</div>
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						  




<tr >

<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Space</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control" size="1" name="HPSPACE">
			<option value="-1" selected="selected">All</option>
		
			<option value="1024" >1 GB</option> 
			<option value="2048" >2 GB</option> 
			<option value="5120" >5 GB</option>
			<option value="10240" >10 GB</option>
			<option value="20480" >20 GB</option>
	
		</select>
	</div>
</div>
						  
						</div>
						
						
						</div>
						

						
					
						<div class="row">
						
							<div class="col-md-6 col-sm-6">
					  
						  







<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Operating System</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<select class="form-control" size="1" name="HPOS">
 <option value="-1" selected="selected">All</option>

<option value="1" >Windows</option> 
<option value="2" >Linux</option> 

</select>
</div>
</div>
						  
						</div>
						
						

						
					
							<div class="col-md-6 col-sm-6">
					  
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">BandWidth</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
							    <input type="text" class="form-control" id="" placeholder="" name="hpBANDWIDTH"
					    >
								
							</div>
						</div>
						
						</div>
						
						
						</div>
						

						
						
						
						<div class="row">
						
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
				<form name="hostingPackage2Form" method="POST" action="/BTCLHosting3/ViewHostingpackage2.do">
				  <table >
					<tbody>
						<tr>
						  <td id="td_arrow">
							<a href="../ViewHostingpackage2.do?id=first" style="margin:0 2px"><i class="fa fa-backward fa-2x"></i></a>
							<a href="../ViewHostingpackage2.do?id=previous" style="margin:0 2px"><i class="fa fa-caret-left fa-2x"></i></a>
							<a href="../ViewHostingpackage2.do?id=next" style="margin:0 2px"><i class="fa fa-caret-right fa-2x"></i></a>
							<a href="../ViewHostingpackage2.do?id=last" style="margin:0 2px"><i class="fa fa-forward fa-2x"></i></a>
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
				<form name="hostingPackageForm" method="POST" action="/BTCLHosting3/BuyPackage.do" onsubmit="return decide();">
				
					<input type="hidden" value="" name="actionType">
					<input type="hidden" value="" name="type">
					
					<table id="search_table" class="table table-bordered">
						<caption class="text-center">
								<%if(loginDTO.getUserID() > 0){%>
                                                        <input type="submit" class="btn btn-danger" name="Delete" value="Delete" onclick="return false;document.forms[3].actionType.value=<%=HostingConstants.STATUS_DELETE%>"/>
														<%-- <input type="submit" name="Reject" value="Reject" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_REJECT%>"/>
                                                        <input type="submit" name="Block" value="Block" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_BLOCK%>"/> --%>
                                                    <%}else{%>
                                                        <%-- <input type="submit" name="Buy" value="Add To Cart" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_PAID%>"/> --%>
                                                        <input type="submit" name="Buy" class="btn btn-primary" value="Add To Cart" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_PENDING%>"/>
                                                    <%}%> 
												</caption>
												<thead>
						 															<tr align="center">
                                                    <td class="td_viewheader" valign="top" >
                                                        ID
                                                    </td>
                                                    <td class="td_viewheader" valign="top" >
                                                        Name
                                                    </td>
                                                    <td class="td_viewheader" valign="top" >
                                                        Cost
                                                    </td>
<!--                                                     <td class="td_viewheader" valign="top" >
                                                        HPCOSTTYPE
                                                    </td> -->
                                                    <td class="td_viewheader" valign="top" >
                                                        Space
                                                    </td>
                                                    <td class="td_viewheader" valign="top" >
                                                        OS
                                                    </td>
                                                    <td class="td_viewheader" valign="top" >
                                                        BandWidth
                                                    </td>
<!--                                                 	<td class="td_viewheader" valign="top" >
                                                        Status
                                                    </td> -->
                                                    
													
													<td class="td_viewheader" align="left" style="padding-left: 12px;" valign="top" width="120" height="25">
                                                        <!-- <input type="checkbox" name="Check_All2" value="CheckAll2" onClick="CheckAll2();"/> -->
                                                        <!-- <input type="submit"  value="Activate" />	 -->
                                                        Select												
													</td>
													                                                  
                                                    
                                                    <!-- TODO : put permission-->
<!--                                                     <td class="td_viewheader" align="left" style="padding-left: 12px;" valign="top" width="100" height="25">
                                                        <input type="checkbox" name="Check_All" value="CheckAll" onClick="CheckAll();">Select
                                                        <input type="submit"  value="Delete" >
                                                    </td> -->
                                                    
                                                </tr>
                                                
                                                    <tr class="td_viewdata1">
                                                        <td  width="128" height="18" >1</td>
                                                        <td  width="128" height="18" >P1</td>
                                                        <td  width="128" height="18" >2500.0 Yearly</td>
                                                        
                                                        <td  width="128" height="18" >5 GB</td>
                                                        <td  width="128" height="18" >Linux</td>
                                                        <td  width="128" height="18" >10 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="1"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata2">
                                                        <td  width="128" height="18" >2</td>
                                                        <td  width="128" height="18" >P2</td>
                                                        <td  width="128" height="18" >3500.0 Yearly</td>
                                                        
                                                        <td  width="128" height="18" >10 GB</td>
                                                        <td  width="128" height="18" >Linux</td>
                                                        <td  width="128" height="18" >12 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="2"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata1">
                                                        <td  width="128" height="18" >3</td>
                                                        <td  width="128" height="18" >P3</td>
                                                        <td  width="128" height="18" >4000.0 Yearly</td>
                                                        
                                                        <td  width="128" height="18" >12 GB</td>
                                                        <td  width="128" height="18" >Linux</td>
                                                        <td  width="128" height="18" >15 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="3"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata2">
                                                        <td  width="128" height="18" >4</td>
                                                        <td  width="128" height="18" >P4</td>
                                                        <td  width="128" height="18" >1000.0 Yearly</td>
                                                        
                                                        <td  width="128" height="18" >1 GB</td>
                                                        <td  width="128" height="18" >Linux</td>
                                                        <td  width="128" height="18" >5 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="4"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata1">
                                                        <td  width="128" height="18" >5</td>
                                                        <td  width="128" height="18" >mmkkll</td>
                                                        <td  width="128" height="18" >100.30000305175781 Yearly</td>
                                                        
                                                        <td  width="128" height="18" >1 GB</td>
                                                        <td  width="128" height="18" >Linux</td>
                                                        <td  width="128" height="18" >5 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="5"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata2">
                                                        <td  width="128" height="18" >6</td>
                                                        <td  width="128" height="18" >asdfasdf</td>
                                                        <td  width="128" height="18" >2222.0 Monthly</td>
                                                        
                                                        <td  width="128" height="18" >1 GB</td>
                                                        <td  width="128" height="18" >Windows</td>
                                                        <td  width="128" height="18" >1 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="6"></input>
                                                        	
															
                                                        	</td>
                                                        
                                                        
                                                        
                                           
                                                        
                                                    </tr>
                                                    
                                                    <tr class="td_viewdata1">
                                                        <td  width="128" height="18" >7</td>
                                                        <td  width="128" height="18" >asdfadsf</td>
                                                        <td  width="128" height="18" >1111.0 Monthly</td>
                                                        
                                                        <td  width="128" height="18" >1 GB</td>
                                                        <td  width="128" height="18" >Windows</td>
                                                        <td  width="128" height="18" >1 GB</td>
                                                        

                                                        

                                                        
                                                        
                                                        <td align="left" style="padding-left: 12px" width="120" height="18" >
                                                        	<input type="checkbox" name="deleteIDs" value="7"></input>
                                                        	
															
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
          <!-- Anything you want -->
        </div>
        <strong>Copyright &copy; 2016 <a href="#">REVE Systems Ltd.</a>.</strong> All rights reserved.
      </footer>

    </div><!-- ./wrapper -->

  </body>
</html>