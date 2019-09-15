
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="hosting.HostingPackageDTO"%>
<%@page import="hosting.HostingConstants"%>
<%@page import="hosting.OrderDTO"%>
<%@page import="hosting.HostingDAO"%>
<%@page import="clientmodule.password.PasswordService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.sql.*, 
                  databasemanager.*,				  
				  util.DAOResult,
				  java.text.SimpleDateFormat,
				  java.util.Date,
 				 sessionmanager.SessionConstants
				 " 
				 %>
				 
<%@page errorPage="../common/failure.jsp" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>BTCL | Dashboard</title>
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

  <body class="layout-skin sidebar-mini">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">

        <!-- Logo -->
        <a href="index2.html" class="logo">
          <img class="img-responsive" alt="" title="" src="../assets/images/BTCL-small-Logo.png">
        </a>

        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top top-right-navbar" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
		  <h1>Bangladesh Telecommunications Company Ltd.</h1>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
              <!-- Control Sidebar Toggle Button -->
			   <li>
                
              </li>
              <li>
                <a href="#" data-toggle="control-sidebar"><i class='fa fa-user'></i>&nbsp;&nbsp;Log Out</i></a>
              </li>
            </ul>
          </div>
		  
        </nav>
      </header>
	  
      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
		
          <!-- search form (Optional) -->
          <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="q" class="form-control" placeholder="Search..."/>
              <span class="input-group-btn">
                <button type='submit' name='search' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
              </span>
            </div>
          </form>
          <!-- /.search form -->

          <!-- Sidebar Menu -->
        <ul class="sidebar-menu">
            <li class="header">All Menu</li>
            <!-- Optionally, you can add icons to the links -->
            <li class="active"><a href="#"><i class='fa fa-home'></i> <span>Home</span></a></li>

			<li class="treeview">
				<a href="#"><i class='fa fa-user'></i> <span>Clients</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu">
                    <li>
						<a href="#"><i class='fa fa-user'></i> <span>Manage Clients</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li><a href="../clients/clientAdd.jsp"> Add Client</a></li>
							<li><a href="../mdf/toBeConnected.jsp"> To Be Connected</a></li>
							<li><a href="../mdf/toBeDisconnected.jsp">To Be Disconnected</a></li>
							<li><a href="../mdf/showAllInfo.jsp">Show All Information</a></li>
							<li><a href="../mdf/printableExchangeInfo.jsp">  Print Exchange Information</a></li>
							<li><a href="../ViewClient.do">Search </a></li>
							<li><a href="../ViewClientStatus.do">Change Client Status </a></li>
							<li><a href="../ViewClientStatusChange.do">View Change Status</a></li>
						</ul>
                    </li>
                	<li>
						<a href="#"><i class='fa fa-user'></i> <span>Recharge Client</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../recharge/rechargeClient.jsp">Recharge Client</a></li>
							<li><a href="../viewRechargeInfo.do">Search</a></li>
						</ul>
                	</li>
                	<li>
						<a href="#"><i class='fa fa-user'></i> <span>Other Charges</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../othercharge/AddOtherCharge.jsp">Add Other Charge</a></li>
							<li><a href="../ViewOther.do">Search</a></li>
						</ul>
                	</li>
					<!-- Shift Client -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Shift Client</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../shift/shiftClient.jsp">Shift Client</a></li>
							<li><a href="../viewShiftingInfo.do">Search</a></li>
						</ul>
					</li>
         	
					<!-- Migration -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Migrations</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../migration/addMigration.jsp">Add Migration</a></li>
							<li><a href="../ViewMigration.do">Search</a></li>
							<li><a href="../batchmigration/AddBatchMigration.jsp">Add Batch Migration</a></li>
							<li><a href="../ViewBatchMigration.do">Search Batch Migration</a></li>
						</ul>
					</li>
					<!-- Remove Client -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Remove Client</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../remove/removeClient.jsp">Remove Client</a></li>
							<li><a href="../viewRemovalInfo.do">Search</a></li>
						</ul>
					</li>
					<!-- Pending Commands -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Pending MML Commands</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../ViewClientsWithPendingMMLCommands.do">Clients With Pending Commands</a></li>
						</ul>
					</li>

					<!-- Exchange -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Exchange</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../exchange/addExchange.jsp">Add Exchange</a></li>
							<li><a href="../ViewExchange.do">Search</a></li>
						</ul>
					</li>
					
					<!-- DSLM -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>DSLM</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../dslm/addDslm.jsp">Add DSLM</a></li>
							<li><a href="../ViewDslm.do">Search</a></li>
						</ul>
					</li>
          	
					<!-- Prepaid payment -->
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Payment</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../prepaidpayment/AddPayment.jsp">Add New Payment</a></li>
							<li><a href="../viewPrepaidPayment.do">Search Payments</a></li>
						</ul>
					</li>
			</ul>
       </li>
			
            <li class="treeview">
              <a href="#"><i class='fa fa-users'></i> <span>Users</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu">
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Manage Users</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="#">Add User</a></li>
						<li><a href="#">Search</a></li>
					</ul>
				</li>
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Manage Roles</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="#">Add Role</a></li>
						<li><a href="#">Search</a></li>
					</ul>
				</li>
              </ul>
            </li>
			
			<li><a href="#"><i class="fa fa-globe"></i> <span>Domains</span></a></li>
			
			<li class="treeview">
              <a href="#"> <i class="fa fa-cloud-download"></i> <span>Web Hosting</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu">
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Package</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="#">Add Package</a></li>
						<li><a href="#">Search</a></li>
						<li><a href="#">Available packages</a></li>
					</ul>
				</li>
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Colocations</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="#">Search</a></li>
					</ul>
				</li>
              </ul>
            </li>
			
			<li><a href="#"><i class="fa fa-ellipsis-h"></i> <span>IP Address</span></a></li>
			
			<li class="treeview">
				<a href="#"><i class="fa fa-wifi"></i> <span>ISP</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu">
					<li>
						<a href="#"><i class='fa fa-circle-o'></i> <span>ADSL</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li><a href="#"><i class='fa fa-circle-o'></i>Package<i class="fa fa-angle-right pull-right"></i></a>
								<ul class="treeview-menu">
									<li><a href="#"><i class='fa fa-circle-o'></i>Package<i class="fa fa-angle-right pull-right"></i></a>
										<ul class="treeview-menu">
											<li class="topline"><a href="../package/prepaidPackageAdd.jsp">Add Prepaid Package</a></li>
											<li><a href="../package/postpaidPackageAdd.jsp">Add Postpaid Package</a></li>
											<li><a href="../viewPackage.do"> Search</a></li>
										</ul>
									</li>
									
									<!-- Bonus -->
									<li><a href="#"><i class='fa fa-circle-o'></i>Bonus<i class="fa fa-angle-right pull-right"></i></a>
										<ul class="treeview-menu">
											<li class="topline"><a href="../bonus/AddBonus.jsp">Add Bonus </a></li>
											<li><a href="../ViewBonus.do"> Search</a></li>
										</ul>
									</li>
									<!-- End of Bonus -->

									<!-- Discount -->
									<li><a href="#"><i class='fa fa-circle-o'></i>Discount<i class="fa fa-angle-right pull-right"></i></a> 
										<ul class="treeview-menu">
											<li class="topline"><a href="../discount/AddDiscount.jsp">Add Discount </a></li>
											<li><a href="../ViewDiscount.do">Search Discount</a></li>
										</ul>
									</li>
								  
									<!-- Invoices -->
									<li><a href="#"><i class='fa fa-circle-o'></i>Invoices<i class="fa fa-angle-right pull-right"></i></a> 
										<ul class="treeview-menu">
											<li class="topline"><a href="../invoiceorder/addInvoiceOrder.jsp">Add Invoice Order </a></li>
											<li><a href="../ViewInvoiceOrder.do">Search Invoice Order</a></li>
											<li><a href="../ViewInvoice.do">Search Invoice</a></li>
										</ul>
									</li>
										 
									<!-- End of Discount -->
									<li><a href="#"><i class='fa fa-circle-o'></i>Region<i class="fa fa-angle-right pull-right"></i></a> 
										<ul class="treeview-menu">
											<li class="topline"><a href="../region/regionAdd.jsp">Add Region</a></li>
											<li><a href="../ViewRegion.do">Search</a></li>
										</ul>
									</li>
							   
								</ul>
							</li><!-- End Of Region -->
							
							<li class="treeview"><a href="#"><i class='fa fa-circle-o'></i>Operators<i class="fa fa-angle-right pull-right"></i></a>
								<ul class="treeview-menu">
									<li>
										<a href="#"><i class='fa fa-circle-o'></i>Recharge Operator<i class="fa fa-angle-right pull-right"></i></a> 
										<ul class="treeview-menu">
											<li class="topline"><a href="../rechargeoperator/addRechargeOperator.jsp">Recharge</a></li>
											<li><a href="../ViewRechargeOperator.do">Search</a></li>
										</ul>
									</li>
									
									<li>
										<a href="#"><i class='fa fa-circle-o'></i>Commission<i class="fa fa-angle-right pull-right"></i></a> 
										<ul class="treeview-menu">
											<li class="topline"><a href="../operatorcommission/EditCommission.jsp">Edit Commission</a></li>
											<li><a href="../ViewOperatorCommission.do">View Commission</a></li>	
										</ul>
									</li>
								</ul>
						   </li>

						</ul>
					</li>
					<li><a href="../../ViewHostingdomain.do"><i class='fa fa-circle-o'></i>VPN</a></li>
				</ul>
			</li>
			
		    <li><a href="#"><i class="fa fa-server"></i> <span>IIG</span></a></li>
			<li><a href="#"><i class="fa fa-files-o"></i> <span>Invoice</span></a></li>
			
			<li class="treeview">
				<a  href="#"><i class="fa fa-list-ul"></i> <span>Reports</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu">	
					<li><a href="../ViewHostingdomainreport.do" target="blank"><i class='fa fa-circle-o'></i>Domain</a></li>
					<li><a href="#"><i class='fa fa-circle-o'></i>WebHosting</a></li>
					<li><a href="#"><i class='fa fa-circle-o'></i>IP Address</a></li>
					<li>
						<a  href="#"><i class="fa fa-circle-o"></i><span>ISP</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li class="treeview">
								<a  href="#"><i class="fa fa-circle-o"></i><span>ADSL</span> <i class="fa fa-angle-right pull-right"></i></a>
								<ul class="treeview-menu">
									<li>
										<a  href="#"><i class="fa fa-circle-o"></i><span>General Reports</span> <i class="fa fa-angle-right pull-right"></i></a>
										<ul class="treeview-menu">
											<li class="topline"><a href="../GeneralReports.do?type=1">Type Of Connectivity </a></li>
											<li><a href="../GeneralReports.do?type=2">BandWidth Consumtion</a></li>
											<li><a href="../GeneralReports.do?type=3"> Billing Packages</a></li>
											<li><a href="../GeneralReports.do?type=4"> Customer Details</a></li>
											<li><a href="../GeneralReports.do?type=5">Region Wise</a></li>
											<li><a href="../GeneralReports.do?type=6"> System Admin</a></li>
											<li><a href="../GeneralReports.do?type=7">Tariff Details </a></li>
											<li><a href="../GeneralReports.do?type=8">Traffic Details </a></li>
											<li><a href="../GeneralReports.do?type=9">Usage Detail </a></li>
											<li><a href="../GeneralReports.do?type=10">Package Wise</a></li>
											<li><a href="../GeneralReports.do?type=13">Client Status</a></li>
											<li><a href="../GeneralReports.do?type=15">Exchange Wise</a></li>
											<li><a href="../GeneralReports.do?type=16">Password Change</a></li>	
										</ul>
									</li>
						  
									<!-- Report Creation wizard -->
									<li><a href="../wizard/singleReportWizard.jsp"><i class="fa fa-circle-o"></i> Report Wizard</a></li>
									<!-- Report Creation Wizard End -->
									
									<!-- Customized Report -->
									<li>
										<a  href="#"><i class="fa fa-circle-o"></i><span>Monitoring Reports</span><i class="fa fa-angle-right pull-right"></i></a>
										<ul class="treeview-menu">
											<li class="topline"><a href="../ViewRecord.do">Search Log</a></li>
											<li class="topline"><a href="../ViewMMLLog.do">Search MML Log</a></li>
											<li><a href="../ViewUsage.do">Search Usage</a></li>
									   </ul>
								    </li>

									<li><a href="../ViewReportAction.do"><i class="fa fa-circle-o"></i>Saved Reports</a></li>
									<li><a href="../wizard/downloads.jsp"><i class="fa fa-circle-o"></i>Downloads</a></li>
									<li><a href="../wizard/downloadCdr.jsp"><i class="fa fa-circle-o"></i>Download CDR</a></li>
									
								</ul>
								
							</li>
							<li><a href="#"><i class="fa fa-circle-o"></i>VPN</a>
							
							</li>
						</ul>
					</li>
					<li><a href="#"><i class="fa fa-circle-o"></i>IIG</a></li>
				</ul>	
			</li>
			
			
			<li class="treeview">
				<a  href="#"><i class="fa fa-info-circle"></i><span>Help Desk</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu">
					<li>
						<a  href="#"><i class="fa fa-circle-o"></i><span>Help Desk(User)</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li class="topline"><a href="../ViewReply.do">Send/Add Reply</a></li>
						</ul>
					</li>
					<li>
						<a  href="#"><i class="fa fa-circle-o"></i><span>Complain Category</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li class="topline"><a href="../complain/complainAdd.jsp"> Add</a></li>	
							<li><a href="../ViewComplain.do">Search</a></li>
						</ul>
					</li>
				</ul>
           </li>
			
			
        </ul><!-- /.sidebar-menu -->
	</section><!-- /.sidebar -->
	</aside>
	  

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <section class="content-header">
          <h1>
            Home
            <small>Optional description</small>
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
            <li class="active">Here</li>
          </ol>
        </section>
		
        <!-- Main content -->
        <section class="content">
		
			<div class="row">
				<div class="col-lg-3 col-xs-6">
				 <a href="#">
					  <!-- small box -->
					  <div class="small-box bg-aqua">
						<div class="small-box-header">DOMAINS</div>
						<div class="inner">
						  <h3>150</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-globe"></i>
						 
						</div>
					  </div>
				 </a>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6">
				 <a href="#">
					  <!-- small box -->
					  <div class="small-box bg-green">
						<div class="small-box-header">WEB HOSTING</div>
						<div class="inner">
						  <h3>53</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-cloud-download"></i>
						</div>
					  </div>
				  </a>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6">
				    <a href="#">
					  <!-- small box -->
					  <div class="small-box bg-yellow">
						<div class="small-box-header">ISP</div>
						<div class="inner">
						  <h3>44</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-wifi"></i>
						</div>
					  </div>
				  </a>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6">
					<a href="#">
					  <!-- small box -->
					  <div class="small-box bg-purple">
						<div class="small-box-header">IIG</div>
						<div class="inner">
						  <h3>65</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-server"></i>
						</div>
					  </div>
				  </a>
				</div><!-- ./col -->
          </div>
		  
		  <div class="row">
				<div class="col-lg-3 col-xs-6">
				<a href="#">
					  <!-- small box -->
					  <div class="small-box bg-blue">
						<div class="small-box-header">TICKET</div>
						<div class="inner">
						  <h3>150</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-ticket"></i>
						</div>
					  </div>
				  </a>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6">
				 <a href="#">
					  <!-- small box -->
					  <div class="small-box bg-red">
						<div class="small-box-header">INVOICE</div>
						<div class="inner">
						  <h3>53</h3>
						</div>
						<div class="icon">
						  <i class="fa fa-newspaper-o"></i>
						</div>
					  </div>
				  </a>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6 ip-address">
				  <!-- small box -->
				  <div class="small-box bg-teal">
					<div class="small-box-header">IP ADDRESS</div>
					<div class="ip-caret">
					  <i class="fa fa-caret-right"></i>
					</div>
					<div class="inner">
					  <h3>44</h3>
					</div>
					<div class="icon">
					  <i class="fa fa-ellipsis-h"></i>
					</div>
				  </div>
				</div><!-- ./col -->
				<div class="col-lg-3 col-xs-6" id="ip_submenu">
				  <!-- small box -->
				  <div class="small-box bg-teal">
					<div class="small-box-header">Category</div>
					<a href="#">
						<div class="inner">
						  <h3 class="pull-left">VPN</h3>
						  <h3 class="pull-right">44</h3>
						</div>
					</a>	
					<a href="#">
						<div class="inner">
						  <h3 class="pull-left">ADSL</h3>
						  <h3 class="pull-right">44</h3>
						</div>
					</a>
					
				  </div>
				</div><!-- ./col -->
          </div>
		  
		  <div class="row">
			  <div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title">Account Information</h3>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="address">
								<p>Md. Rafiqul Islam (REVE Systems)</p>
								<p>199/2-middle badda, 199/2-middle badda</p>
								<p>Dhaka, Dhaka, 1212</p>
								<p>Bangladesh</p>
								<p><a href="mailto:rafiqul.islam@revesoft.com">rafiqul.islam@revesoft.com</a></p>
								<div class="divider"></div>
								<a href="#" class="btn btn-success"><span class="icon"></span>Update Your Details</a>
							</div>
						</div><!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title">Account Overview</h3>
						</div><!-- /.box-header -->
						<div class="box-body">
							<ul class="list-unstyled account-overview">
								<li>
									<strong>Number of Products/Services:</strong> 
									
									<a class="go-arrow" href="#">
									<span class="icon"></span>
									</a>
									<a href="#" class="badges">
									<span class="badge-total">0</span>
									<span class="badge-active">0</span>
									</a>
									
								</li>
								
								<li>
									<strong>Number of Domains:</strong>
									
									<a class="go-arrow" href="#">
										<span class="icon"></span>
									</a>	            
									
									<a href="#" class="badges">
									 <span class="badge-total">0</span>
									 <span class="badge-active">0</span>
									</a> 
								</li>
								<li>
									<strong>Billing Information: &nbsp; </strong> 
									Use Default (Set Per Order) 				
								 </li>
							</ul>
						</div><!-- /.box-body -->
					</div>
				</div>
			</div>
			
			<div class="row">
			  <div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title">Your Active Products/Services</h3>
						  <a href="#" class="btn btn-success"><span class="icon"></span>View All</a>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="address">
								<p>It appears you do not have any products/services with us yet. <a href="cart.php">Place an order to get started</a>.</p>
							</div>
						</div><!-- /.box-body -->
					</div>
				</div>
				
				<div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title">Recent Support Tickets</h3>
						  <a href="#" class="btn btn-success"><span class="icon"></span>Open New Ticket</a>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="address">
								<p>No Recent Tickets Found. If you need any help, please <a href="submitticket.php">open a ticket</a>.</p>
							</div>
						</div><!-- /.box-body -->
					</div>
				</div>
			</div>
			
			<div class="row">
			  <div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title"> Register a New Domain</h3>
						  
						</div><!-- /.box-header -->
						<div class="box-body">
							<form action="" method="post">
								<div class="input-group margin-10">
									<input type="text" class="form-control" name="domain">
									<div class="input-group-btn">
										<input type="submit" class="btn btn-success" value="Register">
										<input type="submit" class="btn" value="Transfer" name="transfer">
									</div>
								</div>
							</form>
						</div><!-- /.box-body -->
					</div>
				</div>
				
				<div class="col-xs-6">
				    <div class="box">
						<div class="box-header with-border">
						  <h3 class="box-title"> Recent News</h3>
						  <a href="#" class="btn btn-success"><span class="icon"></span>View All</a>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="list-group">
								<a id="ClientAreaHomePagePanels-Recent_News-0" class="list-group-item" href="" menuitemname="0">
									Operational Support Procedures for System Outages<br><span class="text-last-updated">29/07/2012</span>
								</a>
								<a id="ClientAreaHomePagePanels-Recent_News-1" class="list-group-item" href="" menuitemname="1">
									Release Note of Web Application<br><span class="text-last-updated">11/10/2011</span>
								</a>
								<a id="ClientAreaHomePagePanels-Recent_News-2" class="list-group-item" href=#" menuitemname="2">
									Eicra.com has launched specials security package.  <br><span class="text-last-updated">30/05/2011</span>
								</a>
							</div>
						</div><!-- /.box-body -->
					</div>
				</div>
			</div>
		  
		  
        </section>
		
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <footer class="main-footer">
        <div class="pull-right hidden-xs">
          Anything you want
        </div>
        <strong>Copyright &copy; 2016 <a href="#">Company</a>.</strong> All rights reserved.
      </footer>

    </div><!-- ./wrapper -->

  </body>
</html>