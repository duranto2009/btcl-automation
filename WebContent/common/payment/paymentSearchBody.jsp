<%@page import="common.*"%>
<%@page import="common.bill.BillDTO"%>
<%@page import="common.bill.BillService"%>
<%@page import="common.payment.PaymentDTO"%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestUtilService"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="user.UserRepository"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	String msg = null;
	String url = "payment/search";
	String navigator = SessionConstants.NAV_PAYMENT;
	String context = "../../.." + request.getContextPath() + "/";
	String moduleIDStr = request.getParameter("moduleID");
	int moduleID=Integer.parseInt(moduleIDStr);
	Logger logger= Logger.getLogger(getClass());
	
%>

<jsp:include page="../../includes/navPayment.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<html:form action="/payment/search" method="POST">
			<div class="table-responsive">
				<table id="example1" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>PaymentID ID</th>
							<th>Client</th>
							<th>Entity</th>
							<th>Payment Gateway Type</th>
							<th>VAT Included</th>
							<th>BTCL Amount(BDT)</th>
							<th>VAT Amount(BDT)</th>
							<th>Total Amount(BDT)</th>
							<th>Paid Amount(BDT)</th>
							<th>Payment Time</th>
							<%if(moduleID==ModuleConstants.Module_ID_VPN
								|| moduleID == ModuleConstants.Module_ID_LLI ){ %>
								<th>Verified By</th>
								<th>Verification Time</th>
								<th>Approved By</th>
								<th>Approval Time</th>
								<th>Payment Status</th>
							<%} %>
						</tr>
					</thead>
					<tbody>

						<%
							BillService billService = ServiceDAOFactory.getService(BillService.class);
							CommonService commonService = new CommonService();
							RequestUtilService requestUtilService = ServiceDAOFactory.getService(RequestUtilService.class);
							
							String userName="";
							String timeStr="";
							ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_PAYMENT);
						
								if (data != null) {
									int size = data.size();
									for (int i = 0; i < size; i++) {
										PaymentDTO row = (PaymentDTO) data.get(i);
										try{
											
											List<BillDTO> billDTOs = billService.getBillIDListByPaymentID(row.getID(), ModuleConstants.Module_ID_VPN);
											BillDTO billDTO = billDTOs.get(0);
											long clientID=billDTO.getClientID();
											CommonRequestDTO cDTO=requestUtilService.getRequestDTOByReqID(billDTO.getReqID());
											ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);
											EntityDTO entityDTO =  commonService.getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID());
											
										%>
											<tr>
												<td><a target="_blank" href="${context}common/payment/linkPayment.jsp?paymentID=<%=row.getID()%>&moduleID=<%=moduleID%>"><%=row.getID() %></a></td>
												<td><a target="_blank" href="${context}GetClientForView.do?entityID=<%=clientID %>
												&entityTypeID=<%=(moduleID==ModuleConstants.Module_ID_VPN)?EntityTypeConstant.VPN_CLIENT:
													((moduleID==ModuleConstants.Module_ID_LLI)?EntityTypeConstant.LLI_CLIENT:"")%>&moduleID=<%=moduleID%>"><%=clientDTO.getLoginName() %></a></td>
												<td><%-- <a target="_blank" href="${context}<%=EntityActionGenerator.getAction(cDTO)%>"><%=entityDTO.getName() %></a> --%></td>
												<td><%=PaymentConstants.paymentGatewayIDNameMap.get(row.getPaymentGatewayType()) %></td>
												<td>
													<%if(row.getVatIncluded()==1){ %>
							                   			<label class="label label-success">YES</label>
							                   		<%}else{ %>
							                   			<label class="label label-info">NO</label>
							                   		<%} %>
						                   		</td>
												<td><%=Math.round(row.getBtclAmount()) %></td>
												<td><%=Math.round(row.getVatAmount()) %></td>
												<td><%=Math.round(row.getPayableAmount()) %></td>
												<td><%=Math.round(row.getPaidAmount()) %></td>
												<td><%=TimeConverter.getTimeStringFromLong( row.getPaymentTime())  %></td>	
												
												<%if( moduleID == ModuleConstants.Module_ID_VPN
													|| moduleID == ModuleConstants.Module_ID_LLI ){ %>
												<td>
													<%
													if(row.getVerifiedBy()>0){
														userName=UserRepository.getInstance().getUserDTOByUserID(row.getVerifiedBy()).getUsername();
													}else{
														userName="N/A";
													} 
													%>
													<%=userName %>
												</td>
												<td>
													<%
													if(row.getLastModificationTime()>0){
														timeStr=TimeConverter.getTimeStringFromLong( row.getLastModificationTime());
													}else{
														timeStr="N/A";
													}
													%>
													<%=timeStr %>
												</td>	
												<td>
													<%
													if(row.getApprovedBy()>0){
														userName=UserRepository.getInstance().getUserDTOByUserID(row.getApprovedBy()).getUsername();
													}else{
														userName="<a target='_blank' href='"+ context+"common/payment/linkPayment.jsp?paymentID="+row.getID()+"&moduleID="+moduleID+"'>View</a>";
													} 
													%>
													<%=userName %>
												</td>
												<td>
													<%
													if(row.getLastModificationTime()>0 && row.getApprovedBy()>0){
														timeStr=TimeConverter.getTimeStringFromLong( row.getLastModificationTime());
													}else{
														timeStr="N/A";
													}
													%>
													<%=timeStr %>
												</td>	
												<td nowrap>
													<%=PaymentConstants.paymentStatus.get(row.getPaymentStatus()) %>
												</td>
												<%} %>
											</tr>
										<%
											}catch(Exception ex){
												logger.fatal("",ex);
											}
										}
								} else{%>
								<tr>
									<td colspan='15' class="text-center">No record is found</td>
								</tr>
								<%} %>
										
					</tbody>
				</table>
			</div>
		</html:form>
	</div>
</div>