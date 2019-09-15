
<li class="nav-item" id="global_search">
    <a href="javascript:" class="nav-link nav-toggle">
        <i class="fa fa-search"></i>
        <span>Advanced Search</span>
        <span class="arrow"></span>
    </a>
    <ul class="sub-menu">
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.OFFICIAL_LETTER)!=-1){%>
        <li class="nav-item" id="official_letter">
            <a href="${context}eeSearch/official-letter.do" class="nav-link">
                <i class="fa fa-book"></i>
                <span>Official Letter</span>
            </a>
        </li>
        <%}%>
        <%if(menuLoginDTO.getMenuPermission(PermissionConstants.SCHEDULER)!=-1){%>
        <li class="nav-item" id="scheduler">
            <a href="javascript:" class="nav-link nav-toggle">
                <i class="fa fa-clock-o"></i>
                <span>Scheduler</span>
                <span class="arrow"></span>
            </a>

            <ul class="sub-menu">
                <%if(menuLoginDTO.getMenuPermission(PermissionConstants.SCHEDULER_VIEW_ALL)!=-1){%>
                <li class="nav-item" id="scheduler-view-all">
                    <a href="${context}scheduler/all.do" class="nav-link">
                        <i class="fa fa-server"></i>
                        <span>View All</span>
                    </a>
                </li>
                <%}%>
            </ul>
        </li>
        <%}%>
    </ul>
</li>
