<%@ page import="common.ApplicationGroupType" %>
<%
	String renderJSP = "";
	String title = "";
	try{
		int type = Integer.parseInt(request.getParameter("appGroup"));

		ApplicationGroupType applicationGroupType = ApplicationGroupType.getAppGroupTypeByOrdinal(type);

		if(applicationGroupType == ApplicationGroupType.NIX_CONNECTION_APPLICATION){
			title = "NIX Connection";
			renderJSP = "/nix/demand-note/nix-dn-connection-body.jsp";
		}else if(applicationGroupType == ApplicationGroupType.NIX_CLIENT_APPLICATION){
			title = "NIX Revise Client";
			renderJSP = "/nix/demand-note/nix-dn-revise-client-body.jsp";
		}
	}catch (Exception e){
		e.printStackTrace();
	}

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="body" value="<%=renderJSP%>"/>
</jsp:include>