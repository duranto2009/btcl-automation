<%@page import="util.TimeConverter"%>
<%@page import="ipaddress.IpBlock"%>
<%@page import="java.util.List"%>
<%@page import="ipaddress.IpAddressService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="user.UserRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="language.LliLanguageConstants"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%

LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
int id = Integer.parseInt(request.getParameter("entityID"));
String context = "../../.." + request.getContextPath() + "/";
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
LliLinkDTO lliLinkDTO= (LliLinkDTO)request.getAttribute("lliLink");
String actionName = lliLinkService.isLinkVerified(lliLinkDTO.getID()) 
						? "../../LLI/Link/Update/Link.do?id="+lliLinkDTO.getID()
						: request.getContextPath() + "/LliLinkAction.do?entityID=" + id + "&entityTypeID="+ EntityTypeConstant.LLI_LINK+"&getMode=edit";
String back  = "../../LliLinkAction.do?entityID=" + id+"&entityTypeID="+EntityTypeConstant.LLI_LINK;

LliFarEndDTO farEndDTO=(LliFarEndDTO)request.getAttribute("lliFE");
LliEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);

request.setAttribute("farEnd", farEndDetailsDTO);

%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>LLI Connection View
		</div>
		<div class="actions">
			<a  href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px;"> Back </a>
			<input type="button" id="printBtn" class="btn btn-default " style="padding-left: 15px; padding-right: 15px;" value="Print" />   
			<%-- <%
			if ( (!lliLinkService.isLinkVerified(lliLinkDTO.getID()) && loginDTO.getAccountID() > 0 ) || loginDTO.getUserID() > 0 ) {
			%>
			<a href="<%=actionName%>" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px;"> Edit </a>
			<% } %> --%>

			<%if (lliLinkService.processLinkUpdatePermission(id, loginDTO, request)){%>
				<a href="<%=actionName%>&edit" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px;"> Edit </a>
			<%}%>

		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-bordered  table-striped">
						<thead>
							<tr>
								<th colspan="3" class="text-center">
									<h1> Connection Details </h1>
								</th>
							</tr>
						</thead>
						<tbody>
							
							<tr>
								<th scope="row">Client Name</th>
								<td colspan="2">${clientName}</td>
							</tr>
							<tr>
								<th scope="row">Connection Name</th>
								<td colspan="2">${lliLink.linkName}</td>
							</tr>
							<%if(loginDTO.getIsAdmin()){ %>
								<tr>
									<th scope="row">Service Purpose</th>
									<td colspan="2"><%if(lliLinkDTO.getServicePurpose()==1){ %> YES <%}else{ %>NO <%} %> </td>
								</tr>
							<%} %>
							<%if(loginDTO.getIsAdmin()){ %>
								<tr>
									<th scope="row">Migrated</th>
									<td colspan="2"><%if(lliLinkDTO.getIsMigrated()==true){ %> YES <%}else{ %>NO <%} %> </td>
								</tr>
							<%} %>							
							<tr>
								<th scope="row">Bandwidth</th>
								<td colspan="2">${lliLink.lliBandwidth} &nbsp; <%=EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) %>&nbsp; </td>
							</tr>
							<tr>
								<th scope="row">Connection Type</th>
								<td colspan="2"><%=EndPointConstants.connectionType.get(lliLinkDTO.getConnectionType()) %></td>
							</tr> 

							<%if( lliLinkDTO.getConnectionType() == EndPointConstants.CONNECTION_TYPE_FIVE_YEARS_ ){ %>
								<tr>
									<th scope="row">Five Year Bandwidth</th>
									<td colspan="2"><%=lliLinkDTO.getFiveYearBandwidth() %> <%=EntityTypeConstant.linkBandwidthTypeMap.get( lliLinkDTO.getFiveYearBandwidthType() ) %></td>
								</tr>
							<%} %>

							<%
						 	List<IpBlock> ipList = ServiceDAOFactory.getService(IpAddressService.class).getIPAddressByEntityID(lliLinkDTO.getID());
							String ipStringMandatory = "";
							String ipStringAdditional = "";
							List<IpBlock>ipListMandatory = new ArrayList<IpBlock>();
							List<IpBlock>ipListAdditional = new ArrayList<IpBlock>();
							if(ipList != null){
								for(IpBlock ipBlock : ipList){
									if(ipBlock.getUsageType() == InventoryConstants.USAGE_ESSENTIAL){
										ipListMandatory.add(ipBlock);
									}else {
										ipListAdditional.add(ipBlock);
									}
								}
							}
							
