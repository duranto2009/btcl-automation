<%@page import="recharge.*"%>
<%@page import="packages.PackageRepository"%>
<%@page import="regiontype.RegionService,recharge.*"%>
<%@ include file="../includes/checkLogin.jsp" %>


<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import = "client.*" %>
<%@page import = "java.util.*" %>
<%@page import = "java.text.*" %>
<%@page import = "client.*" %>
<%@ page import="java.util.ArrayList,
                  java.util.*,
				  java.util.Date,java.text.*,
 				 sessionmanager.SessionConstants,
				help.*,java.sql.*,databasemanager.*,java.lang.Integer,java.lang.*" %>


<%@page errorPage="../common/failure.jsp" %>

<%
   session.removeAttribute(util.ConformationMessage.RECHARGE_CLIENT);
	String rechargeID = request.getParameter("id");
    if(rechargeID == null)
    {
    	rechargeID = ""+(Long)request.getSession().getAttribute("rid");
    }
	/*==== Parameters needed to be configured  =====================================*/

	session.setAttribute("idForRechargeVoucherDownload", rechargeID);
	String windowTitle =  "Recharge Recipt";
	recharge.RechargeClientService rservice = new recharge.RechargeClientService();
	Collection list = new ArrayList();
	list.add(Long.parseLong(rechargeID));
	RechargeClientDTO row = (RechargeClientDTO)((ArrayList)rservice.getDTOs(list)).get(0);
	
%>
<html>
<head>
<title><%=windowTitle %></title>
<script language="JavaScript">
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
  
  <table id="pageTable" height="600" border="0" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0" width="410">
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
                      Telegraph &amp; Telephone Board</font> <font size="2" face="Courier New, Courier, mono"><em>Recharge 
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
<table  style="font-family: Arial; font-size: 10pt; color: #000000; border-collapse:collapse" cellpadding="3" cellspacing="0" border="0" width="409" bordercolor="#000000" >
   <%
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); 
	  %>
	  
	  
	  			<tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Recharge ID</font></td>
                  <td width="275" height="26" align="left" valign="top" ><font size="2" face="Courier New, Courier, mono">:<%=row.getRechargeID()%></font></font></td>
                </tr>
                <tr> 
                  <td width="122" valign="top"><div align="left"><font size="2" face="Courier New, Courier, mono">ADSL Phone No</font></div></td>
                  <td width="275" height="26" align="left" valign="top"> <font size="2" face="Courier New, Courier, mono">:<%=ClientRepository.getInstance().getClient(row.getClientID()).getPhoneNo() %></div></font></font></td>
                </tr>               
                
                 <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Recharge Amount </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getRechargeAmount()%></font></td>
                </tr>
                
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Voucher No</font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getVoucherNo() %></font></td>
                </tr>
                             
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Recharge Date </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=formatter.format(new Date(row.getRechargeDate()))%></font></td>
                </tr>
                
                <tr> 
                  <td valign="top"><font size="2" face="Courier New, Courier, mono">Description </font></td>
                  <td height="26" align="left" valign="top" style="font-family: Arial; font-size: 10pt" ><font size="2" face="Courier New, Courier, mono">:</font><font face="Courier New, Courier, mono"><%=row.getComment() %></font></td>
                </tr>  
                
              
              </table>
</td></tr>
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
              <td align="left" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono">Print 
                Date:<%=new java.util.Date() %> </font></td>
            </tr>
            <tr> 
              <td align="left" style="padding:0; font-family: Arial; font-size: 10pt; "><font face="Courier New, Courier, mono"> 
                Copyright© REVE Systems. All rights reserved </font> </td>
            </tr>
            <br/>
            <br/>
            <tr>
                  <td width="100%" style="padding:0; font-family: Arial (fantasy); font-size: 2pt; font-style: italic; font-weight: bold"> 
                    <p align="center"> <font size="2" face="Courier New, Courier, mono">


<div id="divButtons" name="divButtons" align = "center">
<html:form method="POST" action="/Download">
<input type="submit" name="downloadRechargeVoucher" value="Download">
</html:form>
<input type="button" value = "Print Recipt" onclick="printPage()" >
<INPUT TYPE="button" value = "Back" onClick="parent.location='../viewRechargeInfo.do'">
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
