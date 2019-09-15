<%@page import="org.apache.log4j.Logger"%>
<%@page import="login.PermissionConstants"%>
<%@page import="crm.repository.*" %>
<%

	boolean isNOC = menuLoginDTO.isNOC();
	boolean isCrmEmployee = !(CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(menuLoginDTO.getUserID()).isEmpty());
	boolean isCrmAdmin = (menuLoginDTO.getMenuPermission(PermissionConstants.CRM_EMPLOYEE) != -1);
	boolean isElegibleToViewCrmMenu = (isCrmEmployee || isCrmAdmin);
	Logger logger = Logger.getLogger("menu_crm_admin");
	if(isElegibleToViewCrmMenu) {%>
		<li class="nav-item" id="crmMenu">
			<a href="javascript:;" class="nav-link nav-toggle"> 
				<i class="fa fa-recycle"></i> 
				<span>Complain & Support</span> 
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu">
			<%if(isCrmEmployee && !isNOC){ %>
				<li class="nav-item" id="crmComplain">
					<a href="javascript:;" class="nav-link nav-toggle"> 
						<i class="fa fa-male"></i> 
						<span>Complain</span> 
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<li class="nav-item" id="searchCrmComplain">
							<a href="${context}CrmComplainSearch/Complains.do" class="nav-link ">
								<i class="fa fa-search"></i><span>Search</span>
							</a>
						</li>
					</ul>
				</li>
			<%} %>
			<%if(isNOC){ %>
				<li class="nav-item" id="crmClientComplain">
					<a href="javascript:;" class="nav-link nav-toggle"> 
						<i class="fa fa-user"></i> 
						<span>Client Complain Pool</span> 
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<li class="nav-item" id="crmClientComplainAdd">
							<a href="${context}CrmClientComplain/CreateComplain.do" class="nav-link ">
								<i class="fa fa-plus"></i><span>Add</span>
							</a>
						</li>
						<li class="nav-item" id="crmClientComplainSearch">
							<a href="${context}CrmClientComplainSearch/Complains.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>Search</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="nav-item" id="crmComplain">
					<a href="javascript:;" class="nav-link nav-toggle"> 
						<i class="fa fa-user-circle"></i> 
						<span>Sub Complains</span> 
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<li class="nav-item" id="searchCrmComplain">
							<a href="${context}CrmComplainSearch/Complains.do" class="nav-link ">
								<i class="fa fa-search"></i><span>Search</span>
							</a>
						</li>
						
						<li class="nav-item" id="searchCrmActivityLog">
							<a href="${context}CrmActivityLog/getCrmActivityLogSearch.do" class="nav-link ">
								<i class="fa fa-plus"></i><span>ActivityLog Search</span>
							</a>
						</li>
					</ul>
				</li>
				
			<%} %>
			<%if(isCrmAdmin) {%>
				<li class="nav-item" id="crmEmployee">
					<a href="javascript:;" class="nav-link nav-toggle"> 
						<i class="fa fa-users"></i> 
						<span>Employee</span> 
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<!-- 
						<li class="nav-item" id="viewCrmEmployee">
							<a href="${context}crm/tree/crmEmployeeView.jsp" class="nav-link ">
								<i class="fa fa-plus"></i>
								<span>Manage</span>
							</a>
						</li>
						
						-->
						<li class="nav-item" id="searchCrmEmployee">
							<a href="${context}CrmEmployee/searchEmployee.do" class="nav-link ">
								<i class="fa fa-search"></i>
								<span>Search</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="nav-item" id="crmDepartmentSubmenu1">
					<a href="javascript:;" class="nav-link nav-toggle"> 
						<i class="fa fa-archive"></i> 
						<span>Department</span>
						<span class="arrow"></span>
					</a>
					<ul class="sub-menu">
						<li class="nav-item " id="crmDepartmentSubmenu2">
							<a href="${context}CrmDesignation/department/getAddDepartment.do"
								class="nav-link "> 
								<i class="fa fa-plus"></i>
								<span>Add</span>
							</a>
						</li>
						<li class="nav-item " id="crmDepartmentSubmenu3">
							<a href="${context}CrmDesignation/department/getSearchDepartment.do"
								class="nav-link "> 
								<i class="fa fa-search"></i>
								<span>Search</span>
							</a>
						</li>
					</ul>
				</li>
				
			<%} %>
			</ul>
		</li>
	<%}%>
	
	<!-- 			<li class="nav-item" id="crmDepartmentSubmenu1"> -->
<!-- 					<a href="javascript:;" class="nav-link nav-toggle">  -->
<!-- 						<i class="fa fa-archive"></i>  -->
<!-- 						<span>Department</span> -->
<!-- 						<span class="arrow"></span> -->
<!-- 					</a> -->
<!-- 					<ul class="sub-menu"> -->
<!-- 						<li class="nav-item " id="crmDepartmentSubmenu2"> -->
<%-- 							<a href="${context}CrmDesignation/department/getAddDepartment.do" --%>
<!-- 								class="nav-link ">  -->
<!-- 								<i class="fa fa-plus"></i> -->
<!-- 								<span>Add</span> -->
<!-- 							</a> -->
<!-- 						</li> -->
<!-- 						<li class="nav-item " id="crmDepartmentSubmenu3"> -->
<%-- 							<a href="${context}CrmDesignation/department/getSearchDepartment.do" --%>
<!-- 								class="nav-link ">  -->
<!-- 								<i class="fa fa-search"></i> -->
<!-- 								<span>Search</span> -->
<!-- 							</a> -->
<!-- 						</li> -->
<!-- 					</ul> -->
<!-- 				</li> -->

	