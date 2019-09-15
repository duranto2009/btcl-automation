<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="adslMenu">
	<a href="javascript:;" class="nav-link nav-toggle"> 
		<i class="fa fa-sitemap"></i> <span class="title">ADSL</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">
	
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ADSL_CLIENT) != -1) {%>
			<li class="nav-item" id="adslClientMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
					<span class="title">Client</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">
				
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ADSL_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addAdslClientMenu">
							<a href="${context}client/client/clientAdd.jsp?moduleID=<%=ModuleConstants.Module_ID_ADSL%>" class="nav-link "> 
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>	
					<%}%>
					
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ADSL_CLIENT_SEARCH) != -1) {%>
					<li class="nav-item" id="searchAdslClientMenu">
						<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_ADSL %>"	class="nav-link "> 
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>
					
				</ul>
			</li>
		<%}%>

	</ul>
</li>