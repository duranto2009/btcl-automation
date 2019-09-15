
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%@ page import="inventory.*"%>



			<%
			
			

				String url = "SearchInventoryItem";
				String navigator = SessionConstants.NAV_VPN_INVENTORY;
				
				String context = "" + request.getContextPath() + "/";
			%>
			<jsp:include page="../../../includes/inventoryNav.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>



		<div class="portlet box">
		<div class="portlet-body">
		<html:form action="/SearchInventoryItem" method="POST">
			
			<div class="table-responsive">
					<table id="example1" class="table table-bordered table-striped">
					<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_VPN_INVENTORY);

									if (data != null) {
										int size = data.size();
										for (int i = 0; i < size; i++) {
											InventoryItemDetails inventoryItemDetailsDTO = (InventoryItemDetails) data.get(i);
										
							%>
						<thead>
							<tr>
																
												<%for(InventoryAttributeValue invAttributeValue: inventoryItemDetailsDTO.getInventoryAttributeValues()){ %>				
															
																<th><%=invAttributeValue.getInventoryAttributeNameID() %></th>
														
															<%} %>	
								
							</tr>
						</thead>
						<tbody>
						
						
							
							<tr>
							
											<%for(InventoryAttributeValue invAttributeValue: inventoryItemDetailsDTO.getInventoryAttributeValues()){ %>				
															
															<td> <%=invAttributeValue.getValue()%></td>
															
												<%} %>			
															
								
								
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
		</html:form>
		</div>
	</div>