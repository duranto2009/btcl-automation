<%@page import="java.text.SimpleDateFormat"%>

<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
 
<%String toDate = (String)session.getAttribute("toDate");
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>

<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">To</TD>
<td bgcolor="#deede6" align="left" height="25"> 

 
                           <%if(toDate == null){ %>

<input type ="text" name = "toDate" size="21" readonly="readonly" value = "<%=format.format(new Date())%>"/>
<%}else{ %>
 
 <input type ="text" name = "toDate" size="21" readonly="readonly" value = "<%=format.format(format.parse(toDate))%>"/>
 
<%} %> 
                              <script type="text/javascript" >
                              new pcal ({
                                // form name
                                'formname': 'generateBillForm',
                                // input name
                                'controlname': 'toDate'
                              });
                              </script>
                           
</td>
</tr>
