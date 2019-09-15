<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="common.repository.AllClientRepository"%>
<%
	String context = "" + request.getContextPath() + "/";
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	if (loginDTO.getIsAdmin()) {
		System.out.print(AllClientRepository.getInstance().getAllVpnCleint());
		request.setAttribute("clientList", AllClientRepository.getInstance().getAllVpnCleint());
	}
%>
<!-- Horizontal Form -->
<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>New Connection Form
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal" method="post" id="formConnection" action="../../VpnConnectionAction.do?mode=add">
			<div class="form-body">
			<% if(loginDTO.getIsAdmin()){ %>
				<%-- <datalist id="clientList">
					<c:forEach var="clientDTO" items="${clientList}">
						<option data-value='${clientDTO.clientID}'
							value='${clientDTO.loginName}'></option>
					</c:forEach>
				</datalist> --%>
				
			
				<div class="form-group">
					<label for="cnName" class="col-sm-2 control-label">Client
						Name</label>
					<div class="col-sm-10">
					<!-- 	<input type="text" class="form-control" id="clintID" name="serviceClientID"
							 list="clientList"
							autocomplete="off"> -->
						<input type="hidden" name="clientID" id="clientID"/>
						<select id="clientList" class="form-control select2"
							name="serviceClientID" onchange="setClientID()">
							<c:forEach var="clientDTO" items="${clientList}">
								<option value='${clientDTO.clientID}'>${clientDTO.loginName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<%} %>
				<div class="form-group">
					<label for="cnName" class="col-sm-2 control-label">Connection
						Name</label>

					<div class="col-sm-10">
						<input type="text" class="form-control" name="cnName"
							placeholder="Connection Name" required>
					</div>
				</div>
				<div class="form-group">
					<label for="cnDescription" class="col-sm-2 control-label">Description</label>

					<div class="col-md-10">
						<textarea class="form-control" rows="3" name="cnDescription"
							placeholder="Give a short description..."></textarea>
					</div>
				</div>

				<div class="form-actions fluid">
					<div class="row">
						<div class="col-md-offset-4 col-md-8">
							<button name="B2" type="submit" class="btn green">Submit</button>
							<button name="B1" value="Reset" type="button" class="btn default">Cancel</button>
						</div>
					</div>
				</div>

				<!-- /.input group -->
			</div>
			<!-- /.box-body -->
			<!-- 			<div class="box-footer">
				<button type="submit" class="btn btn-info pull-right">Submit</button>
			</div> -->
			<!-- /.box-footer -->
		</form>
	</div>
</div>

<script src="${context}vpn/connection/connection.js" type="text/javascript"></script>

