<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="exchange.ExchangeRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 dslm.*,exchange.*" %>
<%@page errorPage="../common/failure.jsp" %>
<html>
<head>
<title>DSLM</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
</script>

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
        <td width="600" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title">Dslm</div>
            </td>
          </tr>
    <%
    String msg = null;
    if( (msg = (String)session.getAttribute(util.ConformationMessage.DSLM_UPDATE))!= null)
    {
      session.removeAttribute(util.ConformationMessage.DSLM_UPDATE);
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
	String url = "ViewDslm";
	String navigator = SessionConstants.NAV_DSLM;
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
                      <td class="td_viewheader"  align="center" width="217"  >
                        DSLM Name</td>
                        
                        <td class="td_viewheader"  align="center" width="217"  >
                       Exchange</td>
                      <td class="td_viewheader"  align="center" width="159"  >
                        DSLM Description</td>
                <td class="td_viewheader"  align="center" width="96"  >Edit</td>
		
                <td class="td_viewheader"  align="center" width="58"  >
	                <input type="submit"  value="Delete" >
                </td>
              
              </tr>

<%

		ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_DSLM);
		DslmService fservice = new DslmService(); 
		if( data!= null)
		{
			int size = data.size();
			
			for(int i= 0 ; i < size; i++){

				DslmDTO row = (DslmDTO)data.get(i);
				//out.println(row.getDslmExchangeNo());

%>
              <tr>
                <td class="td_viewdata1"  align="center" width="217" ><%=row.getDslmName() %>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="217" ><%=ExchangeRepository.getInstance().getExchange(row.getDslmExchangeNo()).getExName() %>&nbsp;</td>
                <td class="td_viewdata2 " align="center" width="159" ><%=row.getDslmDescription() %>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="96" ><a href="../GetDslm.do?id=<%=row.getDslmID() %>" >Edit</a></td>
		

				<%if(fservice.hasClient(""+row.getDslmID()) || row.getDslmStatus() == DslmConstants.DSLM_DELETED){ %>
				
                <td class="td_viewdata2"  align="center" width="58" >
	                <input type="checkbox" name="deletedIDs" value="<%=row.getDslmID() %>" disabled = "true"/>
                </td>
               <%}else{ %>
               <td class="td_viewdata2"  align="center" width="58" >
	                <input type="checkbox" name="deletedIDs" value="<%=row.getDslmID() %>" >
                </td>
               <%} %>
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