// 							if(ipList != null)
// 							{								
// 								int ipCount = 0;
// 								for(IpBlock ipBlock: ipList)
// 								{
// 									if(ipBlock.getUsageType() == InventoryConstants.USAGE_ESSENTIAL)
// 									{
// 										ipStringMandatory += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
// 									}
// 									else
// 									{
// 										ipStringAdditional += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
// 									}
// 									if(++ipCount == ipList.size())
// 									{
// 										break;
// 									}
// 									ipStringMandatory += ",";
// 								}
// 							}
							%>
							<tr>							
							<th scope="row">Mandatory IP Address</th>
							<td colspan="2">
							<table class="table table-bordered  table-striped">
								<tbody>
							<%
							if(ipListMandatory.size() > 0) {
								for(int i=0;i<ipListMandatory.size();i++){
									IpBlock ipBlock = ipListMandatory.get(i);
									String startingIP = IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()); 
									String endingIP = IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
								%>
								<tr>
									<td><%=(startingIP + "-" + endingIP)%></td>
								</tr>
							<%	} %>
							<%	
							}else{
								out.println("N/A");
							}
							%>
							</tbody>
							</table>
							</td>
							</tr>
							<tr>							
							<th scope="row">Additional IP Address</th>
							<td colspan="2">
								<table class="table table-bordered  table-striped">
									<tbody>
										<%
											for(IpBlock ipBlock: ipListAdditional){
												String startingIP = IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()); 
												String endingIP = IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress()); 
										%>
											<tr>
												<td><%=(startingIP + "-" + endingIP)%></td>
												<td><b>Expired on </b><%=TimeConverter.getTimeStringByDateFormat(ipBlock.getExpirationTime(), "dd/MM/yyyy") %></td>
											</tr>		
										<%	} %>
									</tbody>
								</table>
							</td>
							</tr>							
<!-- 							<tr> -->
<!-- 								<th scope="row">Connection Balance </th> -->
<%-- 								<td colspan="2"><%=java.lang.Math.round(lliLinkDTO.getBalance()) %> BDT</td> --%>
<!-- 							</tr> -->
							<tr>
								<th scope="row">Description</th>
								<td colspan="2">${lliLink.linkDescription}</td>
							</tr> 
						</tbody>
					</table>
				</div>	
				<div class="row">
					<div class="col-md-12">
						<div class="table-responsive">
							<table class="table table-bordered table-striped">
<!-- 								<thead> -->
<!-- 									<tr> -->
<!-- 										<th colspan="2" class="text-center"> -->
<%-- 											<h4> <%=LliLanguageConstants.REMOTE_END%> Details </h4> --%>
<!-- 										</th> -->
<!-- 									</tr> -->
<!-- 								</thead> -->
						
								<tbody>
<!-- 									<tr> -->
<%-- 										<th scope="row"><%=LliLanguageConstants.END_NAME %></th> --%>
<%-- 										<td class="text-muted">${farEnd.vepName }</td> --%>
<!-- 									</tr> -->
									<tr>
										<th scope="row">Division</th>
										<td class="text-muted"><%=InventoryConstants.mapOfDivisionNameToDivisionID.get(InventoryConstants.districtToDivisionMap.get(farEndDTO.getDistrictID())) %></td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
										<td class="text-muted">${farEnd.districtName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
										<td class="text-muted">${farEnd.upazilaName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></th>
										<td class="text-muted">${farEnd.unionName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></th>
										<td class="text-muted">${farEnd.popName }</td>
									</tr>
									<tr>
										<th scope="row">Router/Switch</th>
										<td class="text-muted">${farEnd.routerName }</td>
									</tr>									
									<tr>
										<th scope="row">Address</th>
										<td class="text-muted">${farEnd.address }</td>
									</tr>
									<tr>
										<th scope="row">Port Type</th>
										<td class="text-muted">${farEnd.portCateogryTypeName }</td>
									</tr>
									<tr>
										<th scope="row">Port</th>
										<td class="text-muted">${farEnd.portName }</td>
									</tr>
									<tr>
										<th scope="row">OFC Type</th>
										<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(farEndDetailsDTO.getCoreType()) %></td>
									</tr> 
									<tr>
										<th scope="row"><%=LliLanguageConstants.LOOP_PROVIDER %></th>
										<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(farEndDTO.getOfcProvider()) %></td>
									</tr>									
									<tr>
										<th scope="row">Terminal Device Provider</th>
										<td class="text-muted"> <%=EndPointConstants.terminalDeviceProvider.get(farEndDTO.getTerminalDeviceProvider()) %></td>
									</tr>
									<%if( farEndDTO.isOFCProvidedByBTCL() ){ %>
									<tr>
										<th scope="row">Outsourcing Company Name </th>
										<td class="text-muted"> <%=UserRepository.getInstance().getUserDTOByUserID( farEndDTO.getOfcProviderID() ).getUsername() %></td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
			</div>
		</div>
		<div class="row">
			<jsp:include page="../../common/fileListHelper.jsp" flush="true">
				<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK %>" />
				<jsp:param name="entityID" value="<%=id %>" />
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
