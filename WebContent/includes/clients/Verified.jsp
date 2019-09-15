
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String valueApplicationStatus = (String)session.getAttribute("clApplicationStatus");
if(valueApplicationStatus == null)valueApplicationStatus= Integer.toString(ClientDTO.APPLICATION_STATUS_VERIFIED);
%>
<tr >
<html:hidden property="clApplicationStatus" value ="<%=Integer.toString(ClientDTO.APPLICATION_STATUS_VERIFIED) %>"/>
</tr>