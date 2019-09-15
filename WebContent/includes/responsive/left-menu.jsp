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
            <li class="active"><a href="../Welcome.do"><i class='fa fa-home'></i> <span>Home</span></a></li>

<!-- 			<li class="treeview">
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
					Shift Client
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Shift Client</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../shift/shiftClient.jsp">Shift Client</a></li>
							<li><a href="../viewShiftingInfo.do">Search</a></li>
						</ul>
					</li>
         	
					Migration
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Migrations</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../migration/addMigration.jsp">Add Migration</a></li>
							<li><a href="../ViewMigration.do">Search</a></li>
							<li><a href="../batchmigration/AddBatchMigration.jsp">Add Batch Migration</a></li>
							<li><a href="../ViewBatchMigration.do">Search Batch Migration</a></li>
						</ul>
					</li>
					Remove Client
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Remove Client</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../remove/removeClient.jsp">Remove Client</a></li>
							<li><a href="../viewRemovalInfo.do">Search</a></li>
						</ul>
					</li>
					Pending Commands
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Pending MML Commands</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../ViewClientsWithPendingMMLCommands.do">Clients With Pending Commands</a></li>
						</ul>
					</li>

					Exchange
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Exchange</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../exchange/addExchange.jsp">Add Exchange</a></li>
							<li><a href="../ViewExchange.do">Search</a></li>
						</ul>
					</li>
					
					DSLM
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>DSLM</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../dslm/addDslm.jsp">Add DSLM</a></li>
							<li><a href="../ViewDslm.do">Search</a></li>
						</ul>
					</li>
          	
					Prepaid payment
					<li>
						<a href="#"><i class='fa fa-user'></i> <span>Payment</span><i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu">
							<li ><a href="../prepaidpayment/AddPayment.jsp">Add New Payment</a></li>
							<li><a href="../viewPrepaidPayment.do">Search Payments</a></li>
						</ul>
					</li>
			</ul>
       </li> -->
			
            <li class="treeview">
              <a href="#"><i class='fa fa-users'></i> <span>Users</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu">
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Manage Users</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="<%=context%>users/userAdd.jsp">Add User</a></li>
						<li><a href="<%=context%>SearchUser.do">Search</a></li>
					</ul>
				</li>
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Manage Roles</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu">
						<li><a href="<%=context%>roles/roleAdd.jsp">Add Role</a></li>
						<li><a href="<%=context%>ViewRole.do">Search</a></li>
					</ul>
				</li>
              </ul>
            </li>
			
			<li><a href="../ViewHostingdomain.do"><i class="fa fa-globe"></i> <span>Domains</span></a></li>
			
<!-- 			<li class="treeview" id="webhosting_parent">
              <a href="../ViewHostingdomain.do"> <i class="fa fa-cloud-download"></i> <span>Web Hosting</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu" id="webhosting">
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Package</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu" id="package">
						<li><a href="../ViewHostingPackage.do">Services</a></li>											
						<li><a href="../ViewHostingpackage2.do">Available Packages</a></li>
						<li><a href="../hostingpackage2/hostingPackageAdd.jsp">Create New Package</a></li>						
					</ul>
				</li>
              </ul>
            </li> -->
			<li class="treeview" id="webhosting_parent"> 
				<a href="#"><i class="fa fa-cloud-download"></i> <span>Web Hosting</span><i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu" id="webhosting">
						<li><a href="../ViewHostingPackage.do">Services</a></li>											
						<li><a href="../ViewHostingpackage2.do">Available Packages</a></li>
						<li><a href="../hostingpackage2/hostingPackageAdd.jsp">Create New Package</a></li>
					</ul>
			</li>			
			
<!-- 			<li class="treeview" id="colocation_parent"> 
				<a href="#"><i class="fa fa-sitemap"></i> <span>Colocations</span><i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu" id="colocation">
						<li><a href="../colocation/colocations.jsp">Services</a></li>						
					</ul>
			</li>			 -->
			<li><a href="../colocation/colocations.jsp"><i class="fa fa-sitemap"></i> <span>Colocations</span></a></li>
			<li><a href="../ipmanagement/ip.jsp"><i class="fa fa-ellipsis-h"></i> <span>IP Management</span></a></li>
			
