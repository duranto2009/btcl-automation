<%@ include file="../includes/checkLogin.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants, mytest.*"%>



<div class="row">
	<div class="col-xs-12">
		<div class="box" style="border-top: 0px solid #d2d6de;">
			<%
				String msg = null;
				if ((msg = (String) session.getAttribute(util.ConformationMessage.MYTEST_EDIT)) != null) {
					session.removeAttribute(util.ConformationMessage.MYTEST_EDIT);
				}

				String url = "ViewMyTest";
				String navigator = SessionConstants.NAV_MYTEST;
			%>
			<jsp:include page="../includes/nav.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>
		</div>
	</div>


	<div class="col-xs-12">
		<!--  start of the form  -->
		<html:form action="/DropMyTest" method="POST">
			<div class="box">
				<div class="box-body">
					<table id="example1" class="table table-bordered table-striped">
						<thead>
							<tr>
																<th>Name</th>
																<th>Roll</th>
																<th>UniqueID</th>
																<th>Edit</th>
								<%
									 {
								%>
								<th><input type="submit" value="Delete" /></th>
								<%
									}
								%>
							</tr>
						</thead>
						<tbody>
							<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_MYTEST);

									if (data != null) {
										int size = data.size();
										for (int i = 0; i < size; i++) {
											MyTestDTO row = (MyTestDTO) data.get(i);
							%>
							<tr>
															<td><%=row.getName()%></td>
															<td><%=row.getRoll()%></td>
															<td><%=row.getUniqueID()%></td>
															<td><a href="../GetMyTest.do?id=<%=row.getId()%>">Edit</a></td>
								
								<td><input type="checkbox" name="deleteIDs" value="<%=row.getId()%>" /></td>
								
								
							</tr>

							<%
								}
							%>
						</tbody>
						<%
							}
						%>

					</table>
				</div>
			</div>
		</html:form>
	</div>
</div>