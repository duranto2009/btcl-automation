

<%@page import="report.ReportConfigurationService"%>
<%@page import="report.ReportConfigurationDTO"%>
<%@page import="java.util.*"%>

<script language="JavaScript" src="../scripts/script.js"></script>
<!-- <link rel="stylesheet" type="text/css" href="../stylesheets/menu.css">
<script language="JavaScript" src="../scripts/menu.js"></script> -->
<link rel="stylesheet" type="text/css" href="../stylesheets/style.css">
<!-- <style>
.horizontal-nav {
  background: #efefef;
  border-radius: 6px;
}
.horizontal-nav ul {
  background: #128F9A;
  float: left;
  text-align: center;
  border-radius: 6px;
  border: 1px solid #0e7079;
}
.horizontal-nav ul li {
  float: left;
  border-left: 1px solid #0e7079;
}
.horizontal-nav ul li:first-child {
  border-left: 0 none;
}
.horizontal-nav ul li a {
  display: block;
  padding: 10px 20px;
  color: #fff;
  border-top: 1px solid rgba(255,255,255, 0.25);
  border-left: 1px solid rgba(255,255,255, 0.25);
}
.horizontal-nav ul li:first-child a {
  border-left: 0 none;
}
.horizontal-nav ul li a:hover {
  background: #12808a;
}
.horizontal-nav ul li:first-child a {
  border-top-left-radius: 6px;
  border-bottom-left-radius: 6px;
}
.horizontal-nav ul li:last-child a {
  border-top-right-radius: 6px;
  border-bottom-right-radius: 6px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	  $('.full-width').horizontalNav({});
	});
</script> -->
<nav class="horizontal-nav full-width horizontalNav-notprocessed">
<ul class="menu" id="menu">
	
	
	

                <!-- Clients -->


 
<%@include file="menucomponents/menu_client.jsp"%>
<%@include file="menucomponents/menu_user.jsp"%>
<%@include file="menucomponents/menu_domain.jsp"%>
<%@include file="menucomponents/menu_hosting.jsp"%>
<%@include file="menucomponents/menu_ip.jsp"%>
<%@include file="menucomponents/menu_isp.jsp"%>
<%@include file="menucomponents/menu_iig.jsp"%>
<!-- <li style="width:80px"><a href="../ViewHostingPayment.do" class="menulink">Invoice</a></li> -->
<li style="width:80px"><a href="../ViewOrder.do" class="menulink">Invoice</a></li>
<%@include file="menucomponents/menu_report.jsp"%>
<%@include file="menucomponents/menu_help.jsp" %>
<%@include file="menucomponents/menu_config.jsp" %>

<%-- <%@include file="menucomponents/menu_package_adsl.jsp"%>
<%@include file="menucomponents/menu_report_adsl.jsp"%>
<%@include file="menucomponents/menu_config.jsp"%> --%>


<!-- Shared Secret -->
  
                <!-- Accounts -->
 <%-- <%if (loginDTO.getMenuPermission(login.PermissionConstants.ACCOUNTS) != -1) 
 {%>
    <li style="width:84px"><a href="#" class="menulink">Accounts</a>
      	<ul>
        	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_ACCOUNTS) != -1) 
        	{%>
                 <li><a href="#" class="sub">Manage Accounts </a> 
	                <ul>
                    	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_ACCOUNTS_CLIENT_BILL_REPORT) != -1) 
                    	{%>
                    		<li class="topline"><a href="../searchBill.do">Search Bill</a></li>
                        <%}%>
						<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_ACCOUNTS_VIEW_PAYMENT) != -1) 
						{%>
                   			<li><a href="../viewRechargeInfo.do">View Recharge</a></li>
                       <%}%>
                    	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_ACCOUNTS_MULTIPLE_BILL_GENERATION) != -1) 
                    	{%>
                    		<li><a href="../getClientsForBill.do"> Manual Bill Generation</a></li>
                        <%}%>
                	</ul>
                </li>
           <%}%>
          </ul>
       </li>
  <%}%> --%>

                <!-- Reports -->


