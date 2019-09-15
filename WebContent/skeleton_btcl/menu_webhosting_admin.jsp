<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="webHostingMenu">
	<a href="javascript:;" class="nav-link nav-toggle"> 
		<i class="fa fa-globe"></i> <span class="title">Web Hosting</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">
	
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.WEBHOSTING_CLIENT) != -1) {%>
			<li class="nav-item" id="webHostingClientMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
					<span class="title">Client</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">
				
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.WEBHOSTING_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addWebHostingClientMenu">
							<a href="${context}webhosting/client/clientAdd.jsp" class="nav-link "> 
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>	
					<%}%>
					
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.WEBHOSTING_CLIENT_SEARCH) != -1) {%>
					<li class="nav-item" id="searchWebHostingClientMenu">
						<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_WEBHOSTING %>"	class="nav-link "> 
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>
					
				</ul>
			</li>
		<%}%>

	</ul>
</li>