<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="user.UserRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 invoiceorder.*,java.text.SimpleDateFormat,java.util.Date" %>
<%@page errorPage="../common/failure.jsp" %>
<html>
<head>
<title>Invoice Orders</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
</script>

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title">Invoice Orders</div>
            </td>
          </tr>
   


          <tr>
            <td width="100%" align="center">
            <br>
<!-- search and navigation control-->
<%
	String url = "ViewInvoiceOrder";
	String navigator = SessionConstants.NAV_INVOICE_ORDER;
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url %>"/>
	<jsp:param name="navigator" value="<%=navigator %>" />
</jsp:include>
<!-- search and navigation control -->


<!-- start of the form -->
<html:form action="/DropInvoiceOrder" method="POST" >

            <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="center" width="217"  >
                        Operator Name</td>
                      <td class="td_viewheader"  align="center" width="159"  >
                       Total Number of Invoice</td>
                       
                        <td class="td_viewheader"  align="center" width="159"  >
                       Order Date</td>
                       
                        <td class="td_viewheader"  align="center" width="159"  >
                       Invoice Cost(Per Unit)</td>
                <td class="td_viewheader"  align="center" width="96"  >View</td>
		
                
              
              </tr>

<%

		ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_INVOICE_ORDER);
		InvoiceOrderService fservice = new InvoiceOrderService(); 
		if( data!= null)
		{
			int size = data.size();

			for(int i= 0 ; i < size; i++){

				InvoiceOrderDTO row = (InvoiceOrderDTO)data.get(i);

%>
              <tr>
                <td class="td_viewdata1"  align="center" width="217" ><%=UserRepository.getInstance().getUserDTOByUserID(row.getInvoiceOrderOperator()).getUsername()%>&nbsp;</td>
                <td class="td_viewdata2 " align="center" width="159" ><%=row.getInvoiceOrderCount() %>&nbsp;</td>
                <td class="td_viewdata2 " align="center" width="159" ><%=format.format(new Date(row.getInvoiceOrderTime())) %>&nbsp;</td>
                <%if(row.getInvoicePerUnitCost() == -1){ %>
                <td class="td_viewdata2 " align="center" width="159" >Undefined&nbsp;</td>
				<%}else{ %>
                <td class="td_viewdata2 " align="center" width="159" ><%=row.getInvoicePerUnitCost()%>&nbsp;</td>
                <%} %>
                <td class="td_viewdata1"  align="center" width="96" ><a href="../GetInvoiceOrder.do?id=<%=row.getInvoiceOrderID() %>" >View</a></td>
		

				
              </tr>
              <%
            }
          }
          %>

            </table>
</html:form>

<!-- end of the form -->
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
	<%@ include file="../includes/footer.jsp"  %></td></tr></table></body></html>
