<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,client.*,invoice.*,user.*,role.*" %>				 
				 

<%
 String title = "Invoice Details";
 String submitCaption2 = "Back";
 String actionName = "/ViewInvoice";

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
	 
	 function validate()
      {
        var f = document.forms[0];

        var ob = f.dslmName;
        if( isEmpty(ob.value))
        {
          alert("Please enter InvoiceOrder Name");
          ob.value = "";
          ob.focus();
          return false;
        }

       
        return true;
      }

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
                   
                   
                    <bean:define id="type" scope="request"> <bean:write name="invoiceForm" property = "invoiceType"/></bean:define>
                    <bean:define id="operator" scope="request"> <bean:write name="invoiceForm" property = "invoiceOperatorID"/></bean:define>
                    <bean:define id="status" scope="request"> <bean:write name="invoiceForm" property = "invoiceStatus"/></bean:define>
                  
					 <TR> 
                      <TD valign="top" height="22">Invoice Name</TD>
                      <TD height="22" colspan="2">
                      <bean:write name="invoiceForm" property="invoiceName"/>			 
					
                      </TD>
                    </TR>
                   
                    <tr>
                      <td width="200" style="padding-right: 0" height="22">Package Type</td>
                      <td height="22"> 
                          
                       <%=InvoiceConstants.INVOICE_TYPE_NAME[Integer.parseInt(type)]%>
                       </td>
                    </tr> 
                    <tr>
                        <td width="194" style="padding-right: 0" height="21">Operator</td>
                        <td width="292" height="22"> <%=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(operator)).getUsername() %>	</td>
                      </tr>
                      
                      <%if(status.equals(""+InvoiceConstants.INVOICE_USED)) {%>
                      
                      <tr>
                        <td width="194" style="padding-right: 0" height="21">Invoice Status</td>
                        <td width="292" height="22">Used</td>
                      </tr>
                      <%}else{ %>
                      <tr>
                        <td width="194" style="padding-right: 0" height="21">Invoice Status</td>
                        <td width="292" height="22">Unused</td>
                      </tr>
                      <%} %>
                    
                     
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
