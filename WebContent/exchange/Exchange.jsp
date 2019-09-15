<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="regiontype.RegionRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 exchange.*" %>
<%@page errorPage="../common/failure.jsp" %>
<html>
<head>
<title>Exchange</title>
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
            <div class="div_title">Exchange</div>
            </td>
          </tr>
    <%
    String msg = null;
    if( (msg = (String)session.getAttribute(util.ConformationMessage.EXCHANGE_UPDATE))!= null)
    {
      session.removeAttribute(util.ConformationMessage.EXCHANGE_UPDATE);
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
	String url = "ViewExchange";
	String navigator = SessionConstants.NAV_EXCHANGE;
%>
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url %>"/>
	<jsp:param name="navigator" value="<%=navigator %>" />
</jsp:include>
<!-- search and navigation control -->


<!-- start of the form -->
<html:form action="/DropExchange" method="POST" >

            <table  class="table_view"  width="550">
              <tr>
                      <td class="td_viewheader"  align="center" width="217"  >
                        Exchange Name</td>
                        
                        <td class="td_viewheader"  align="center" width="217"  >
                       Exchange Code</td>
                      <td class="td_viewheader"  align="center" width="159"  >
                        Region</td>
                        
                        <td class="td_viewheader"  align="center" width="159"  >
                        Phone Number Range</td>
                        
                <td class="td_viewheader"  align="center" width="96"  >Edit</td>
		
                <td class="td_viewheader"  align="center" width="58"  >
	                <input type="submit"  value="Delete" >
                </td>
              
              </tr>

<%

		ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_EXCHANGE);
		ExchangeService fservice = new ExchangeService(); 
		if( data!= null)
		{
			int size = data.size();
			
			for(int i= 0 ; i < size; i++){

				ExchangeDTO row = (ExchangeDTO)data.get(i);
				//out.println(row.getExchangeExchangeNo());

%>
              <tr>
                <td class="td_viewdata1"  align="center" width="217" ><%=row.getExName() %>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="217" ><%=row.getExCode() %>&nbsp;</td>
                <td class="td_viewdata2 " align="center" width="159" ><%=RegionRepository.getInstance().getRegionID((long)row.getExNWD()).getRegionName() %>&nbsp;</td>
                <td class="td_viewdata2 " align="center" width="159" ><%=row.getExStartPhone() +" - "+row.getExEndPhone() %>&nbsp;</td>
                <td class="td_viewdata1"  align="center" width="96" ><a href="../GetExchange.do?id=<%=row.getExCode() %>" >Edit</a></td>
		

				<%if(fservice.hasDslm(row.getExCode()) || row.getExStatus() == ExchangeConstants.EXCHANGE_DELETED){ %>
				
                <td class="td_viewdata2"  align="center" width="58" >
	                <input type="checkbox" name="exchangeIDs" value="<%=row.getExCode() %>" disabled = "true"/>
                </td>
               <%}else{ %>
               <td class="td_viewdata2"  align="center" width="58" >
	                <input type="checkbox" name="exchangeIDs" value="<%=row.getExCode() %>" >
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
