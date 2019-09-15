<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="mmlclient.mml.CommandConstants"%>
<%@page import="mmlclient.mml.MMLDTO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="login.PermissionConstants"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.*,sessionmanager.SessionConstants,tracker.*"%>
<%@page errorPage="../common/failure.jsp"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
%>
<html>
<head>
<html:base/>
<title>MML Log</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
 <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">

<style> 
p.test
{
width:11em; 
word-wrap:break-word;
}
</style>
<script language="JavaScript">


</script>
</head>
<body class="body_center_align">
<table border="0" cellpadding="0" cellspacing="0" width="780" id="AutoNumber1">
  <tr>
    <td width="100%">
      <%@include file="../includes/header.jsp"%>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <table border="0" cellpadding="0" cellspacing="0" width="780" id="AutoNumber2">
        <tr>
         
          <td width="780" valign="top" class="td_main" align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
              <tr>
                <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                  <div class="div_title">
                    <!--Client-->
MML Log                </div>
                </td>
              </tr>
           
              <tr>
                <td width="100%" align="center">
                  <br/>
                  <!-- search and navigation control-->
                <%
                  String url = "ViewMMLLog";
                  String navigator = SessionConstants.NAV_MML_LOG;
                %>
                  <jsp:include page="../includes/nav.jsp" flush="true">
                  <jsp:param name="url" value="<%=url %>"/>
                  <jsp:param name="navigator" value="<%=navigator %>"/>
                  </jsp:include>
                  <!-- search and navigation control -->
                  <!-- start of the form -->
                 <html:form  action = "/ViewMMLLog" method = "POST">
                    <table width="595" class="table_view">
                      <tr>
                        <td class="td_viewheader" align="center" valign="top" width="170" >
                          <!--Customer ID-->
						Command</td>
                        <td class="td_viewheader" align="center" valign="top" width="170" >
                          <!--Name-->
						Execution Time</td>
                        <td class="td_viewheader" align="center" valign="top" width="70">
                          <!--Payment Type-->
                         Response 
                        </td>
                        
                         <td class="td_viewheader" align="center" valign="top" width="70" >
                          <!--Application Status-->
                         Response Time
                        </td>
                        
                        
                        
                         <td class="td_viewheader" align="center" valign="top" width="70" >
                          <!--Account Status-->
                         Status</td>
                        
                        
                       
                        <!-- <td class="td_viewheader" align="center" valign="top" width="50" height="25">View</td> -->
                      	
                       
                      	
                      	
                      	
                       
                      </tr>
                    <%
                      ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_MML_LOG);
                      if (data != null) {
                        int size = data.size();
                        System.out.println("size:"+size);
                        
                        for (int i = 0; i < size; i++)
						{
                          MMLDTO row = (MMLDTO) data.get(i);
                    %>
                      <tr >
                        <td class="td_viewdata1" align="center" width="170"  ><p class="test"><%=row.getCommand()%></p></td>
                        <td class="td_viewdata2" align="center" width="170" ><%=format.format(new Date(row.getExecutionTime()))%> </td>
                         <td class="td_viewdata1" align="center" width="70" ><%=row.getResponseMessage()%> </td>
                        <td class="td_viewdata1" align="center" width="70"><%=format.format(new Date(row.getResponseTime()))%> </td> 
                        <td class="td_viewdata1" align="center" width="70" ><%=row.getStatus()==CommandConstants.SUCCESSFUL?"Successful":row.getStatus() == CommandConstants.UNSUCCESSFUL?"Unsuccessful":"Pending" %> </td> 
                        
                        
                        
                      </tr>
                    <%
                      }
                    }
                    %>
                    </table>
                  </html:form>
                  <br/>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <%@include file="../includes/footer.jsp"%>
    </td>
  </tr>
</table>
</body>
</html>
