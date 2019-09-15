<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="packages.PackageRepository"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="packages.PackageConstants"%>
<%@page import="billing.BillDTO"%>
<%@page import="billing.BillService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import = "report.*" %>
<%@page import = "java.util.*" %>

<%@page import = "client.*" %>
<%@ page import="
                  java.util.*,
				
 				 sessionmanager.SessionConstants,
				help.*,databasemanager.*,java.lang.*" %>




<%

ArrayList billIds = new ArrayList();
String billIdsString = (String)session.getAttribute("id");
System.out.println("id is "+billIdsString);
String[] billIdsArray = billIdsString.split(":");
System.out.println("array first elem "+billIdsArray[0]);
for(int j = 0;j<billIdsArray.length;j++)
{
	   billIds.add(billIdsArray[j]);
}


BillService billService = new BillService();
ArrayList<BillDTO> bills = (ArrayList<BillDTO>)billService.getDTOs(billIds);

String realContextPath = request.getSession().getServletContext().getRealPath("");
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
    <script language="JavaScript" src="../scripts/util.js"></script>
    <script language="JavaScript" src="../scripts/script.js"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/menu.css">
<script language="JavaScript" src="../scripts/menu.js"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/style.css">
<title>Bill</title>
</head>

<body class="body_center_align" onload="showPullDown('pullDownClient');">
        <table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber1">
            <tr>
                <td width="100%">
                 
                </td>
            </tr>
            <tr>
                <td width="100%">
                    <table border="0" cellpadding="0" cellspacing="0" width="1024" id="AutoNumber2">
                        <tr>
                           
                            <td width="1024" valign="top" class="td_main" align="center">
                                <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
                                    <tr>
                                        
                                    </tr>

