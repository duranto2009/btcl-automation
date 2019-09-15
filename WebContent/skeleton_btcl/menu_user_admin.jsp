<li class="nav-item" id="userAndRoleManagement">
    <a href="javascript:void(0);" class="nav-link nav-toggle"> 
        <i class="fa fa-users"></i> 
        <span>General</span>
        <span class="arrow"></span>
    </a>
    <ul class="sub-menu">
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.USER_MANAGEMENT) != -1) {%>
        <li class="nav-item" id="userManagement">
            <a href="javascript:void(0);" class="nav-link nav-toggle"> 
                <i class="fa fa-user-plus"></i> 
                <span>User Management</span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.USER_ADD) != -1) {%>
                <li class="nav-item" id="userAdd">
                    <a href="${context}users/userAdd.jsp" class="nav-link"> 
                        <i class="fa fa-plus"></i> 
                        <span>Add</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.USER_SEARCH) != -1) {%>
                <li class="nav-item" id="userSearch">
                    <a href="${context}SearchUser.do" class="nav-link"> 
                        <i class="fa fa-search"></i> 
                        <span>Search</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ROLE_MANAGEMENT) != -1) {%>
        <li class="nav-item" id="roleManagement">
            <a href="javascript:void(0);" class="nav-link nav-toggle"> 
                <i class="fa icon-key"></i>
                <span>Role Management</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
            <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ROLE_ADD) != -1) {%>
                <li class="nav-item" id="roleAdd">
                    <a href="${context}getAddRole.do" class="nav-link"> 
                        <i class="fa fa-plus"></i>
                        <span> Add</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(login.PermissionConstants.ROLE_SEARCH) != -1) {%>
                <li class="nav-item" id="roleSearch">
                    <a href="${context}ViewRole.do" class="nav-link">
                        <i class="fa fa-search"></i> 
                        <span> Search</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
        <li class="nav-item" id="client-classification-menu">

            <%if (menuLoginDTO.getMenuPermission(PermissionConstants.CLIENT_CLASSIFICATION_MANAGEMENT) != -1) {%>
            <a href="javascript:void(0);" class="nav-link nav-toggle">
                <i class="fa fa-hand-peace-o"></i>
                <span class="title">Client Classification</span>
                <span class="arrow"></span>
            </a>
            <%}%>
            <ul class="sub-menu">
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.ADD_CLIENT_TYPE) != -1) {%>
                <li class="nav-item" id="add-client-type">
                    <a href="${context}client-classification/add-client-type-page.do" class="nav-link ">
                        <i class="fa fa-plus"></i>
                        <span>Add Type</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.ADD_CLIENT_CATEGORY) != -1) {%>
                <li class="nav-item" id="add-client-category">
                    <a href="${context}client-classification/add-client-category-page.do" class="nav-link ">
                        <i class="fa fa-plus"></i>
                        <span>Add Category</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.MODIFY_CLIENT_TYPE) != -1) {%>
                <li class="nav-item" id="modify-client-type">
                    <a href="${context}client-classification/modify-client-type-page.do" class="nav-link ">
                        <i class="fa  fa-question"></i>
                        <span>Modify Type</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.MODIFY_CLIENT_CATEGORY) != -1) {%>
                <li class="nav-item" id="modify-client-category">
                    <a href="${context}client-classification/modify-client-category-page.do" class="nav-link ">
                        <i class="fa fa-question"></i>
                        <span>Modify Category</span>
                    </a>
                </li>
                <%}%>
                <%if (menuLoginDTO.getMenuPermission(PermissionConstants.MODIFY_CLIENT_TARIFF_CATEGORY) != -1) {%>
                <li class="nav-item" id="modify-client-tariff-category">
                    <a href="${context}client-classification/modify-client-tariff-category-page.do" class="nav-link ">
                        <i class="fa fa-question"></i>
                        <span>Modify Tariff</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        
        <li class="nav-item" id="webSecurityLog">
            <a href="javascript:void(0);" class="nav-link nav-toggle"> 
                <i class="fa fa-lock"></i>
                <span>Security Log</span> 
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <li id="webSecurityLogSearch">
                    <a href="${context}WebSecurityLogSearch.do" class="nav-link">
                        <i class="fa fa-search"></i>
                        <span>Search</span>
                    </a>
                </li>
                <li id="webSecurityLogCharts">
                    <a href="${context}websecuritylog/webSecurityLogCharts.jsp" class="nav-link">
                       <i class="fa fa-bar-chart"></i>
                        <span>Charts</span>
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</li>
				