<%@page import="java.text.SimpleDateFormat"%>

<%@page import="util.TimeFormat"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*"%>

<%String frDate = (String)session.getAttribute("frDate");
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>

<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">From</TD>
<td bgcolor="#deede6" align="left" height="25"> 

<%if(frDate == null){ %>

<input type ="text" name = "frDate" size="15" readonly="readonly" value = "<%=TimeFormat.getCurrentMonthStart()%>"/>
<%}else{ %>
 
 <input type ="text" name = "frDate" size="15" readonly="readonly" value = "<%=format.format(format.parse(frDate))%>"/>
 
<%} %>                      
                              <script type="text/javascript" >
                              new pcal ({
                                // form name
                                'formname': 'shiftClientForm',
                                // input name
                                'controlname': 'frDate'
                              });
                              </script>
                           
</td>
</tr>
