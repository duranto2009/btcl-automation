<%@page import="login.PermissionConstants"%>
<%@page import="common.ModuleConstants"%>

<li class="nav-item" id="dnshostingMenu">
	<a href="javascript:;" class="nav-link nav-toggle"> 
		<i class="fa fa-cloud"></i> <span class="title">DNS Hosting</span> <span class="arrow"></span>
	</a>
	<ul class="sub-menu">
	
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_CLIENT) != -1) {%>
			<li class="nav-item" id="dnshostingClientMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-user"></i> 
					<span class="title">Client</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">
				
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_CLIENT_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addDnshostingClientMenu">
							<a href="${context}dnshosting/client/clientAdd.jsp" class="nav-link "> 
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>	
					<%}%>
					
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_CLIENT_SEARCH) != -1) {%>
					<li class="nav-item" id="searchDnshostingClientMenu">
						<a href="${context}SearchClient.do?moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING %>"	class="nav-link "> 
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>
					
				</ul>
			</li>
		<%}%>
		
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_DOMAIN) != -1) {%>
			<li class="nav-item" id="dnshostingDomainMenu">
				<a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-location-arrow"></i> 
					<span class="title">Domain</span> <span class="arrow"></span>
				</a>
				<ul class="sub-menu">
				
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_DOMAIN_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="addDnshostingDomainMenu">
							<a href="${context}DNS/Domain/add.do" class="nav-link "> 
								<i class="fa fa-plus"></i><span class="title">Add</span>
							</a>
						</li>	
					<%}%>
					
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.DNSHOSTING_DOMAIN_SEARCH) != -1) {%>
					<li class="nav-item" id="searchDnshostingDomainMenu">
						<a href="${context}DNS/Domain/search.do" class="nav-link "> 
							<i class="fa fa-search"></i><span class="title">Search</span>
						</a>
					</li>
					<%}	%>
					
				</ul>
			</li>
		<%}%>

	</ul>
</li>