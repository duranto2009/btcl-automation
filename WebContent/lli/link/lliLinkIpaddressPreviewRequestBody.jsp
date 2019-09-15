<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.link.request.ipaddress.LliLinkIpaddressRequestDTO"%>
<%
	String titleForm = request.getParameter("title");
	LliLinkIpaddressRequestDTO lliLinkIpaddressRequestDTO = (LliLinkIpaddressRequestDTO) 
														request.getAttribute("ipAddressRequestDTO");
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
    Logger logger = Logger.getLogger(this.getClass());

	try{
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i><%=titleForm %>
		</div>
		<div class="actions">
			<input type="button" id="printBtn" class="btn btn-default" style="padding-left: 15px; padding-right: 15px;" value="Print" />   
		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th colspan="3" class="text-center">
									<h3>Ip Address Request Preview </h3>
								</th >
							</tr>
						</thead>
						<tbody>
							
							<tr>
								<th scope="row">Client Name</th>
								<td class="text-muted"><%=AllClientRepository.getInstance().getClientByClientID(lliLinkIpaddressRequestDTO.getClientID()).getLoginName() %></td>
							</tr>
							<tr>
								<th scope="row">Connection Name</th>
								<td class="text-muted"><%=lliLinkService.getLliLinkByLliLinkID(lliLinkIpaddressRequestDTO.getEntityID()).getName() %></td>
							</tr>
							<tr>
								<th scope="row">IP Type</th>
								<td class="text-muted">Additional</td>
							</tr>
							<tr>
								<th scope="row">IP Count</th>
								<td class="text-muted"><%=lliLinkIpaddressRequestDTO.getNewAdditionalRequestedIpCount() %></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>

<script src="${context}assets/scripts/vpn/link/linkBandwidth.js" type="text/javascript"></script>
<script src="${context}assets/scripts/printThis.js"></script>
<script>

$(document).ready(function(){
    $("#printBtn").click(function(){
		$("#printContent").printThis();
    })
})

</script>