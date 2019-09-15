<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">

		<!-- search form (Optional) -->
		<form action="#" method="get" class="sidebar-form">
			<div class="input-group">
				<input type="text" name="q" class="form-control" placeholder="Search..." /> <span class="input-group-btn">
					<button type='submit' name='search' id='search-btn' class="btn btn-flat">
						<i class="fa fa-search"></i>
					</button>
				</span>
			</div>
		</form>
		<!-- /.search form -->

		<!-- Sidebar Menu -->
		<ul class="sidebar-menu">
			<li class="header">All Menu</li>
			<!-- Optionally, you can add icons to the links -->
			<li class="active"><a href="../Welcome.do"><i class='fa fa-home'></i> <span>Home</span></a></li>

			
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
						</ul></li>
					<li><a href="../ipmanagement/buyIP.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Service</span></a></li>
				</ul>
			</li>



			<li class="treeview" id="domain_parent"><a href="#"><i class="fa fa-globe"></i> <span>Domains</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="domain">
					<li><a href="../ViewHostingdomain.do"><i class='fa fa-circle-o'></i> <span>My Services</span></a></li>
					<li><a href="../hosting/buydomain.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Domain
								Service</span></a></li>
				</ul></li>

			<li class="treeview" id="webhosting_parent"><a href="#"><i class="fa fa-cloud-download"></i> <span>Web
						Hosting</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="webhosting">
					<!-- <li><a href="../hostingpackage2/hostingPackageAdd.jsp">Add Package</a></li> -->
					<li><a href="../ViewHostingPackage.do">My Services</a></li>
					<li><a href="../ViewHostingpackage2.do">Available Packages</a></li>
				</ul></li>

			<li class="treeview" id="colocation_parent"><a href="#"><i class="fa fa-sitemap"></i> <span>Colocations</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="colocation">
					<li><a href="../colocation/colocations.jsp">My Services</a></li>
					<li><a href="../colocation/colocationAdd.jsp">Apply for Service</a></li>
				</ul></li>

			<!-- 			<li class="treeview" id="webhosting_parent">
              <a href="../ViewHostingdomain.do"> <i class="fa fa-cloud-download"></i> <span>Web Hosting</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu" id="webhosting">
                <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Package</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu" id="package">
						<li><a href="../hostingpackage2/hostingPackageAdd.jsp">Add Package</a></li>
						<li><a href="../ViewHostingPackage.do">Hosting Requests</a></li>
						<li><a href="../ViewHostingpackage2.do">Available Packages</a></li>
					</ul>
				</li>
              </ul>
            </li>
			<li class="treeview" id="webhosting_parent">
              <a href="../ViewHostingdomain.do"> <i class="fa fa-cloud-download"></i> <span>Web Hosting</span> <i class="fa fa-angle-right pull-right"></i></a>
              <ul class="treeview-menu" id="webhosting">            
                 <li>
					<a href="#"><i class='fa fa-circle-o'></i> <span>Colocations</span> <i class="fa fa-angle-right pull-right"></i></a>
					<ul class="treeview-menu" id="colocation">
						<li><a href="../colocation/colocationAdd.jsp">Add</a></li>
						<li><a href="../colocation/colocations.jsp">Search</a></li>
					</ul>
				</li>
				</ul>
				</li> -->


			<!-- <li><a href="#"><i class="fa fa-ellipsis-h"></i> <span>IP Address</span></a></li> -->

			<li class="treeview" id="ip_parent"><a href="#"><i class="fa fa-ellipsis-h"></i> <span>IP Management</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="ip">

					<li><a href="../ipmanagement/ip.jsp"><i class='fa fa-circle-o'></i> <span>My Services</span></a></li>
					<li><a href="../ipmanagement/buyIP.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Service</span></a></li>
				</ul></li>
				
			

			<li class="treeview" id="isp"><a href="#"><i class="fa fa-wifi"></i> <span>ISP</span> <i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="isp_child">

					<li class="treeview"><a href="#"><i class="fa fa-circle"></i> <span>LLI</span><i
							class="fa fa-angle-right pull-right"></i></a>
						<ul class="treeview-menu" id="lli">

							<li><a href="../ViewLli.do"><i class='fa fa-circle-o'></i> <span>My Services</span></a></li>
							<li><a href="../lli/lliAdd.jsp"><i class='fa fa-circle-o'></i> <span>Apply for LLI Service</span></a></li>
						</ul></li>
