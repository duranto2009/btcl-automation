<script language="JavaScript" src="../scripts/script.js"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/menu.css">
<script language="JavaScript" src="../scripts/menu.js"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/style.css">

<ul class="menu" id="menu">

	
		<li style="width:80px"><a  href="#" class="menulink">Domains</a>
		<ul>
		<li><a href="../ViewHostingdomain.do">Order Track</a></li>
		<li><a href="../hosting/buydomain.jsp">Buy domain</a></li>
		</ul>
		</li>	
	
	<li style="width:120px"><a  href="#" class="menulink">Web Hosting</a>		
	<ul>	
		<li><a href="#">Package</a>
		<ul>
		<li><a href="../ViewHostingPackage.do">Order Track</a></li>
		<li><a href="../ViewHostingpackage2.do">Available packages</a></li>
		</ul>
		</li>				
	
		<li><a  href="#">Colocations</a>
		<ul>
		<li><a href="../hosting/myColocations.jsp">Order Track</a></li>
		<li><a href="../hosting/colocations.jsp">Available Colocations</a></li>
		</ul>
		</li>				
	</ul>	
	</li>
		

		<li style="width:120px"><a href="#" class="menulink">IP Address</a></li>		
		<li style="width:70px"><a href="#" class="menulink">ISP</a>
		<ul>
		<li><a  href="#">ADSL</a>		
		<ul>	
			<li><a href="../clientModule/packageDetails.jsp">Package</a></li>
			<li><a href="../clientModule/getUsage.jsp">Usage History</a></li>
			<li><a href="../clientModule/viewClientRechargeInfo.do">Recharge History</a></li>
		</ul>
		</li>
		<li><a href="#">VPN</a></li>
		</ul>
		</li>		
		<li style="width:80px"><a  href="#" class="menulink">IIG</a>
		<ul>
		<li><a href="../ViewIig.do">Order Track</a></li>
		<li><a href="../iig/iigAdd.jsp">Buy Bandwidth</a></li>
		</ul>
		</li>		
		<li style="width:70px"><a href="../ViewHostingPayment.do" class="menulink">Invoice</a>
    <li style="width:80px"><a href="#" class="menulink">Help Desk</a> 
    <ul>
          	<li><a href="../help/helpAdd.jsp">Add New Complain</a></li>
            <li><a href="../ViewHelp.do">Search/Get Answer</a></li>
         </ul>
    </li>
    <li style="width:80px"><a href="../showClientSummary.do?id=<%=loginDTO.getAccountID()%>" class="menulink">Profile</a></li>
     
    	
	
</ul>
<script type="text/javascript">
	var menu=new menu.dd("menu");
	menu.init("menu","menuhover");
</script>
