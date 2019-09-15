<%@page import="client.IdentityTypeConstants"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="vpn.client.ClientDetailsDTO"%>

<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="vpn.client.ClientUpdateService"%>
<%@page import="common.RegistrantTypeConstants"%>
<%@page import="common.LanguageConstants"%>
<%@page import="domain.constants.DomainRegistrantsConstants"%>
<%@page import="util.MultipleTypeUtils"%>
<%@page import="java.util.Locale"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="java.util.Set"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="client.classification.ClientClassificationDAO"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String context = "../../.." + request.getContextPath() + "/";
	LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	int id = Integer.parseInt(request.getParameter("entityID"));
	String actionName = "../../GetClientForEdit.do?entityID=" + id + "&moduleID=" + ModuleConstants.Module_ID_DOMAIN;
	String back = "../../GetClientForView.do?entityID=" + id + "&moduleID=" + ModuleConstants.Module_ID_DOMAIN;
	ClientForm clientForm = (ClientForm) request.getAttribute("form");
	ClientContactDetailsDTO registrantContactDetails = clientForm.getRegistrantContactDetails();
	ClientDetailsDTO clientDetailsDTO = clientForm.getClientDetailsDTO();
	Integer registrantCategory = (int)clientForm.getClientDetailsDTO().getRegistrantCategory();
	//System.out.println(regCat);

	//String idValue =(String) request.getParameter("identity");
	//if(idValue==null)idValue="-1";
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-user"></i>Info 
		</div>

		<div class="actions">
			<a href="<%=back%>" class="btn btn-back-btcl"
				style="padding-left: 15px; padding-right: 15px;"> Back </a>
			<input type="button" id="printBtn" class="btn btn-default "
				style="padding-left: 15px; padding-right: 15px;" value="Print" />   
			<%
			boolean showEditButton = false;
					
			if((Math.abs(clientDetailsDTO.getCurrentStatus()) % ModuleConstants.MULTIPLIER) == 5009)
			{
				
			}
			else if(loginDTO.getUserID() > 0)
			{
				Boolean isUserPermittedToUpdateClient = new ClientUpdateService().isUserPermittedToUpdateClient(clientDetailsDTO.getClientID(), ModuleConstants.Module_ID_DOMAIN*100+51, loginDTO);
				if(isUserPermittedToUpdateClient)
				{
					showEditButton = true;
				}
			}
			else if(loginDTO.getAccountID() > 0)
			{
				boolean isActiveClient = new ClientService().getClientIsActive(clientDetailsDTO.getClientID(), ModuleConstants.Module_ID_DOMAIN);
				if(isActiveClient)
				{
					showEditButton = true;				
				}
			}
			%>



			<% if (showEditButton){%>
			<a href="<%=actionName%>&edit" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px;"> Edit </a>
			<%}%>
		</div>
	</div>
	<div class="portlet-body " id="printContent">
		<div class="table-responsive">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="text-center" colspan="2"><h3>
								<b><%=clientForm.getClientDetailsDTO().getLoginName()%></b>'s Information
							</h3>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">Username</th>
						<td>${form.clientDetailsDTO.loginName}</td>
					</tr>
					<%
						if (clientForm.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_INDIVIDUAL) {
					%>
						<tr>
							<th scope="row">First Name</th>
							<td>${form.registrantContactDetails.registrantsName}</td>
						</tr>
						<tr>
							<th scope="row">Last Name</th>
							<td>${form.registrantContactDetails.registrantsLastName}</td>
						</tr>
						<tr>
							<th scope="row">Father Name</th>
							<td>${form.registrantContactDetails.fatherName}</td>
						</tr>
						<tr>
							<th scope="row">Mother Name</th>
							<td>${form.registrantContactDetails.motherName}</td>
						</tr>
						<tr>
							<th scope="row">Occupation</th>
							<td>${form.registrantContactDetails.occupation}</td>
						</tr>
					<%
						} else if (clientForm.getClientDetailsDTO().getClientCategoryType() == ClientForm.CLIENT_TYPE_COMPANY){
					%>
						<tr>
							<th scope="row"><%=LanguageConstants.COMPANY %> Name</th>
							<td>${form.registrantContactDetails.registrantsName}</td>
						</tr>
						<tr>
							<th scope="row"><%=LanguageConstants.COMPANY %> Type</th>
							<td><%=RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_DOMAIN).get(clientForm.getClientDetailsDTO().getRegistrantType()) %></td>
						</tr>
						
						<tr>
							<th scope="row">Category</th>
							<td>
							<%=ServiceDAOFactory.getService(ClientClassificationDAO.class).getRegistrantCategoryDTOById(registrantCategory).getName()%> 
							</td>
						</tr>
					<%
						}
					%>
					
					<tr>
						<th scope="row">Email</th>
						<td>${form.registrantContactDetails.email}</td>
					</tr>

					<tr>
						<th scope="row">Country</th>
						<td><%=new Locale("",clientForm.getClientDetailsDTO().getVpnContactDetails().get(0).getCountry()).getDisplayCountry()%></td>
					</tr>
					<tr>
						<th scope="row">Post Code</th>
						<td>${form.registrantContactDetails.postCode}</td>
					</tr>
					<tr>
						<th scope="row">Address</th>
						<td>${form.registrantContactDetails.address}</td>
					</tr>
					<tr>
						<th scope="row">Mobile</th>
						<td>${form.registrantContactDetails.phoneNumber}</td>
					</tr>
					<%if(clientForm.getClientDetailsDTO().getClientCategoryType()==ClientForm.CLIENT_TYPE_INDIVIDUAL){ %>
						<tr>
							<th scope="row">Date of birth</th>
							<td>${form.registrantContactDetails.dateOfBirth}</td>
						</tr>
					<%} %>
					<%if(StringUtils.isNotEmpty(clientForm.getClientDetailsDTO().getIdentity())){
					String[] identityArray = clientForm.getClientDetailsDTO().getIdentity().split(",");
					for(String identity : identityArray){
					%>
					<tr>
						<th scope="row"><%=IdentityTypeConstants.IdentityTypeName.get((Integer.parseInt(identity.split(":")[0]))%1000)%> No</th>
						<td><%=identity.split(":")[1]%></td>
					</tr>
					<%}
					} %>
					<tr>
						<th scope="row">Fax Number</th>
						<td>${form.registrantContactDetails.faxNumber}</td>
					</tr>
					<%if(clientForm.getClientDetailsDTO().getClientCategoryType()==ClientForm.CLIENT_TYPE_COMPANY){ %>
						<tr>
							<th scope="row">Web Address</th>
							<%
								if(StringUtils.isNotEmpty(clientForm.getRegistrantContactDetails().getWebAddress())){
									String url=clientForm.getRegistrantContactDetails().getWebAddress();
									if(!url.startsWith("http")){
										url="http://"+url;
									}								
							%>
							<td><a target="_blank" href="<%=url%>">Visit Company Website</a></td>
							<%}else{ %>
								<td></td>
							<%} %>
						</tr>
					<%} %>
				</tbody>
			</table>
		</div>
		<div class="table-responsive">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th><h3>Contact Info</h3></th>
						<th><h3>Billing Contact</h3></th>
						<th><h3>Admin Contact</h3></th>
						<th><h3>Technical Contact</h3></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">First Name</th>
						<td class="text-muted">${form.billingContactDetails.registrantsName}</td>
						<td class="text-muted">${form.adminContactDetails.registrantsName}</td>
						<td class="text-muted">${form.technicalContactDetails.registrantsName}</td>
					</tr>
					<tr>
						<th scope="row">Last Name</th>
						<td class="text-muted">${form.billingContactDetails.registrantsLastName}</td>
						<td class="text-muted">${form.adminContactDetails.registrantsLastName}</td>
						<td class="text-muted">${form.technicalContactDetails.registrantsLastName}</td>
					</tr>
					<tr>
						<th scope="row">City</th>
						<td class="text-muted">${form.billingContactDetails.city}</td>
						<td class="text-muted">${form.adminContactDetails.city}</td>
						<td class="text-muted">${form.technicalContactDetails.city}</td>
					</tr>
					<tr>
						<th scope="row">Post Code</th>
						<td class="text-muted">${form.billingContactDetails.postCode}</td>
						<td class="text-muted">${form.adminContactDetails.postCode}</td>
						<td class="text-muted">${form.technicalContactDetails.postCode}</td>
					</tr>
					<tr>
						<th scope="row">Address</th>
						<td class="text-muted">${form.billingContactDetails.address}</td>
						<td class="text-muted">${form.adminContactDetails.address}</td>
						<td class="text-muted">${form.technicalContactDetails.address}</td>
					</tr>
					<tr>
						<th scope="row">Mobile</th>
						<td class="text-muted">${form.billingContactDetails.phoneNumber}</td>
						<td class="text-muted">${form.adminContactDetails.phoneNumber}</td>
						<td class="text-muted">${form.technicalContactDetails.phoneNumber}</td>
					</tr>
					<tr>
						<th scope="row">Fax</th>
						<td class="text-muted">${form.billingContactDetails.faxNumber}</td>
						<td class="text-muted">${form.adminContactDetails.faxNumber}</td>
						<td class="text-muted">${form.technicalContactDetails.faxNumber}</td>
					</tr>
				</tbody>
			</table>
			<jsp:include page="../../common/fileListHelper.jsp" flush="true">
				<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.DOMAIN_CLIENT%>" />
				<jsp:param name="entityID" value="<%=id%>" />
			</jsp:include>

		</div>
	</div>
</div>

<script src="${context}assets/scripts/printThis.js" type="text/javascript"></script>
<script type="text/javascript">

$(document).ready(function(){
    $("#printBtn").click(function(){
		$("#printContent").printThis();
    })
})

</script>