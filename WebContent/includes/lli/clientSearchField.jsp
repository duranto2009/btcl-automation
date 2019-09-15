<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<% 
LoginDTO loginDTO = (LoginDTO)request.getSession().getAttribute(SessionConstants.USER_LOGIN);



if(loginDTO.getIsAdmin()) {%>
	<div class="form-group">
		<label class="control-label col-md-4">Client Name</label>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<input id="clientName" placeholder="Type to Select Client..." type="text" class="form-control">
 			<input id="clientID" type="hidden" name="clientID" value="">
		</div>
	</div>
	<script>
	// Here is another one
	var moduleID = <%=ModuleConstants.Module_ID_LLI%>;
	// passing through !!!!
	$(document).ready(function(){
		$("#clientName").autocomplete({
			source: function(request, response) {
				var url = '../../AutoComplete.do?need=client&moduleID='+ moduleID;
				ajax(url, {name : request.term.trim()}, response, "GET", [$("#clientName")]);
				$('#clientID').val("");
			},
			minLength: 1,
			select: function(e, ui) {
				$('#clientName').val(ui.item.data);
				$('#clientID').val(ui.item.id);
				return false;
			},
		}).autocomplete("instance")._renderItem = function(ul, item) {
			return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
		};
	});
	</script>

<%} else {%>	
	<div class="form-group">
		<label class="control-label col-md-4">Client Name</label>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<p class="form-control" ><%=AllClientRepository.getInstance().getClientByClientID(loginDTO.getAccountID()).getName()%></p>
		</div>
	</div>
<%}%>

	