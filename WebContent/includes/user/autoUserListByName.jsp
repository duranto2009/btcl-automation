<%@page import="common.repository.AllClientRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="user.UserDTO"%>
<%@page import="common.ClientDTO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="common.UtilityConstants"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>

<%@ page import="sessionmanager.SessionConstants" %>
<%
    login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>



<%
String propertyName=request.getParameter("propertyName");
String propertyNameID=propertyName+"ID";
String propertyNameTextID=propertyName+"TextID";
String propertyNameTextIDType = propertyName + "Type";

String labelName=UtilityConstants.propertyNameToPropertyLabelMap.get(propertyName);
String labelClass=request.getParameter("labelClass");
String inputWrapperClass=request.getParameter("inputWrapperClass");
if(StringUtils.isBlank(labelName)){
	labelName="Invalid label. Please Check label in SessionConstants";
}
%>

<%if(loginDTO.getUserID()>0){ 
	String userID ="";
	UserDTO userDTO=null;
	if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedByAccountID")){
		userID=  (String)session.getAttribute("arRequestedByAccountID");
		//session.removeAttribute("arRequestedByAccountID");
	}else if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedToAccountID")){
		userID=  (String)session.getAttribute("arRequestedToAccountID");
		//session.removeAttribute("arRequestedToAccountID");
	}
	String name="";
	String id="";
	if(StringUtils.isNotBlank(userID)){
		if(userID.startsWith("-")){
			userDTO=UserRepository.getInstance().getUserDTOByUserID(Long.parseLong(userID.replace("-", "")));
			name = userDTO.getUsername();
			id = userID;
		} else{
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(Long.parseLong(userID));
			name = clientDTO.getLoginName();
			id=String.valueOf(clientDTO.getClientID());
		}
	}
	
	String requestByOrToType ="";
	if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedByAccountID")){
		if(session.getAttribute("arRequestedByAccountIDType")!=null){
			requestByOrToType=  (String)session.getAttribute("arRequestedByAccountIDType");
		}
	}else if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedToAccountID")){
		if(session.getAttribute("arRequestedToAccountIDType")!=null){
			requestByOrToType=  (String)session.getAttribute("arRequestedToAccountIDType");
		}
	}
%>

<div class="form-group">
	<label for="<%=propertyName%>" class="control-label col-md-4 col-sm-4 col-xs-4"><%=labelName + " Type"%></label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control" size="1" id=<%=propertyName+"Type"%> name=<%=propertyName+"Type"%>>
			<option value=0 <%=(requestByOrToType.equals("0") ? "selected=\"selected\"" : "")%>> All </option>
		  	<option value=1 <%=(requestByOrToType.equals("1") ? "selected=\"selected\"" : "")%>> Client </option>
		  	<option value=2 <%=(requestByOrToType.equals("2") ? "selected=\"selected\"" : "")%>> System</option>
		  	<%if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedByAccountID")){%>
		  	<option value=3 <%=(requestByOrToType.equals("3") ? "selected=\"selected\"" : "")%>> Scheduler</option>
		  	<%}%>
		</select>
	</div>
</div>

<div class="form-group">
	<label for="" class="<%=labelClass%>"><%=labelName%></label>
	<div class="<%=inputWrapperClass%>"> 
	    <input id="<%=propertyNameTextID%>" type="text" class="form-control" autocomplete="off" value="<%=name%>" >
		<input id="<%=propertyNameID%>"  name="<%=propertyName%>" type="hidden" value="<%=id%>" >
	</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	var propertyNameTextID="#"+'<%=propertyNameTextID%>';
	var propertyNameID="#"+"<%=propertyNameID%>";
	var propertyNameTextIDType = "#"+"<%=propertyNameTextIDType%>";
	var requestModuleID = <%=request.getParameter("moduleID")%>;
	
	$(propertyNameTextID).autocomplete({
		source : function(request, response) {
			$(propertyNameID).val("");
			if($(propertyNameTextIDType).val() == "0"){
				callAjax(context + 'AutoComplete.do?need=usersAndClient', {name:request.term, moduleID: requestModuleID, status:"active"}, response, "GET");
			} else if($(propertyNameTextIDType).val() == "1"){
				callAjax(context + 'AutoComplete.do?need=client', {name:request.term, moduleID: requestModuleID, status:"active"}, response, "GET");
			} else if($(propertyNameTextIDType).val() == "2"){
				callAjax(context + 'AutoComplete.do?need=userForRequestSearch', {name:request.term, moduleID: requestModuleID, status:"active"}, response, "GET");
			} else{
				response({});
			}
		},
		minLength : 1,
		select : function(e, ui) {
			$(propertyNameTextID).val(ui.item.data);
			$(propertyNameID).val(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};
	
	var clearID = function(){
		if($(propertyNameTextID).val().trim()==""){
			$(propertyNameTextID).val('');
			$(propertyNameID).val('');
		}
	}
	$(propertyNameTextID).blur(clearID).click(clearID).change(clearID).keyup(clearID).keydown(clearID);
	$(propertyNameTextIDType).change(function(){
		$(propertyNameTextID).val('');
		$(propertyNameID).val('');
	});
	
});
</script>
<%} %>

<%if(loginDTO.getAccountID()>0){ 
	String requestID ="";
	if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedByAccountID")){
		if(session.getAttribute("arRequestedByAccountID")!=null){
			requestID=  (String)session.getAttribute("arRequestedByAccountID");
		}
	}else if(StringUtils.isNotBlank(propertyName) &&  propertyName.equals("arRequestedToAccountID")){
		if(session.getAttribute("arRequestedToAccountID")!=null){
			requestID=  (String)session.getAttribute("arRequestedToAccountID");
		}
	}
%>
<div class="form-group">
	<label for="<%=propertyName%>" class="control-label col-md-4 col-sm-4 col-xs-4 <%=labelClass%>"><%=labelName%></label>
	<div class="col-md-8 col-sm-8 col-xs-8"> <!-- inputWrapperClass -->
		<select id="<%=propertyNameID%>" class="form-control" size="1" name="<%=propertyName%>">
			<option <%=(requestID.length()<=0 ? "selected=\"selected\"" : "")%>> All </option>
		  	<option value="<%=loginDTO.getAccountID()%>" <%=((requestID.length()>0 && requestID.equals(""+loginDTO.getAccountID())) ? "selected=\"selected\"" : "")%>> Self </option>
		  	<option value="-1" <%=((requestID.length()>0 && requestID.equals("-1")) ? "selected=\"selected\"" : "")%>> System</option>
		</select>
	</div>
</div>
<%} %>


<%
//request.getSession().removeAttribute(propertyName);
//request.getSession().removeAttribute(propertyName+"Type");
%>