<!-- 					<li class="treeview"><a href="#"><i class="fa fa-circle"></i> <span>VPN</span><i -->
<!-- 							class="fa fa-angle-right pull-right"></i></a> -->
<!-- 						<ul class="treeview-menu" id="vpn"> -->
<!-- 							<li><a href="../vpn/vpnConnectionRequest.jsp"><i class='fa fa-circle-o'></i> <span>VPN Connection -->
<!-- 										Request</span></a></li> -->
<!-- 							<li><a href="../ViewVpn.do"><i class='fa fa-circle-o'></i> <span>My Services</span></a></li> -->
<!-- 												<li><a href="../vpn/vpnAdd.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Service</span></a></li>					 -->
<!-- 						</ul></li> -->

					<li><a href="../../BTCLBilling4/home/loginAuto.jsp"><i class='fa fa-circle'></i> <span>ADSL</span> </a></li>



				</ul></li>





			<li class="treeview" id="iig_parent"><a href="#"><i class="fa fa-globe"></i> <span>IIG</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="iig">

					<li><a href="../ViewIig.do"><i class='fa fa-circle-o'></i> <span>IIG Services</span> </a></li>
					<li><a href="../iig/iigAdd.jsp"><i class='fa fa-circle-o'></i> <span>Apply for Service</span> </a></li>
				</ul></li>

			<li class="treeview" id="finance_parent"><a href="#"><i class="fa fa-files-o"></i> <span>Finance</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="finance">

					<li><a href="../ViewOrder.do"><i class='fa fa-circle-o'></i> <span>Invoice</span> </a></li>
					<li><a href="../ViewHostingPayment.do"><i class='fa fa-circle-o'></i> <span>Payments</span> </a></li>
				</ul></li>

			<!-- 			<li class="treeview" id="crm"> 
				<a href="http://180.234.212.118:90/btcl-campaign/view/campaign-login.jsp"><i class="fa fa-files-o"></i> <span>CRM</span></a>			
			</li> -->

			<!-- <li><a href="../ViewOrder.do"><i class="fa fa-files-o"></i> <span>Invoice</span></a></li> -->




			<li class="treeview" id="help_parent"><a href="#"><i class="fa fa-info-circle"></i><span>Help Desk</span> <i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li><a href="../help/helpAdd.jsp"><i class="fa fa-circle-o"></i><span>Add New Complain</span></a></li>
					<li><a href="../ViewHelp.do"><i class="fa fa-circle-o"></i><span>Search/Get Answer</span> </a></li>
				</ul></li>

			<li class="treeview"><a href="#"><i class="fa fa-pencil-square"></i><span>Complaint</span><i
					class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li><a href="../complaint/complaintForm.jsp"><i class="fa fa-circle-o"></i><span>Submit New
								Complain</span></a></li>
					<li><a href="../SearchComplaint.do"><i class="fa fa-circle-o"></i><span>Search Complaint</span> </a></li>
				</ul></li>
			
				
			 <li class="treeview">
				<a  href="#"><i class="fa fa-pencil-square"></i><span>Recharge Account</span><i class="fa fa-angle-right pull-right"></i></a>
				<ul class="treeview-menu" id="help">
					<li>
						<a  href="../rechargecard/RechargeAccount.jsp"><i class="fa fa-circle-o"></i><span>Recharge</span></a>
						
					</li>
					
				</ul>
           </li>
           
           
           
         
			
			
			
				
			<li class="treeview"><a href="../clients/showClientSummary.jsp"><i class="fa fa-briefcase"></i><span>Profile</span></a>
			</li>




		</ul>
		<!-- /.sidebar-menu -->
	</section>
	<!-- /.sidebar -->
</aside>