<!-- 			<li class="treeview" id="ip_parent"> 
				<a href="#"><i class="fa fa-ellipsis-h"></i> <span>IP Management</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="ip">

					<li><a href="../ipmanagement/ip.jsp"><i class='fa fa-circle-o'></i> <span>IP Requests</span></a></li>
					<li><a href="../ipmanagement/buyIP.jsp"><i class='fa fa-circle-o'></i> <span>Buy IP</span></a></li>					
				</ul>
			</li> -->
			
			<li class="treeview" id="vpn_parent"><a href="#"><i class="fa fa-ellipsis-h"></i> <span>VPN</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="ip">

					<li class="treeview"><a href="#"><i class="fa fa-circle"></i> <span>My Services</span><i
							class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu" id="vpn">
							<li><a href="<%=context%>vpn/connection/vpnConnectionRequest.jsp"><i class='fa fa-circle-o'></i> <span>New Connection Request</span></a></li>
							<li><a href="<%=context%>vpn/link/newLinkRequest.jsp"><i class='fa fa-circle-o'></i> <span>New Link Request</span></a></li>
							<li><a href="<%=context%>vpn/link/linkView.jsp"><i class='fa fa-circle-o'></i> <span>VPN Link View</span></a></li>
							<li><a href="<%=context%>vpn/client/clientAdd.jsp"><i class='fa fa-circle-o'></i> <span>Client Add</span></a></li>
							<li><a href="<%=context%>SearchClient.do"><i class='fa fa-circle-o'></i> <span>Client Search</span></a></li>
							<li><a href="<%=context%>GetCostConfig.do"><i class='fa fa-circle-o'></i> <span>Cost Config</span></a></li>
							<li><a href="<%=context%>SearchRequest.do"><i class='fa fa-circle-o'></i> <span>Search Request</span></a></li>
							
							
						</ul></li>
					<li><a href="../ipmanagement/buyIP.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Service</span></a></li>
				</ul>
			</li>
						
			
			<li class="treeview" id="isp">
				<a href="#"><i class="fa fa-wifi"></i> <span>ISP</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="isp_child">					
					<li class="treeview"> 
						<a href="#"><i class="fa fa-circle"></i> <span>LLI</span><i class="fa fa-angle-right pull-right"></i></a>
							<ul class="treeview-menu" id="lli">
								<li><a href="../ViewLli.do"><i class='fa fa-circle-o'></i> <span>Services</span></a></li>
					<!-- <li><a href="../lli/lliAdd.jsp"><i class='fa fa-circle-o'></i> <span>Apply for LLI Service</span></a></li> -->					
							</ul>
					</li>
		    		
			<li class="treeview"> 
				<a href="../../BTCLBilling4/home/loginAuto.jsp?admin"><i class='fa fa-circle'></i> <span>ADSL</span> </a>
			</li>
			
				</ul>
			</li>
			
		    <li><a href="../ViewIig.do"><i class="fa fa-server"></i> <span>IIG</span></a></li>
		    
		    <li class="treeview" id="finance_parent"> 
				<a href="#"><i class="fa fa-files-o"></i> <span>Finance</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="finance">

					<li><a href="../ViewOrder.do"><i class='fa fa-circle-o'></i> <span>Invoice</span>
							</a>
						</li>
					<li><a href="../ViewHostingPayment.do"><i class='fa fa-circle-o'></i> <span>Payments</span>
							</a>
						</li>
				</ul>
			</li>		    
		    
		    <li class="treeview">
				<a  href="#"><i class="fa fa-pencil-square"></i><span>Distance</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li>
						<a  href="../distance/updateDistance.jsp"><i class="fa fa-circle-o"></i><span>Update Distance</span></a>
						
					</li>
					
				</ul>
           </li>
           
           <li class="treeview">
				<a  href="#"><i class="fa fa-pencil-square"></i><span>Test</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li>
						<a  href="../ViewTest.do"><i class="fa fa-circle-o"></i><span>Search Test</span></a>
						
					</li>
					<li>
						<a  href="../test/testAdd.jsp"><i class="fa fa-circle-o"></i><span>Add Test</span></a>
						
					</li>
				</ul>
           </li>		
		    
<!-- 			<li><a href="../ViewOrder.do"><i class="fa fa-files-o"></i> <span>Invoice</span></a></li> -->
			
			<li class="treeview" id="report_parent">
				<a  href="#"><i class="fa fa-list-ul"></i> <span>Reports</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="report">	
					<li><a href="../ViewHostingdomainreport.do"><i class='fa fa-circle-o'></i>Domain</a></li>
					<li><a href="../ViewHostingPackageReport.do"><i class='fa fa-circle-o'></i>WebHosting</a></li>
					<li><a href="#"><i class='fa fa-circle-o'></i>IP Address</a></li>
					<li>
						<a  href="#"><i class="fa fa-circle-o"></i><span>ISP</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu" id="ispreport">
							<li><a href="../ViewLliReport.do"><i class="fa fa-circle-o"></i>LLI</a></li>
							<li class="treeview"><a  href="#"><i class="fa fa-circle-o"></i><span>ADSL</span></a>
<!-- 								<ul class="treeview-menu">
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
						  
									Report Creation wizard
									<li><a href="../wizard/singleReportWizard.jsp"><i class="fa fa-circle-o"></i> Report Wizard</a></li>
									Report Creation Wizard End
									
									Customized Report
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
									
								</ul> -->
								
							</li>
							<li><a href="../ViewVpnReport.do"><i class="fa fa-circle-o"></i>VPN</a></li>
						</ul>
					</li>
					<li><a href="../ViewIigReport.do"><i class="fa fa-circle-o"></i>IIG</a></li>
					<li><a href="../report/ActivityLog.jsp"><i class='fa fa-circle-o'></i>Activity Log</a></li>
					<li><a href="../report/Inventory.jsp"><i class='fa fa-circle-o'></i>Inventory</a></li>
				</ul>	
			</li>
			
			<li class="treeview" id="crm"> 
				<a href="http://180.234.212.118:90/btcl-campaign/view/campaign-login.jsp"><i class="fa fa-files-o"></i> <span>Campaign Management</span></a>			
			</li>			
			
			<li class="treeview" id="help_parent">
				<a  href="#"><i class="fa fa-info-circle"></i><span>Help Desk</span> <i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li>
						<a  href="#"><i class="fa fa-circle-o"></i><span>Help Desk(User)</span> <i class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu" id="help_search">
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