<% %>
<!-- Start of Data Header -->
<tr><td width="1024" valign="top" class="td_main" align="center">
<table width = "700" style="font-family: Arial; color: #000000; border-collapse:collapse" cellpadding="3" cellspacing="0" border="0" width="898" bordercolor="#000000" align="center">
                <tr> 
                <%
                String date = "";
                if(billIds.size()<=0)
                {%>
         			<td><p><h2>No Data Found</h2></p></td></tr>
                <%}
                else
                {
                	for(int i=0;i<bills.size();i++)
                	{
                	date = format.format(new Date(bills.get(i).getIssueDate()));
                	
					int unitType = bills.get(i).getUnitType();
					
					String unit = "";
					String unitStr = "";
					
					if(PackageConstants.UNIT_TYPE_PER_KB == unitType)
					{
						unit = "KB";
						unitStr = "/KB";
					}
					else if(PackageConstants.UNIT_TYPE_PER_MB == unitType)
					{
						unit = "MB";
						unitStr = "/MB";
					}
					else if(PackageConstants.UNIT_TYPE_PER_MIN == unitType)
					{
						unit = "Minutes";
						unitStr = "/Minute";
					}
					
                	%>
                	<tr>
                		<td colspan="4" align="center">
                			<img src="<%="../images/common/btcl.png"%>" align="top">
                		</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 12pt" align="center">
                 		Bangladesh Telecommunications Company Ltd.
                 		</td>
                 	</tr>
                 	<tr>
                 	<td>&nbsp;</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 10pt" align="right">
                 		Subscriber Copy
                 		</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 10pt" align="center">
                 		ADSL Bill
                 		</td>
                 	</tr>
                 	<tr>
				<td colspan="4">&nbsp;</td>
                 	
                 	</tr>
                 	<tr>
                 		<td style="font-weight: bold;font-size: 8pt" align="left">
                 		Customer Information:
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Name: "+bills.get(i).getName() %>
                 		</td>
                 		<td align="left">
                 		<%="Billing Period: "+format.format(new Date(bills.get(i).getDateFrom()))+" to "+format.format(new Date(bills.get(i).getDateTo())) %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Address: "+bills.get(i).getAddress() %>
                 		</td>
                 		<td align="left">
                 		<%="Issue Date: "+date %>
                 		</td>
                 	</tr>
               		<tr>
                 		<td align="left" colspan="3">
                 		&nbsp;
                 		</td>
                 		<td align="left">
                 		<%="Last Date of Payment: <br/>"+bills.get(i).getLastDateOfPayment() %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Account No: "+bills.get(i).getAccountNo() %>
                 		</td>
                 		<td align="left">
                 		<%="Bill No: "+bills.get(i).getBillNo() %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="User ID: "+bills.get(i).getUserID() %>
                 		</td>
                 		<td align="left">
                 		<%="Bill Cycle: "+bills.get(i).getBillCycle() %>
                 		</td>
                 	</tr>
                 	<%
                 	int subscriberType = bills.get(i).getSubscriberType();
    				String subscriberTypeStr = "";
    				subscriberTypeStr = ClientConstants.TYPEOFCONNECTION_NAME[subscriberType -1];
    				
                 	%>
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Subscriber Type: "+subscriberTypeStr %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Package: "+bills.get(i).getPackageName() %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Telephone Number With Area Code: "+"0"+bills.get(i).getTelNoWithAreaCode() %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		&nbsp;
                 		</td>
                 		<td align="left">
                 		<%="Amount " %>
                 		</td>
                 	</tr>
                 	<tr>
                 	<td colspan="3">&nbsp;</td>
                 	</tr>
                 	<tr>
                 	<%
                 	if(bills.get(i).getIsUnlimited() == 1){
                 		%>
                 		<td align="left" colspan="3">
					<%="Monthly Fixed charge(Unlimited)"%>
					</td>
					<%
				}else{
					
					
					%>
					<td align="left" colspan="3">
					<%="Monthly Fixed charge("+PackageRepository.getInstance().getPackage(bills.get(i).getPackageID()).getVolumeLimit()+" "+unit+" Monthly Traffic Data)"%>
					</td>
			<% 	}
				%>
				<td align="left">
				<%="Tk= "+bills.get(i).getMonthlyCharge() %>
				</td>
				</tr>
				<tr>
					<td align="left" colspan="3">
					<%="For Additional Use: "+bills.get(i).getAddtionalDataUsed()/(1024*1024)+" "+unit+"@  "+bills.get(i).getAdditionalUsageRate()+" Tk."+unitStr %>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getAdditionalUsageCharge() %>
					</td>
				</tr>
				<tr>
                 	<td colspan="4"><hr/></td>
                 	</tr>
				<tr>
					<td>&nbsp;
					</td>
					<td align="left" colspan="2">
					<%="Registration Charges" %>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getRegistrationCharge() %>
					</td>
				</tr>
				<tr>
				<td>&nbsp;
					</td>
					<td align="left" colspan="2">
					<%="Service Charges" %>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getServiceCharge() %>
					</td>
				</tr>
				<tr>
				<td>&nbsp;
					</td>
					<td align="left" colspan="2">
					<%="Other Charges" %>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getTotalOtherCharge() %>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
                 	<td colspan="3"><hr/></td>
                 	</tr>
				<tr>
				<td colspan="2">&nbsp;
					
					</td>
					<td align="left">
					<%="Total Charge" %>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getTotalCharge() %>
					</td>
				</tr>
				<tr>
				<td colspan="2">&nbsp;
					<%
					double vatAmount = subscriberType== ClientDTO.CUSTOMER_TYPE_SERVICE?0: PackageRepository.getInstance().getPackage(bills.get(i).getPackageID()).getVat();
					%>
					</td>
					<td align="left">
					<%="Add. VAT("+vatAmount+"% of Charges)"%>
					</td>
					<td align="left">
					<%="Tk= "+(bills.get(i).getTotalCharge()*vatAmount/100) %>
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
                 	<td colspan="2"><hr/></td>
                 	</tr>
				<tr>
				<td colspan="2">&nbsp;
					
					</td>
					<td align="left">
					<%="Total  Payable Charge"%>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getTotalPayableCharge() %>
					</td>
				</tr>
				<tr>
				<td colspan="2">&nbsp;
					
					</td>
					<td align="left">
					<%="Previous Bill Amount"%>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getPreviousCharge() %>
					</td>
				</tr>
				<tr>
				<%double discount = bills.get(i).getTotalPayableCharge() - bills.get(i).getNetPayableCharge()+bills.get(i).getPreviousCharge(); %>
				<td colspan="2">&nbsp;
					
					</td>
					<td align="left">
					<%="Discount"%>
					</td>
					<td align="left">
					<%="Tk= "+discount %>
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
                 	<td colspan="2"><hr/></td>
                 	</tr>
				<tr>
				<td colspan="2">&nbsp;
					
					</td>
					<td align="left">
					<%="Net Payable Charge"%>
					</td>
					<td align="left">
					<%="Tk= "+bills.get(i).getNetPayableCharge() %>
					</td>
				</tr>
				
				
				<tr>
				<td align="left" colspan="2">
					<div style="border:1px solid black;">
					<table>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					<%="Advertisement"%>
					</td>
					</tr>
					</table>
					</div>
					</td>
				</tr>
				<tr>
				<td colspan="3">&nbsp;</td>
                 	<td><hr/></td>
                 	</tr>
				<tr>
					<td align="left" colspan="3">
					&nbsp;
					</td>
					<td align="center">
					Signature of Authority
					</td>
				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
				
				<tr>
				<td>&nbsp;
				</td>
				<td>&nbsp;</td>				</tr>
			<%-- 	<tr>
					<td align="left" colspan="4">
						<img src="<%="../images/common/cut.png"%>"/>
					----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					</td>
				
				</tr> --%>
				<tr>
                		<td colspan="4" align="center">
                			<img src="<%="../images/common/btcl.png"%>" align="top"/>
                		</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 12pt" align="center">
                 		Bangladesh Telecommunications Company Ltd.
                 		</td>
                 	</tr>
                 	<tr>
                 	<td>&nbsp;</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 10pt" align="right">
                 		Bank Copy
                 		</td>
                 	</tr>
                 	<tr>
                 		<td colspan="4" style="font-weight: bold;font-size: 10pt" align="center">
                 		ADSL Bill
                 		</td>
                 	</tr>
                 	<tr>
				<td colspan="4">&nbsp;</td>
                 	
                 	</tr>
                 	<tr>
                 		<td style="font-weight: bold;font-size: 8pt" align="left">
                 		Customer Information:
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Name: "+bills.get(i).getName() %>
                 		</td>
                 		<td align="left">
                 		<%="Billing Period: "+format.format(new Date(bills.get(i).getDateFrom()))+" to "+format.format(new Date(bills.get(i).getDateTo())) %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Address: "+bills.get(i).getAddress() %>
                 		</td>
                 		<td align="left">
                 		<%="Issue Date: "+date %>
                 		</td>
                 	</tr>
               		<tr>
                 		<td align="left" colspan="3">
                 		&nbsp;
                 		</td>
                 		<td align="left">
                 		<%="Last Date of Payment: <br/>"+bills.get(i).getLastDateOfPayment() %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="Account No: "+bills.get(i).getAccountNo() %>
                 		</td>
                 		<td align="left">
                 		<%="Bill No: "+bills.get(i).getBillNo() %>
                 		</td>
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="3">
                 		<%="User ID: "+bills.get(i).getUserID() %>
                 		</td>
                 		<td align="left">
                 		<%="Bill Cycle: "+bills.get(i).getBillCycle() %>
                 		</td>
                 	</tr>
                 
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Subscriber Type: "+subscriberTypeStr %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Package: "+bills.get(i).getPackageName() %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
                 		<td align="left" colspan="4">
                 		<%="Telephone Number With Area Code: "+"0"+bills.get(i).getTelNoWithAreaCode() %>
                 		</td>
                 		
                 	</tr>
                 	<tr>
				<td colspan="4">&nbsp;</td>
				</tr>
                 	<tr>
					<td align="left">
					<%="Total Charge: " %>
					
					<%="Tk. "+bills.get(i).getTotalCharge() %>
					</td>
					<td align="left">
					<%="Vat: "%>
					
					<%="Tk."+bills.get(i).getTotalCharge()*vatAmount/100 %>
					</td>
					<td>
					&nbsp;
					</td>
					<td align="left">
					<%="Net Payable Charge: "%>
				
					<%="Tk= "+bills.get(i).getNetPayableCharge() %>
					</td>
				</tr>
				<tr>
				<td align="left" colspan="2">
					<div style="border:1px solid black;">
					<table>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					&nbsp;
					</td>
					</tr>
					<tr>
					<td>
					<%="Advertisement"%>
					</td>
					</tr>
					</table>
					</div>
					</td>
				</tr>
				<tr>
				<td colspan="3">&nbsp;</td>
                 	<td><hr/></td>
                 	</tr>
				<tr>
					<td align="left" colspan="3">
					&nbsp;
					</td>
					<td align="center">
					Signature of Authority
					</td>
				</tr>
				
				<tr>
				<td colspan="4">&nbsp;</td>
				</tr>
				<%
                	}
                }

%>
              
</td>
</tr>
</table>
</td></tr>
<!-- End of Data -->

</table></td>
</tr>

</table><!-- end of page table -->
</center>
</body>
</html>
