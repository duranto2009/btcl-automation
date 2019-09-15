<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="client.ClientDTO"%>
<%@page import="client.ClientRepository"%>
<%@page import="regiontype.RegionRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@page errorPage="../common/failure.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,
 				 sessionmanager.SessionConstants,
				pendingmmlcommandhandler.*" %>

<html>
<head>
<title>Clients With Pending MML Commands</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

 <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
 <script  src="../scripts/util.js" type="text/javascript"></script>
 

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber2">
      <tr>
	    <!--left menu-->
       
	    <!--main -->
        <td width="780" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title">Clients with Pending Commands</div>
            </td>
          </tr>
   


          <tr>
            <td width="100%" align="center">
            <br>
<!-- search and navigation control-->
<%
	String url = "ViewClientsWithPendingMMLCommands";
	String navigator = SessionConstants.NAV_PENDING_COMMANDS;
	
%>
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url %>"/>
	<jsp:param name="navigator" value="<%=navigator %>" />
</jsp:include>
<!-- search and navigation control -->


<!-- start of the form -->
<html:form action="/ViewClientsWithPendingMMLCommands" method="POST" >

            <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="center" width="217"  >
                        Area</td>
                        
                        <td class="td_viewheader"  align="center" width="217"  >
                       Adsl Phone No</td>                        
                            
                       
                        <td class="td_viewheader"  align="center" width="217"  >
                     User Name</td> 
                      
                       <td class="td_viewheader"  align="center" width="217"  >
                       No of Pending Commands</td>                  
                <td class="td_viewheader"  align="center" width="96"  >Details</td>
		
                
              </tr>

<%

		ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_PENDING_COMMANDS);
		PendingMMLCommandService pservice = new PendingMMLCommandService();
		if( data!= null)
		{
			int size = data.size();
			

			for(int i= 0 ; i < size; i++){

				ClientPendingDTO row = (ClientPendingDTO)data.get(i);
				ClientDTO client = ClientRepository.getInstance().getClient(row.getUniqueID());
%>
              <tr>
               <td class="td_viewdata1"  align="center" width="217" ><%=RegionRepository.getInstance().getRegionID(client.getAreaCode()).getRegionName()%>&nbsp;</td>
               <td class="td_viewdata1"  align="center" width="217" ><%=client.getPhoneNo()%>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="217" ><%=client.getUserName()%>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="217" ><%=row.getCmdCount()%>&nbsp;</td>        
                <td class="td_viewdata1"  align="center" width="96" ><a href="../GetClientCommands.do?id=<%=row.getUniqueID() %>" >Details</a></td>
		

				
               
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
