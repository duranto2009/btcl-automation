<%@page import="common.repository.AllClientRepository" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="vpn.td.VPNProbableTD" %>
<%@ page import="common.ClientDTO" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="vpn.td.VPNClientTDService" %>
<%@ page import="util.TimeConverter" %>
<%
	String msg = null;
	String url = "vpn/td/search";
	String navigator = SessionConstants.NAV_VPN_PROBABLE_TD_CLIENT;
	String context = "../../.." + request.getContextPath() + "/";
%>

<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>"/>
	<jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>


<div class="portlet box" id="btcl-application">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
				<tr>
					<th>Client Name</th>
					<th>TD Date</th>
					<th>Days Left</th>
					<th>TD</th>
				</tr>
				</thead>
				<tbody>
				<%
					List<VPNProbableTD> data = ( List<VPNProbableTD> )session.getAttribute(SessionConstants.VIEW_VPN_PROBABLE_TD_CLIENT);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							VPNProbableTD vpnProbableTD = (VPNProbableTD) data.get(i);
							String clientName = "";
							ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnProbableTD.getClientID());
							if(clientDTO != null) {
								clientName = clientDTO.getLoginName();
							}
							boolean isAlreadyTemporarilyDisconneced =
									ServiceDAOFactory.getService(VPNClientTDService.class).isClientTemporarilyDisconnected(vpnProbableTD.getClientID());
							if(!isAlreadyTemporarilyDisconneced){
				%>
				<tr>
					<td style="font-weight: bold">
						<%=clientName %>
					</td>

					<td>
						<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnProbableTD.getTdDate(), "dd/MM/yyyy") %>

					</td>

					<td>
						<%= ((vpnProbableTD.getTdDate() - System.currentTimeMillis()) / TimeConverter.MILLS_IN_A_DAY) %>
					</td>
					<td>
						<button type=button class='btn form-control' @click=td(<%=vpnProbableTD.getClientID()%>) >TD
						</button>
					</td>

				</tr>
				<%
						}
						}
				%>
				</tbody>
				<% } %>
			</table>
		</div>


		<div class="modal fade" id="modal" role="dialog">
			<div class="modal-dialog modal-lg" style="width: 50%;">
				<div class="modal-content">
					<div class="modal-body">
						<div>
							<div class="form-body">
								<btcl-field title="Adviced Date">
									<btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker>
								</btcl-field>
								<btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
							</div>
						</div>


					</div>
					<div class="modal-footer">
						<button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
						<button type="button" class="btn btn-danger" data-dismiss="modal">Close
						</button>
					</div>
				</div>

			</div>
		</div>

	</div>
</div>
<script>
	var vue = new Vue({
		el : '#btcl-application',

		data : {
			context : context,
			revise: {
				client: {}
			},

		},
		methods : {
			td: function (id) {
				this.revise.client.key = id;
				$('#modal').modal({show: true});
			},
			submitData: function () {
				var url = "request";
				axios.post(context + 'vpn/td/' + url + '.do', {'data': JSON.stringify(this.revise)})
						.then(result => {
							if (result.data.responseCode == 2) {
								toastr.error(result.data.msg);
							} else if (result.data.responseCode == 1) {
								toastr.success("Your request has been processed", "Success");
								window.location.href = context + 'vpn/link/search.do';
							}
							else {
								toastr.error("Your request was not accepted", "Failure");
							}
						})
						.catch(function (error) {
						});
			},
		}
	});
</script>
