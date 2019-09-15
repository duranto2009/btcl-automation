<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="iigMenu">
	<a href="javascript:;" class="nav-link nav-toggle"> 
		<i class="fa fa-chain"></i> <span class="title">IIG</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">
	
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IIG_CLIENT) != -1) {%>
			<li class="nav-item" id="iigClientMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
					<span class="title">Client</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">
				
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IIG_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addIigClientMenu">
							<a href="${context}iig/client/clientAdd.jsp" class="nav-link "> 
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>	
					<%}%>
					
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_CLIENT_SEARCH) != -1) {%>
					<li class="nav-item" id="searchIigClientMenu">
						<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_IIG %>"	class="nav-link "> 
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>
					
				</ul>
			</li>
		<%}%>

	</ul>
</li>