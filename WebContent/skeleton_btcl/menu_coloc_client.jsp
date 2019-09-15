<%@page import="common.EntityTypeConstant" %>
<%@page import="common.ModuleConstants" %>
<%@page import="coLocation.ColocationStateConstants" %>

<%if (!registeredColocation) {%>
<li class="nav-item">
    <a href="${context}Client/Add.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"
       class="nav-link ">
        <i class="fa fa-plus"></i>
        <span class="title">Co-Location</span>
    </a>
</li>
<%} else {%>
<%if (statusClientColocation == ColocationStateConstants.REQUEST_NEW_CLIENT.REGISTRATION_NOT_COMPLETED) {%>
<li class="nav-item"><a
        href="${context}GetClientForEdit.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>&edit"
        class="nav-link nav-toggle">
    <i class="fa fa-chain"></i> <span class="title">Co Location</span> <span class="fa fa-exclamation-circle"
                                                                             style="color: red"></span></a>
</li>
<%} else if (activationStatusColocation != EntityTypeConstant.STATUS_ACTIVE) {%>
<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-chain"></i> <span
        class="title">Co Location</span> <span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item"><a
                href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                class="nav-link nav-toggle">
            <i class="fa fa-user"></i> <span class="title">Profile</span> <span class="arrow"></span></a>
        </li>
        <%--<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"	class="nav-link "><i class="fa fa-search"></i>--%>
        <%--<span class="title">Search </span></a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
    </ul>
</li>
<% } else if (activationStatusColocation == EntityTypeConstant.STATUS_ACTIVE) {%>
<li class="nav-item " id="colocationMenu"><a href="javascript:;" class="nav-link nav-toggle"> <i
        class="fa fa-chain"></i> <span
        class="title">Co Location</span> <span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item"><a
                href="${context}GetClientForView.do?entityID=<%=menuLoginDTO.getAccountID()%>&moduleID=<%=ModuleConstants.Module_ID_COLOCATION %>"
                class="nav-link ">
            <i class="fa fa-user"></i> <span class="title">Profile</span> </a>
        </li>
        <%--<li class="nav-item  "><a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-share-alt"></i> <span class="title">Request</span> <span class="arrow"></span></a>--%>
        <%--<ul class="sub-menu">--%>
        <%--<li class="nav-item  "><a href="${context}SearchRequest.do?moduleID=<%=ModuleConstants.Module_ID_COLOCATION%>"	class="nav-link "><i class="fa fa-search"></i>--%>
        <%--<span class="title">Search </span></a>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>

        <li class="nav-item" id="colocationColocationSubmenu1">
            <a href="javascript:;" class="nav-link nav-toggle"> <i class="fa fa-server"></i>
                <span class="title">Manage CoLocation</span> <span class="arrow"></span>
            </a>
            <ul class="sub-menu">

                <li class="nav-item" id="new-connection">
                    <a href="${context}co-location/new-connection-application.do" class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">New Connection</span>
                    </a>
                </li>

                <li class="nav-item" id="revise-connection">
                    <a href="${context}co-location/revise-application.do" class="nav-link ">
                        <i class="fa fa-plus"></i><span class="title">Revise Connection</span>
                    </a>
                </li>

                <li class="nav-item" id="search-application">
                    <a href="${context}co-location/search.do" class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Search Application</span>
                    </a>
                </li>

                <li class="nav-item" id="search-connection">
                    <a href="${context}co-location/connection-search.do" class="nav-link ">
                        <i class="fa fa-search"></i><span class="title">Search Connection</span>
                    </a>
                </li>

            </ul>
        </li>


    </ul>
</li>
<%
        }
    }%>