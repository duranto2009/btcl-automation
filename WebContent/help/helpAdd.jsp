<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="complain.ComplainService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,complain.*" %>


<%
 String title = "Add New Complain";
 String submitCaption = "Add another complain";
 String submitCaption2 = "Submit";
 String actionName = "/AddHelp";

%>
<html><html:base />
  <head>
    <meta charset="UTF-8" />
    <title>BTCL | Help Add</title>
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

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body onload="activateMenu('help_parent','help','')" class="layout-skin sidebar-mini">
    <div class="wrapper">

      <!-- 1. ADD Main Header (top header) -->
      <!-- 2. ADD Left side column. contains the logo and sidebar -->
      <!-- 3. Content Wrapper. Contains page content -->
      <%@ include file="../includes/responsive/top-header.jsp" %>
      <%if(loginDTO.getUserID() > 0){%>      
      <%@ include file="../includes/responsive/left-menu.html" %>
      <%}else{%>
      <%@ include file="../includes/responsive/left-menu_client.html" %>
      <%}%>      
      <div class="content-wrapper">
        <section class="content-header">
          <h1>
            New Complain
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
					
						<div class="panel-heading" style="text-align: center; font-size: 14pt; background-color: #aeeaae;"><div class="div_title">Describe your Complain</div></div>
					
					<div class="panel-body">
						
							<html:form styleClass="form-horizontal"  action="/AddHelp" method="POST" onsubmit="return validate();">
				 		<%						
				  String msg = null;
				  if( (msg = (String)session.getAttribute(util.ConformationMessage.HELP_ADD))!= null)
				  {
				    session.removeAttribute(util.ConformationMessage.HELP_ADD);
				    %>
						
                                <div style="margin: 0 0 2px;" class="row">
                                    <b class="green_text"><%=msg%></b>
                                </div>
                            <%}%>
                            	
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Subject</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<html:text styleClass="form-control"  property="helpSub" value="" />
									</div>
								</div>
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Complain Category</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<html:select styleClass="form-control"  property="categoryID" size="1">
		                          		<%
		                                   ComplainService complainService = new ComplainService();
		                          		   ArrayList<ComplainDTO> complainList = complainService.getComplains();
		                                   for(int i = 0; i<complainList.size();i++)
		                                   {
		                          		%>
		                           				<html:option value="<%=Long.toString(complainList.get(i).getComplainID())%>"><%=complainList.get(i).getComplainType() %> </html:option>
		                          		<%
		                                   }    
		                         		%>
									  </html:select>
									</div>
								</div>
								<div class="form-group">
									<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Complain Message</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<html:textarea styleClass="form-control"  property="helpDesc" value="" />
									</div>
								</div>						



							<%-- <div class="form-group">
								<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Cost</label>
								<div class="col-md-6 col-sm-6 col-xs-6" style="padding-right:0px">
									<!-- <div class='input-group'> -->
										<html:text styleClass="form-control" property="hpCOST"
											value="" />
											</div>
											<div class="col-xs-2" style="padding-left:0px">

										<!-- <span class="input-group-addon"> -->
										<!-- <div styleClass="input-group-addon"> --> 
										<html:select styleClass="form-control"
												 property="hpCOSTTYPE" >
												<html:option value="<%=HostingConstants.PaymentTypeVal[0]%>"><%=HostingConstants.PaymentType[0]%></html:option>
												<html:option value="<%=HostingConstants.PaymentTypeVal[1]%>"><%=HostingConstants.PaymentType[1]%></html:option>
											</html:select>
											
										<!-- </span> -->
									<!-- </div> -->
								</div>
							</div>

							<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Operating System</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										<html:select styleClass="form-control"  property="hpOS" size="1">
									  	<html:option value="<%=HostingConstants.OSVal[0]%>"><%=HostingConstants.OS[0]%></html:option>
									  	<html:option value="<%=HostingConstants.OSVal[1]%>"><%=HostingConstants.OS[1]%></html:option>
									  </html:select>
									</div>
								</div>
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">Space</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										 <html:select styleClass="form-control"  property="hpSPACE">
									  	<html:option value="1024">1 GB</html:option>
									  	<html:option value="1024">2 GB</html:option>
									  	<html:option value="1024">5 GB</html:option>
									  	<html:option value="1024">10 GB</html:option>
									  	<html:option value="1024">20 GB</html:option>
									  </html:select>
									</div>
								</div>
								
								<div class="form-group">
									<label for=""
										class="control-label col-md-4 col-sm-4 col-xs-4">BandWidth</label>
									<div class="col-md-8 col-sm-8 col-xs-8">
										 <html:select styleClass="form-control"  property="hpBANDWIDTH" >
									  	
									  	<html:option value="1024">5 GB</html:option>
									  	<html:option value="2048">10 GB</html:option>
									  	<html:option value="5120">20 GB</html:option>
									  	<html:option value="10240">30 GB</html:option>
									  	<html:option value="20480">Unlimited</html:option>
									  </html:select>
									</div>
								</div> --%>
								
								
								<div class="form-group">
									<div
										class="col-md-offset-4 col-sm-offset-4 col-xs-offset-4 col-md-8 col-sm-8 col-xs-8">
										<div class="input-group">
											
										    <input class="btn btn-default"  type="reset" value="Reset">
										    
										    <input class="btn btn-success"  type="submit" value="Submit">
										</div>
									</div>
								</div>
								

							</html:form>
						
					</div>
				</div>
			</div>





			</div>
        </section>
		
      </div><!-- /.content-wrapper -->
	  
     <%@ include file="../includes/responsive/footer.html"%>
      

    </div><!-- ./wrapper -->

  </body>
</html>