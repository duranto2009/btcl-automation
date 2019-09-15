<%@page import="login.PermissionConstants"%>

<li class="nav-item" id="ip-management">
	<a href="javascript:;" class="nav-link nav-toggle">
		<i class="fa fa-chain"></i>
		<span>IP Management</span>
		<span class="arrow"></span>
	</a>
	<ul class="sub-menu">
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_SUBNET) != -1) {%>
			<li class="nav-item" id="ip-management-subnet">
				<a href="javascript:;" class="nav-link nav-toggle"> 
					<i class="fa fa-cog"></i> 
					<span>Subnet Tool</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_SUBNET_V4) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="subnet-v4">
							<a href="${context}ip/subnet/v4.do" class="nav-link ">
						   		<i class="fa fa-angle-double-down"></i>
							   	<span>IPv4</span>
							</a>
						</li>
					<%} %>
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_SUBNET_V6) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="subnet-v6">
							<a href="${context}ip/subnet/v6.do" class="nav-link ">
						   		<i class="fa fa-angle-double-up"></i>
							   	<span>IPv6</span>
							</a>
						</li>
					<%} %>	
				</ul>	
			</li>
		<%}%>
        <li class="nav-item" id="ip-management-region">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="fa fa-cog"></i>
                <span>IP Region</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <li class="nav-item" id="ip-region-create">
                    <a href="${context}ip/region/ipRegionCreatePage.do" class="nav-link ">
                        <i class="fa fa-angle-double-down"></i>
                        <span>Create</span>
                    </a>
                </li>

                <li class="nav-item" id="ip-region-search">
                    <a href="${context}ip/region/searchIPRegion.do" class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>

            </ul>
        </li>
		<li class="nav-item" id="ip-management-sub-region">
			<a href="javascript:;" class="nav-link nav-toggle">
				<i class="fa fa-cog"></i>
				<span>IP Sub-Region</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">
				<li class="nav-item" id="ip-sub-region-create">
					<a href="${context}ip/region/ipSubRegionCreatePage.do" class="nav-link ">
						<i class="fa fa-angle-double-down"></i>
						<span>Create</span>
					</a>
				</li>
                <li class="nav-item" id="all-ip-sub-regions">
                    <a href="${context}ip/region/showAllSubRegions.do" class="nav-link ">
                        <i class="fa fa-search"></i>
                        <span>Show All Sub-Regions</span>
                    </a>
                </li>
			</ul>


		</li>
		<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_INVENTORY) != -1) {%>
			<li class="nav-item" id="ip-management-inventory">
				<a href="javascript:;" class="nav-link nav-toggle">
					<i class="fa fa-cog"></i>
					<span>Inventory</span>
					<span class="arrow"></span>
				</a>
				<ul class="sub-menu">
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_INVENTORY_ADD) >= PermissionConstants.PERMISSION_FULL) {%>
						<li class="nav-item" id="inventory-add">
							<a href="${context}ip/inventory/add.do" class="nav-link ">
						   		<i class="fa fa-plus"></i>
							   	<span>Add</span>
							</a>
						</li>
					<%} %>
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_INVENTORY_SEARCH) >= PermissionConstants.PERMISSION_FULL) {%>
					<li class="nav-item" id="inventory-search">
						<a href="${context}ip/inventory/search.do" class="nav-link ">
							<i class="fa fa-search"></i>
							<span>Search</span>
						</a>
					</li>
					<%} %>
					<%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.IPADDRESS_INVENTORY_NAT) >= PermissionConstants.PERMISSION_FULL) {%>
					<li class="nav-item" id="inventory-nat">
						<a href="${context}ip/inventory/nat.do" class="nav-link ">
							<i class="fa fa-link"></i>
							<span>NAT</span>
						</a>
					</li>
					<%} %>

				</ul>
			</li>
		<%}%>
		<%if (menuLoginDTO.getMenuPermission(PermissionConstants.IPADDRESS_USAGE_SEARCH) != -1) {%>
		<li class="nav-item" id="ip-management-usage-search">
			<a href="${context}ip/usage/search.do" class="nav-link">
				<i class="fa fa-search"></i>
				<span>Usage Search</span>
			</a>

		</li>
		<%}%>
	</ul>
</li>