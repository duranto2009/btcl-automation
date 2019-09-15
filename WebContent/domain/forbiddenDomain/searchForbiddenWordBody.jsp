<%@page import="domain.UncensoredWordDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainService"%>
<%@page import="domain.DomainNameDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%
	String msg = null;
	String url = "SearchForbiddenWord";
	String navigator = SessionConstants.NAV_FORBIDDEN_DOMAIN_NAME;
	String context = "../../.." + request.getContextPath() + "/";
	String SmsgType = (String)request.getParameter("msg");
	Integer msgType = null;
	if(SmsgType != null )
		msgType = Integer.parseInt(SmsgType) ;
%>
<%if(msgType != null && msgType == -1){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Error!</strong>
</div><%}else if(msgType != null && msgType == 1){ %>
<div class="alert alert-block alert-success fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Success!</strong>
</div>
<%} %>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<html:form action="/SearchDomainName" method="POST">
			<div class="table-responsive">
				<table id="example1" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>

							<th>Id</th>
							<th>Forbidden Word</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody>


						<%
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_FORBIDDEN_DOMAIN_NAME);

								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										UncensoredWordDTO row = (UncensoredWordDTO) data.get(i);
										request.setAttribute("ID", row.getID());
										request.setAttribute("uncensoredWord", row.getUncensoredWord());
						%>
						<tr>
							<td>${ID }</td>
							<td>${uncensoredWord}
							</td>		
							<td><a href="<%=context%>DomainAction.do?mode=deleteForbiddenWord&id=<%=row.getID()%>">Delete</a></td>
							
							<%
								}
							%>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>

			</div>
		</html:form>
	</div>
</div>