<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="common.repository.AllClientRepository"%>
<%@page import="othercharge.OtherChargeDTO"%>
<%@page import="othercharge.OtherChargeService"%>
<%@page import="prepaidpayment.*"%>
<%@page import="packages.PackageRepository"%>
<%@page import="regiontype.RegionService"%>
<%@ include file="../includes/checkLogin.jsp" %>


<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import = "java.util.*" %>
<%@page import = "java.text.*" %>
<%@ page import="java.util.ArrayList,
                  java.util.*,
				  java.util.Date,java.text.*,
 				 sessionmanager.SessionConstants,
				help.*,java.sql.*,databasemanager.*,java.lang.Integer,java.lang.*" %>


<%@page errorPage="../common/failure.jsp" %>

<%
   session.removeAttribute(util.ConformationMessage.PREPAID_PAYMENT_ADD);
	String paymentID = request.getParameter("id");
    if(paymentID == null)
    {
    	paymentID = ""+(Long)request.getSession().getAttribute("prepaidPaymentID");
    }
	/*==== Parameters needed to be configured  =====================================*/

	session.setAttribute("idForPrepaidPaymentDownload", paymentID);
	String windowTitle =  "Payment Receipt";

	PrepaidPaymentService pservice = new PrepaidPaymentService();
	Collection list = new ArrayList();
	list.add(Long.parseLong(paymentID));
	PrepaidPaymentDTO row = (PrepaidPaymentDTO)((ArrayList)pservice.getDTOs(list)).get(0);
	
%>
<html>
<head>
<title><%=windowTitle %></title>
<script language="JavaScript">

function goBack()
{
window.history.back()
}

function printPage() {
if(document.all) {
document.all.divButtons.style.visibility = 'hidden';
window.print();
document.all.divButtons.style.visibility = 'visible';
} else {
document.getElementById('divButtons').style.visibility = 'hidden';
window.print();
document.getElementById('divButtons').style.visibility = 'visible';
}
}
</script>

</head>

<body>
<center>
  
  <table id="pageTable" height="600" cellpadding="0" cellspacing="0" width="410">
<tr>
<td valign="top" width="410">
<table style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0" width="100%">
<!-- Start of Page Header -->
<tr><td>
</td></tr>
<!-- End of Page Header -->




<!-- Start of Report Header -->
<tr><td>
<table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
  <tr>
                  <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 2pt; font-style: italic; font-weight: bold"> 
                    <p align="center"> <font size="3" face="Courier New, Courier, mono">Bangladesh 
                      Telecommunication &amp; Company Limited</font> <font size="2" face="Courier New, Courier, mono"><em>Payment 
                      Receipt</em></font> </p>
                    
                  </td>
  </tr>
</table>
</td></tr>
<!-- End of Report Header -->
<tr>
 <td>&nbsp;</td>
</tr>


<!-- Start of Data Header -->
<tr><td>
<table  style="font-family: Arial; font-size: 10pt; color: #000000;  cellpadding="3" cellspacing="0" border="0" width="409"  >
   <%
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); 
	  %>
	  
	  
	  			<tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Payment ID</font></td>
                  <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono">:<%=row.getPaymentID()%></font></font></td>
                </tr>
                <tr> 
                  <td width="122" valign="top"><div align="left"><font size="2" face="Courier New, Courier, mono">ADSL Phone No</font></div></td>
                  <td width="275" height="26" align="left" valign="top"> <font size="2" face="Courier New, Courier, mono">:<%=AllClientRepository.getInstance().getClientByClientID(row.getClientID()).getPhoneNo() %></div></font></font></td>
                </tr>               
                
                 <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Payment Amount </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getPaymentAmount()%></font></td>
                </tr>
                
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Scroll No</font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getScrollNo() %></font></td>
                </tr>
                             
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Payment Date </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=formatter.format(new Date(row.getPaymentTime()))%></font></td>
                </tr>
                
                
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Recieving Date </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=formatter.format(new Date(row.getReceivedTime()))%></font></td>
                </tr>
                
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Description </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getDescription() %></font></td>
                </tr>  
                
              
              </table>
</td></tr>

<tr>

<table  style="font-family: Arial; font-size: 10pt; color: #000000;  cellpadding="3" cellspacing="0" border="1" width="300" bordercolor="#000000" >
   
	  
	  
	  			<tr> 
                  <td valign="top" colspan = "3"><font size="2" face="Courier New, Courier, mono" >Payment Details</font></td>                  
                </tr>
                
                 <tr>
                 <td width="122" valign="top"><div align="left"><font size="2" face="Courier New, Courier, mono">Charging Date</font></div></td>
                 <td valign="top"><font size="2" face="Courier New, Courier, mono">Description</font></td>
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Amount</font></td>
                </tr>
                
                <%
                
                OtherChargeService oservice = new OtherChargeService();
                ArrayList<OtherChargeDTO> charges = ( ArrayList<OtherChargeDTO>)oservice.getOtherChargeWithPayment(paymentID); 
                
               
                for(int i = 0; i< charges.size();i++)
                {
                %>
                
                
                <tr> 
                  
                  <td width="275" height="26" align="left" valign="top"> <font size="2" face="Courier New, Courier, mono"><%=formatter.format(new Date(charges.get(i).getOtherDate()))%></div></font></font></td>
                
                
                  
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=charges.get(i).getOtherDescription()%></font></td>
               
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono"></font><font face="Courier New, Courier, mono"><%=charges.get(i).getOtheramount() %></font></td>
                </tr> 
                <%} %>
              </table>

</tr>
<!-- End of Data -->
</table></td>
</tr>

<tr>
      <td width="410"> <p>&nbsp;</p>
        <p><font face="Courier New, Courier, mono"><br>
          <!-- Start of Page Footer -->
          </font> </p>
        <center>
          <table border="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
            <tr>
              <td align="center" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono">Print 
                Date:<%=new java.util.Date() %> </font></td>
            </tr>
            <tr> 
              <td align="center" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono"> 
                Copyright© REVE Systems. All rights reserved </font> </td>
            </tr>
            <br/>
            <br/>
            <tr>
                  <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 2pt; font-style: italic; font-weight: bold"> 
                    <p align="center"> <font size="2" face="Courier New, Courier, mono">


<div id="divButtons" name="divButtons" align = "center">
<html:form method="POST" action="/Download">
<input type="submit" name="downloadPrepaidPaymentVoucher" value="Download">
</html:form>
<input type="button" value = "Print Recipt" onclick="printPage()" >
<INPUT TYPE="button" value = "Back" onClick="goBack();">
</div>








</font> </p>
                    
                  </td>
  </tr>
           
          </table>
        </center>
        <font face="Courier New, Courier, mono">
        <!-- End of Page Footer -->
        </font></td>
    </tr>
</table><!-- end of page table -->


    <!--Summery/Running totals -->

  </table>
  
 
</center>
</body>
</html>
