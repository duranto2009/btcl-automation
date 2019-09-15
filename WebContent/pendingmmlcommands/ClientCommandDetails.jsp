<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="pendingmmlcommandhandler.PendingMMLCommandDTO"%>
<%@page import="pendingmmlcommandhandler.PendingMMLCommandService"%>
<%@page import="regiontype.RegionRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,client.*,invoiceorder.*,user.*,role.*" %>				 
				 

<%
 String title = "Pending Commands";
 String submitCaption2 = "Back";
 String submitCaption1 = "Execute Commands";
 

 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
 long clientUniqueID = -1;
 clientUniqueID = Long.parseLong((String)session.getAttribute("clientUniqueID"));
 ClientDTO client = null;
 if(clientUniqueID>0)
 {
	 client = ClientRepository.getInstance().getClient(clientUniqueID);
 }
 if(client ==null)
 {
	 response.sendRedirect("../home/login.jsp");
	 
 }
 session.removeAttribute("clientUniqueID");
 String actionName = "/ExecuteMMLCommands.do?clientUniqueID="+clientUniqueID;
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
	 
function goBack()
{
	window.history.back()
}	 

</script>
<style> 
p.test
{
width:11em; 
word-wrap:break-word;
}
</style>
</head>

<body class="body_center_align" onload="document.forms[0].dslmName.focus();">


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="780" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" >
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                  
					 <TR> 
                      <TD height="25" align="left" style="padding-left: 80px">Area Code</TD>
                      <TD height="25" colspan="2" align="left" style="padding-left: 80px">
                      <%=RegionRepository.getInstance().getRegionID(client.getAreaCode()).getRegionName()%>			 
					
                      </TD>
                    </TR>
                   
                    <tr>
                      <td width="200"  style="padding-left: 80px" height="25" align="left">ADSL Phone No</td>
                      <td height="25" align="left" style="padding-left: 80px"> 
                          
                          <%=client.getPhoneNo()%>
                       </td>
                    </tr> 
                    <tr>
                        <td width="194"  style="padding-left: 80px" height="25" align="left">User Name</td>
                        <td width="292" height="25" align="left" style="padding-left: 80px"> <%=client.getUserName() %></td>
                      </tr>
                      
                 
                    <br/><br/>
                      <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="center" width="217"  >
                       Command Type</td>
                      <td class="td_viewheader"  align="center" width="159"  >
                       Command String</td>
                       
                        <td class="td_viewheader"  align="center" width="159"  >
                      Insert Time</td>
                
                </td>
              
              </tr>

<%

	
		PendingMMLCommandService pservice = new PendingMMLCommandService();
        ArrayList<PendingMMLCommandDTO> data = pservice.getAllPendingCommandsOfaClient(clientUniqueID);
		if( data!= null)
		{
			int size = data.size();

			for(int i= 0 ; i < size; i++){

				PendingMMLCommandDTO row = (PendingMMLCommandDTO)data.get(i);

%>
              <tr>
              <td class="td_viewdata1"  align="center" width="217" ><%=row.getMMLCommandID()%>&nbsp;</td>                
              <td class="td_viewdata2 " align="center" width="159" ><p class = "test"><%=row.getMMLCommandString() %>&nbsp;</p></td>  
              <td class="td_viewdata2 " align="center" width="159" ><%=format.format(row.getInsertTime()) %></td>
				
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
                  <input type="button" value="Back" onclick="goBack();"/>
                  <html:submit property = "submitType"><%=submitCaption1 %></html:submit> 
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
