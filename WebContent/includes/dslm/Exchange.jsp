
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,exchange.*" %>
<%String dslmExchangeNo = (String)session.getAttribute("dslmExchangeNo");
if(dslmExchangeNo == null)dslmExchangeNo="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Exchange</td>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="dslmExchangeNo">
 <option value="-1" <%=(dslmExchangeNo.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    ArrayList exchangeList = exchange.ExchangeRepository.getInstance().getExchangeList();
    for(int i=0;i<exchangeList.size();i++)
    {
      exchange.ExchangeDTO exchangeDTO = (exchange.ExchangeDTO)exchangeList.get(i);
      
 %>
               <option value="<%=exchangeDTO.getExCode()%>" <%=(dslmExchangeNo.equals(""+exchangeDTO.getExCode())?"selected=\"selected\"":"")%>><%=exchangeDTO.getExName()%></option>
 <%} %>
</select>
</td>
</tr>