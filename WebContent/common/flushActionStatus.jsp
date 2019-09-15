<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.SOP"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Logger logger=Logger.getLogger(getClass());
String failureMsg =(String) session.getAttribute("failureMsg");
session.removeAttribute("failureMsg");

CommonActionStatusDTO dto=CommonActionStatusDTO.getCommonActionDTO(request, response);
CommonActionStatusDTO wDTO=CommonActionStatusDTO.getWarningActionDTO(request, response);

if(wDTO!=null){ %>
	<%logger.debug(wDTO); %>
	<div class="alert alert-warning alert-dismissable">
    		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
<!--    		<strong>warning!</strong> please check the warning(s)<br> -->
   		 <%=wDTO.getMessage() %>
   	</div>
<% }%>
<%if(dto!=null){%>
	<%logger.debug(dto);%>
	<%if(dto.getStatusCode()==CommonActionStatusDTO.SUCCESS_STATUS_CODE){ %>
		<div class="alert alert-success alert-dismissable">
      		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
     		<strong>Successfully Completed!</strong><br>  <%=dto.getMessage() %>
     	</div>
	<%}%>

	<%if(dto.getStatusCode()==CommonActionStatusDTO.ERROR_STATUS_CODE){ %>
		<div class="alert alert-danger alert-dismissable">
      		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
<!--      		<strong>Error!</strong> please check the error(s)<br> -->
     		 <%=dto.getMessage() %>
     	</div>
	<%}%>
<%}else if(failureMsg!=null){ %>
	<div class="alert alert-danger alert-dismissable">
   		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
<!--    		<strong>Error!</strong> please check the error(s)<br> -->
   		 <%=StringUtils.trimToEmpty(failureMsg) %>
   	</div>
<%} %>