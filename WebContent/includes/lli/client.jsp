<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%
	String clientName = "";
	String requested = (String) session.getAttribute("clientID");
	long clientID = -1;
	if (requested == null) {
		requested = "";
	}else {
		clientID = requested.equals("")? -1 : Long.parseLong(requested);
		clientName = (clientID == -1) ? "" : AllClientRepository.getInstance().getClientByClientID( clientID ).getLoginName();
	}
	
%>
<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Client Name</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type=text  name=clientName class=form-control value = <%=clientName %>>
		<input type=hidden  name=clientID  value = <%=requested %>>
	</div>
</div>

<script>
	var configObj = {
		source : function (request, response){
			var url = context + 'lli/inventory/ipAddress/getClient.do';
			var param = {};
			param.moduleID = <%=ModuleConstants.Module_ID_LLI%>;
			param.partialName = request.term;
			ajax(url, param, response, 'GET', []);
		}, 
		minLength : 0,
		select : function (e, ui) {
			LOG(ui.item);
			$("input[name='clientID']").val(ui.item.key);
			$("input[name='clientName']").val(ui.item.value);
			return false;
		}	
	};
	var renderItems = function renderItems (ul, data) {
		return $('<li>').append('<a>' + data.value + '</a>').appendTo(ul);
	}
	$(document).ready(function(){
	
		$('input[name=clientName]').autocomplete(configObj).autocomplete('instance')._renderItem = renderItems;
	});
</script>