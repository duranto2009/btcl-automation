<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="upstreamMenu">

	<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-shield"></i> <span class="title">Upstream</span> <span class="arrow"></span></a>
		
	<ul class="sub-menu">
	
	
		<!-- 	CLIENT    -->
		<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT) != -1) {%>--%>
<%--		<li class="nav-item" id="upstream">--%>
<%--			<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i><span class="title">Request</span> <span class="arrow"></span></a>--%>
<%--			--%>
<%--			<ul class="sub-menu">--%>
				<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>--%>
				<%--<li class="nav-item" id="addVpnClient">--%>
					<%--<a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "><i class="fa fa-plus"></i><span class="title">Add</span></a>--%>
				<%--</li>--%>
				<%--<%}%>--%>
				<%----%>
				<%--<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.VPN_CLIENT_SEARCH) != -1) {%>--%>
				<%--<li class="nav-item" id="searchVpnClient">--%>
					<%--<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_VPN %>" class="nav-link "> <i class="fa fa-search"></i><span class="title">Search</span></a>--%>
				<%--</li>--%>
				<%--<%}%>--%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_NEW_REQUEST) != -1) {%>
					<li class="nav-item" id="newUpstreamRequest">
						<a href="${context}upstream/new-request.do" class="nav-link "><i class="fa fa-plus"></i><span class="title">New Request</span></a>
					</li>
		<%}%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_APPLICATINO_LIST) != -1) {%>

		<li class="nav-item" id="searchUpstreamRequest">
						<a href="${context}upstream/request-search.do" class="nav-link "><i class="fa fa-search"></i><span>Application List</span></a>
					</li>
		<%}%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_CONTRACT_LIST) != -1) {%>

		<li class="nav-item" id="searchUpstreamContract">
						<a href="${context}upstream/contract-search.do" class="nav-link "><i class="fa fa-search"></i><span>Contract List</span></a>
					</li>
		<%}%>


<%--		<li class="nav-item" id="searchUpstreamInvoice">--%>
<%--			<a href="${context}upstream/invoice-search.do" class="nav-link "><i class="fa fa-search"></i><span>Invoice List</span></a>--%>
<%--		</li>--%>



		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_CONTRACT_EXTENSION) != -1) {%>

		<li class="nav-item" id="contractExtension">
						<a href="${context}upstream/contract-extension.do" class="nav-link "><i class="fa fa-arrows-alt"></i><span>Contract Extension</span></a>
					</li>
		<%}%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_CONTRACT_BANDWIDTH_CHANGE) != -1) {%>

		<li class="nav-item" id="contractBandwidthChange">
						<a href="${context}upstream/contract-bandwidth-change.do" class="nav-link "><i class="fa fa-edit"></i><span>Contract Bandwidth Change</span></a>
					</li>
		<%}%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.UPSTREAM_CONTRACT_CLOSE) != -1) {%>

		<li class="nav-item" id="contractClose">
						<a href="${context}upstream/contract-close.do" class="nav-link "><i class="fa fa-lock"></i><span>Contract Close</span></a>
					</li>
		<%}%>
		<!-- Report begins -->
		<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.GENERAL_REPORT_UPSTREAM ) != -1) {%>
		<li class="nav-item  "
			id="upStreamReportMenu">
			<a href="javascript:;"
			   class="nav-link nav-toggle">
				<i class="fa fa-file"></i>
				<span>Report</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<%--<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CLIENT_UPSTREAM ) != -1) {%>
				<li class="nav-item"
					id="upStreamClientReportMenu">
					<a href="${context}Report/GetClientReport.do?moduleID=<%=ModuleConstants.Module_ID_UPSTREAM %>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Client Report</span>
					</a>
				</li>
				<%} %>--%>

				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_CONNECTION_UPSTREAM ) != -1) {%>

				<li class="nav-item"
					id="upStreamConnectionReportMenu">
					<a href="${context}report/connection.do?module=<%=ModuleConstants.Module_ID_UPSTREAM%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Connection Report</span>
					</a>
				</li>
				<%} %>
				<%if( menuLoginDTO.getMenuPermission( login.PermissionConstants.REPORT_APPLICATION_UPSTREAM ) != -1) {%>

				<li class="nav-item"
					id="upStreamApplicationReportMenu">
					<a href="${context}report/application.do?module=<%=ModuleConstants.Module_ID_UPSTREAM%>"
					   class="nav-link">
						<i class="fa fa-plus"></i>
						<span>Application Report</span>
					</a>
				</li>
				<%} %>

			</ul>
		</li>
		<%} %>
		<!-- Report ends -->
				
<%--			</ul>--%>
<%--		</li>--%>
		<%--<%}%>--%>

	</ul>
</li>
