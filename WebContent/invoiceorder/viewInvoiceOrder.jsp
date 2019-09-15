<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,client.*,invoiceorder.*,user.*,role.*,invoice.*" %>				 
				 

<%
 String title = "Invoice Order Details";
 String submitCaption2 = "Back";
 String actionName = "/ViewInvoiceOrder";

 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>

<html>
<head>
<html:base/>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 
	 

</script>

</head>

<body class="body_center_align" onload="document.forms[0].dslmName.focus();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                   <bean:define id="oid" scope="request"> <bean:write name="invoiceorderForm" property = "invoiceOrderID"/></bean:define>
                    <bean:define id="operator" scope="request"> <bean:write name="invoiceorderForm" property = "invoiceOrderOperator"/></bean:define>
                    <bean:define id="type" scope="request"> <bean:write name="invoiceorderForm" property = "invoiceOrderType"/></bean:define>
                   <bean:define id="date" scope="request"> <bean:write name="invoiceorderForm" property = "invoiceOrderTime"/></bean:define> 
                    <bean:define id="cost" scope="request"> <bean:write name="invoiceorderForm" property = "invoicePerUnitCost"/></bean:define> 
					 <TR> 
                      <TD height="25" align="left" style="padding-left: 80px">Order Operator</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px">
                      <%=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(operator)).getUsername() %>			 
					
                      </TD>
                    </TR>
                   
                    <tr>
                      <td width="200"  style="padding-left: 80px" height="25" align="left">Package Type</td>
                      <td height="25" align="left" style="padding-left: 80px"> 
                          
                          <%=InvoiceConstants.INVOICE_TYPE_NAME[Integer.parseInt(type)==2?0:Integer.parseInt(type)==1?1:Integer.parseInt(type)-1]%>
                       </td>
                    </tr> 
                    <tr>
                        <td width="194"  style="padding-left: 80px" height="25" align="left">Total Number of Invoice</td>
                        <td width="292" height="25" align="left" style="padding-left: 80px"> <bean:write name = "invoiceorderForm" property="invoiceOrderCount"/></td>
                      </tr>
                      
                       <TR> 
                      <TD  style="padding-left: 80px" height="25" align="left">Invoice Per Unit Cost</TD>
                      <%if(!(Double.parseDouble(cost) == -1)){ %>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px"><%=cost%> </TD>
                      <%}else{ %>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px">Undefined</TD>
                      <%} %>
                    </TR>    
					 <TR> 
                      <TD  style="padding-left: 80px" height="25" align="left">Description</TD>
                  <TD height="25" colspan="2" align="left" style="padding-left: 80px"><bean:write name = "invoiceorderForm" property="invoiceOrderDescription"  /></TD> 
                    </TR>                    
                  
                    <%
                     date = format.format(new Date(Long.parseLong(date)));
                    %>
                     <TR> 
                      <TD height="25" align="left" style="padding-left: 80px">Order Date</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px"><%=date %></TD>
                      
                     </TR>    
                    <br/><br/>
                      <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="center" width="217"  >
                        Invoice Number</td>
                      <td class="td_viewheader"  align="center" width="159"  >
                       Invoice Type</td>
                       
                        <td class="td_viewheader"  align="center" width="159"  >
                      Invoice Status</td>
                
                </td>
              
              </tr>

<%

	
		InvoiceService fservice = new InvoiceService(); 
        ArrayList data = (ArrayList)fservice.getDTOs(Long.parseLong(oid));
		if( data!= null)
		{
			int size = data.size();

			for(int i= 0 ; i < size; i++){

				InvoiceDTO row = (InvoiceDTO)data.get(i);

%>
              <tr>
              <td class="td_viewdata1"  align="center" width="217" ><%=row.getInvoiceName()%>&nbsp;</td>                
              <td class="td_viewdata2 " align="center" width="159" ><%=InvoiceConstants.INVOICE_TYPE_NAME[row.getInvoiceType()==2?0:row.getInvoiceType()==1?1:row.getInvoiceType()-1] %>&nbsp;</td>
              <%if(row.getInvoiceStatus() == InvoiceConstants.INVOICE_UNUSED){ %>
              
              <td class="td_viewdata2 " align="center" width="159" >Unused</td>
              <%}else{ %>
              <td class="td_viewdata2 " align="center" width="159" >Used</td>
              <%} %>
               
               
		

				
              </tr>
              <%
			}
          }
          %>

            </table>
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  <html:submit><%=submitCaption2 %></html:submit> 
                  </html:form> 
                  <!-- end of the form -->
                  <br>

            <br>
            </td>
          </tr>
        </table>

        </td>
      </tr>
    </table>
    </td>
  </tr>

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/footer.jsp"  %>
    </td>
  </tr>

</table>
</body>
</html>
