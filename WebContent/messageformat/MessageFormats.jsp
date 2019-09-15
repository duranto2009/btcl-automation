<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 messageformat.*" %>
<%@page errorPage="../common/failure.jsp" %>
<html>
<head>
<title>Message Format</title>
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
            <div class="div_title">Message Formats</div>
            </td>
          </tr>
    <%
    String msg = null;
    if( (msg = (String)session.getAttribute(util.ConformationMessage.MSG_FORMAT_UPDATE))!= null)
    {
      session.removeAttribute(util.ConformationMessage.MSG_FORMAT_UPDATE);
      %>
      <tr>
        <td width="100%" align="center" valign="top" height="25"><b><%=msg%></b></td>
      </tr>
      <%}%>


          <tr>
            <td width="100%" align="center">
            <br>
<!-- search and navigation control-->
<%
	String url = "ViewMessageFormat";
	String navigator = SessionConstants.NAV_MSG_FORMAT;
%>
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url %>"/>
	<jsp:param name="navigator" value="<%=navigator %>" />
</jsp:include>
<!-- search and navigation control -->


<!-- start of the form -->
<html:form action="/DropDslm" method="POST" >

            <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="left" width="217"  >
                        Message Category</td>
                        
                        <td class="td_viewheader"  align="left" width="217"  >
                       Message Type</td>                      
                <td class="td_viewheader"  align="left" width="96"  >Edit</td>
		
                
              </tr>

<%

		ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_MSG_FORMAT);
		MessageService fservice = new MessageService(); 
		if( data!= null)
		{
			int size = data.size();

			for(int i= 0 ; i < size; i++){

				MessageDTO row = (MessageDTO)data.get(i);

%>
              <tr>
                <td class="td_viewdata1"  align="center" width="217" ><%=MessageConstants.MESSAGE_TYPE_NAME[row.getMsgFormatCategory()-1]%>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="217" ><%=row.getMsgFormatType()==MessageConstants.MESSAGE_TYPE_MAIL?"Mail":"SMS"%>&nbsp;</td>
                
                <td class="td_viewdata1"  align="center" width="96" ><a href="../GetMessageFormat.do?id=<%=row.getMsgFormatID() %>" >Edit</a></td>
		

				
               
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
