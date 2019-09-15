<%@page import="request.CommonRequestDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
CommonRequestDTO commonRequestDTONew = (CommonRequestDTO) request.getAttribute("commonRequestDTO");
%>   
<input type='hidden' name='requestTypeID' value="<%=commonRequestDTONew.getRequestTypeID()%>"> 
<input type="hidden" name="actionName" value="" class="actionName">
<input type='hidden' name='entityTypeID' value="<%=commonRequestDTONew.getEntityTypeID()%>">
<input type='hidden' name='entityID' value="<%=commonRequestDTONew.getEntityID()%>">
<input type='hidden' name='clientID' value="<%=commonRequestDTONew.getClientID()%>">
<input type='hidden' name='reqID' value="<%=commonRequestDTONew.getReqID()%>">
<%-- <input type='hidden' name='sourceRequestID' value="<%=commonRequestDTONew.getReqID()%>"> --%>
<input type='hidden' name='rootEntityID' value="<%=request.getAttribute("rootEntityID")%>">
<input type='hidden' name='rootEntityTypeID' value="<%=request.getAttribute("rootEntityTypeID")%>">
<input type='hidden' name='requestToAccountID' value="<%=commonRequestDTONew.getRequestToAccountID()%>">
