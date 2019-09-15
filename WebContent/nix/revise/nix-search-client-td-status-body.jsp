<%@page import="util.ServiceDAOFactory"%>
<%@page import="nix.revise.NIXClientTDService"%>
<%@page import="lli.LLIClientService"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<%@page import="nix.revise.NIXProbableTDClient"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%
	String url = "nix/revise/searchprobable";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_NIX_PROBABLE_TD_CLIENT;
	List<NIXProbableTDClient> data = (ArrayList<NIXProbableTDClient>) session.getAttribute(SessionConstants.VIEW_NIX_PROBABLE_TD_CLIENT);
	data = (data == null ? new ArrayList<NIXProbableTDClient>() : data);
	
%>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<div id=btcl-application>
<btcl-body>
	<table class="table table-hover table-striped table-bordered">
		<thead>
			<tr>
				<th>Client Name</th>
				<th>TD Date</th>
				<th>Days Left</th>
				<th>TD</th>
			</tr>
		</thead>
		<tbody>
		<% for(NIXProbableTDClient listItem : data) {
			String clientName = "";
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getClientID());
			if(clientDTO != null) {
				clientName = clientDTO.getLoginName();
			}
			boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(NIXClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
			if(!isAlreadyTemporarilyDisconneced){
		%>
			<tr>
				<td><%=clientName %></td>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getTdDate(), "dd/MM/yyyy") %></td>
				<td><%= ((listItem.getTdDate() - System.currentTimeMillis()) / TimeConverter.MILLS_IN_A_DAY) %></td>
				<td><button type=button class='btn form-control' @click=td(<%=listItem.getClientID()%>) >TD</button></td>
			</tr>
		<%}
		}%>
		</tbody>
	</table>
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
</btcl-body>
	
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
            var url = "tdrequest";
            axios.post(context + 'nix/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/revise/search.do';
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
