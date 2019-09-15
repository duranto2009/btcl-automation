<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i>Request Additional IP Addresses</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal" method="post" action="../../LliLinkIpaddress/submit.do">
			<%LoginDTO loginDTO = (LoginDTO) request.getSession().getAttribute(SessionConstants.USER_LOGIN);%>
			<div class="form-body">
				<%if (loginDTO.getIsAdmin()) { %>
				<div class="form-group">
					<label for="clientIDName" class="col-sm-3 control-label">
						Client Name
					</label>
					<div class="col-sm-6">
						<input id="clientIDName" placeholder="Client Name" type="text" class="form-control" name="clientIDName" required /> 
						<input id="clientID" type="hidden" class="form-control" name="clientID" value="-1" required />
					</div>
					<div class="col-sm-2 hidden">
						<a id="clientHyperLink" target="_blank" href="#" required>
							View Client Details
						</a>
					</div>
				</div>
				<%} else {%>
					<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID()%>">
				<%}%>
				<div class="form-group">
					<label for="lliLinkIDName" class="col-sm-3 control-label"> Connection Name </label>
					<div class="col-sm-6">
						<input type="text" placeholder="Connection  Name" class="linkName form-control" id="lliLinkIDName" name="lliLinkIDName" required />
						<input type="hidden" class="form-control" name="linkID" value="-1" required />
					</div>
					<div class="col-sm-2 hidden">
						<a id="linkHyperlink" target="_blank" href="">
							View Connection Details
						</a>
					</div>
				</div>
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Additional IP Address Count</label>
					<div class="col-sm-6">
						<select class="form-control fe-hide " name="newAdditionalRequestedIpCount" id="newAdditionalRequestedIpCount">
							<option value=0>0</option>
							<option value=4>4</option>
							<option value=8>8</option>
							<option value=16>16</option>
							<option value=32>32</option>
							<option value=64>64</option>
							<option value=128>128</option>
							<option value=256>256</option>
						</select>
					</div>
				</div>
			</div>
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button class="btn btn-submit-btcl" type="submit">Submit</button>
			</div>	
		</form>
	</div>
</div>
<script>
	function getLliLinkList(clientID){
		$("#lliLinkName").html("<option>Select Link</option>");
		ajax(context+"LliAjax/GetLinkList.do", {clientID:clientID}, function(data){
			$.each(data, function(index, value){
				$("#lliLinkName").append("<option value='"+value.ID+"'>"+value.linkName+"</option>");
			});
		}, "GET", [$("#lliLinkName")]);
	}

	$("select[name=lliLinkID]").on("change", function(){
		$("#currentIpaddress").html("<p class=form-control></p>")
		ajax(context+"LliAjax/GetAdditionalIP.do", {lliLinkID: $(this).val()}, function(data){
			data.size ?
			$("#currentIpaddress").html("<p class=form-control>Count: "+data.size+"</p>" +
					"<p class=form-control>Starting Address: "+data.start+"</p>" +
					"<p class=form-control>Ending Address: "+data.end+"</p>")
			 : $("#currentIpaddress").html("<p class=form-control>No Additional IP Address was allocated to this Link</p>");
			
		}, "GET", [$("#lliLinkName")]);
	});
</script>


<script src="${context}assets/scripts/lli/link/linkBandwidthVsClientAutoComplete.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/lli/link/link-bandwidth-change-validation.js" type="text/javascript"></script>
