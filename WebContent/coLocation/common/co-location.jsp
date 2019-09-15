<%@ page import="coLocation.CoLocationConstants" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/4/18
  Time: 4:31 PM
--%>

<%
    //set up the menu
    request.setAttribute("menu", "colocationMenu");

    //request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));

    //which url to choose
    String url = (String)request.getAttribute(CoLocationConstants.CO_LOCATION_SESSION_URL);

    //choose content for the url
    String path = "../coLocation/application/co-location-application-new-connection-application-details.jsp";
    if(CoLocationConstants.NEW_CONNECTION_APPLICATION.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","new-connection");
        path = "../coLocation/application/co-location-application-new-connection-application.jsp";
    }
    else if(CoLocationConstants.NEW_CONNECTION_VIEW_DETAILS.equals(url)){
        path = "../coLocation/application/co-location-application-new-connection-application-details.jsp";
    }
    else if(CoLocationConstants.NEW_CONNECTION_VIEW_DETAILS_FULL.equals(url)){
        path = "../coLocation/application/co-location-application-new-connection-application-details-full.jsp";
    }
    else if(CoLocationConstants.UPGRADE_APPLICATION.equals(url)){
        path = "../coLocation/application/co-location-application-upgrade-application.jsp";
    }
    else if(CoLocationConstants.DOWNGRADE_APPLICATION.equals(url)){
        path = "../coLocation/application/co-location-application-downgrade-application.jsp";
    }
    else if(CoLocationConstants.REVISE_APPLICATION.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","revise-connection");
        path = "../coLocation/application/co-location-application-revise-application.jsp";
    }

    else if(CoLocationConstants.APPLICATION_SEARCH.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","search-application");
        path = "../coLocation/application/co-location-application-search.jsp";
    }
    else if(CoLocationConstants.NEW_CONNECTION_APPLICATION_DEMAND_NOTE.equals(url)){
        path = "../coLocation/demand-note/co-location-application-new-connection-demand-note.jsp";
    }
    else if(CoLocationConstants.CONNECTION_SEARCH.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","search-connection");

        path = "../coLocation/connection/co-location-connection-search.jsp";
    }
    else if(CoLocationConstants.INVENTORY_ADD.equals(url)){
        request.setAttribute("subMenu1","colocationInventorySubmenu1");
        request.setAttribute("subMenu2","colocationInventoryAdd");
        path = "../coLocation/inventory/co-location-inventory-add.jsp";
    }
    else if(CoLocationConstants.INVENTORY_SEARCH.equals(url)){
        request.setAttribute("subMenu1","colocationInventorySubmenu1");
        request.setAttribute("subMenu2","colocationInventorySearch");
        path = "../coLocation/inventory/co-location-inventory-search.jsp";
    }

    else if(CoLocationConstants.INVENTORY_COST_CONFIG_ADD.equals(url)){
        path = "../coLocation/inventory/co-location-inventory-cost-config.jsp";
    }
    else if(CoLocationConstants.INVENTORY_COST_CONFIG_SEARCH.equals(url)){
        path = "../coLocation/inventory/co-location-inventory-cost-config-search.jsp";
    }

    else if(CoLocationConstants.CONNECTION_DETAILS.equals(url)){
        path = "../coLocation/connection/co-location-connection-details.jsp";
    }
    else if(CoLocationConstants.PROBABLE_TD.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","coloc-td");
        path = "../coLocation/td-reconnect/co-location-probable-td.jsp";
    }
    else if(CoLocationConstants.RECONNECT.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","coloc-reconnect");
        path = "../coLocation/td-reconnect/co-location-reconnect.jsp";
    }
    else if(CoLocationConstants.CLOSE_CONNECTION.equals(url)){
        request.setAttribute("subMenu1","colocationColocationSubmenu1");
        request.setAttribute("subMenu2","coloc-close");

        path = "../coLocation/connection/co-location-connection-close.jsp";
    }

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Co Location" />
    <jsp:param name="body" value="<%=path%>" />
    <jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>