<%--  <%if (loginDTO.getMenuPermission(login.PermissionConstants.VPN) != -1) 
 {%>
 	<li style="width:68px"><a href="#" class="menulink">VPN</a>
 		<ul>
	 		<%if (loginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT) != -1) 
	 		{%>
	 			<li><a href="#" class="sub">VPN Client</a> 
	 				<ul>
	 					 <%if (loginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_ADD) != -1) 
	 					 {%>
	 					 	<li class="topline"><a href="../vpnclients/clientAdd.jsp">Add</a></li>
	 					 <%}%>
	 					 <%if (loginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_SEARCH) != -1) 
	 					 {%>
	 					 	<li class="topline"><a href="../ViewVPNClient.do">Search</a></li>
	 					 <%}%>	 					 
	 				</ul>
	 			</li>
	 		<%} %>
	 		<%if (loginDTO.getMenuPermission(login.PermissionConstants.MSG_FORMAT) != -1) 
			  {%>
	                <li><a href="#" class="sub">Manage Message Formats</a> 
	               		<ul>
							<%if (loginDTO.getMenuPermission(login.PermissionConstants.MSG_FORMAT_SEARCH) != -1) 
							{%>
	                    		<li class="topline"><a href="../ViewMessageFormat.do"> Message Formats</a></li>
	                       <%}%>
	               		</ul>
	               	</li>
	          <%}%>
			<%if (loginDTO.getMenuPermission(login.PermissionConstants.MSG) != -1) 
			{%>
	             <li><a href="#" class="sub">Messages</a> 
	             	<ul>
						<%if (loginDTO.getMenuPermission(login.PermissionConstants.MSG_SEARCH) != -1) 
						{%>
	                    	<li class="topline"><a href="../ViewMessage.do">View Messages</a></li>
	                   <%}%>
	                   
	                   <%if (loginDTO.getMenuPermission(login.PermissionConstants.SEND_MAIL) != -1) 
						{%>
	                    	<li class="topline"><a href="../message/SendMail.jsp">Send Mail</a></li>
	                   <%}%>
	                   
	                   
	                   <%if (loginDTO.getMenuPermission(login.PermissionConstants.SEND_SMS) != -1) 
						{%>
	                    	<li class="topline"><a href="../message/SendSms.jsp">Send Sms</a></li>
	                   <%}%>
	                </ul>
	              </li>
	        <%}%>
       </ul>
    </li>
<%}%> --%> 			



<!--  			<li style="width:68px"><a href="../hosting/admininvoices.jsp?domain=abc.bd&status=pending" class="menulink">Invoice</a></li> -->
	

<%-- <%if (loginDTO.getMenuPermission(login.PermissionConstants.RADIUS_CONFIGURATION) != -1) 
  {%>
  	<li><a href="#" class="menulink"> Others</a>
      	<ul>
         	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_SHARED_SECRET) != -1) 
         	{%>
               <li><a href="#" class="sub">Manage Shared Secrets</a> 
               		<ul>
                    	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_SHARED_SECRET_ADD) != -1) 
                    	{%>
                    		<li class="topline"><a href="../sharedsecret/sharedSecretAdd.jsp">Add Shared Secret</a></li>
                      	<%}%>
                    	<%if (loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_SHARED_SECRET_SEARCH) != -1) 
                    	{%>
                    		<li><a href="../ViewSharedSecret.do">Search</a></li>
                        <%}%>
                	</ul>
                </li>
            <%}%>
            <%if (loginDTO.getMenuPermission(login.PermissionConstants.RADIUS_PORT) != -1) 
            {%>
                <li><a href="#" class="sub">Manage Ports</a> 
	               <ul>
                    	<%if (loginDTO.getMenuPermission(login.PermissionConstants.RADIUS_PORT_CONFIGURE) != -1) 
                    	{%>
                    		<li class="topline"><a href="../ViewRadiusPortConfiguration.do">Configure Radius Ports</a></li>
                        <%}%>
                 	</ul>
                 </li>
            <%}%>
          </ul>
       </li>
 <%}%>
 --%>
 
 
 <!--  kayesh -->

		
				
 

   </ul>
</nav>          
                <!-- Customized Report End --> 
<script type="text/javascript">
	var menu=new menu.dd("menu");
	menu.init("menu","menuhover");
</script>
