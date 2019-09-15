<li class="nav-item" id="crmMenu"><a href="javascript:;"
	class="nav-link nav-toggle"> <i class="fa fa-recycle"></i> <span
		class="title">Support & Complain</span> <span class="arrow"></span>
</a>
	
	<ul class="sub-menu">
		<%if(loginDTO.isNOC() || loginDTO.getIsAdmin()){ %>
		<li class="nav-item" id="crmComplain"><a href="javascript:;"
			class="nav-link nav-toggle"> <i class="fa fa-user"></i> <span
				class="title">Complain</span> <span class="arrow"></span></a>
			<ul class="sub-menu">
				<li class="nav-item" id="searchCrmComplain"><a
					href="${context}CrmComplainSearch/Complains.do" class="nav-link ">
						<i class="fa fa-plus"></i><span class="title">Search</span>
				</a></li>
			</ul></li>
		<%} %>
		<%if(loginDTO.isNOC() || !loginDTO.getIsAdmin()){ %>
		<li class="nav-item" id="crmClientComplain"><a href="javascript:;"
			class="nav-link nav-toggle"> <i class="fa fa-user"></i> <span
				class="title">Complain</span> <span class="arrow"></span>
		</a>
			<ul class="sub-menu">
				<%if(!loginDTO.isNOC()){ %>
				<li class="nav-item" id="crmClientComplainAdd"><a
					href="${context}CrmClientComplain/CreateComplain.do" class="nav-link ">
						<i class="fa fa-plus"></i><span class="title">Add</span>
				</a></li>
				<%} %>
				<li class="nav-item" id="crmClientComplainSearch"><a
					href="${context}CrmClientComplainSearch/Complains.do" class="nav-link ">
						<i class="fa fa-plus"></i><span class="title">Search</span>
				</a></li>
			</ul></li>
		<%} %>
		<%if(loginDTO.isNOC() || loginDTO.getIsAdmin()){ %>
		<li class="nav-item" id="crmEmployee"><a href="javascript:;"
			class="nav-link nav-toggle"> <i class="fa fa-user"></i> <span
				class="title">Employee</span> <span class="arrow"></span>
		</a>
			<ul class="sub-menu">
				<li class="nav-item" id="viewCrmEmployee"><a
					href="${context}crm/tree/crmEmployeeView.jsp" class="nav-link ">
						<i class="fa fa-plus"></i><span class="title">Manage</span>
				</a></li>
			</ul></li>
		<%} %>
	</ul></li>