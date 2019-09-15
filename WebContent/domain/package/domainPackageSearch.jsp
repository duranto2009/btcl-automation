<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.PACKAGE) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.PACKAGE_SEARCH )>=PermissionConstants.PERMISSION_READ ) )
    	
        hasPermission=true;
    
}

if( !hasPermission ){
	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}

%>

<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","configDomainMenu");
  request.setAttribute("subMenu2","packageDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Domain Package Search" /> 
	<jsp:param name="body" value="../domain/package/domainPackageSearchBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css" />
	
	<jsp:param name="css" value="assets/global/plugins/fancybox/source/jquery.fancybox.css" />
	<jsp:param name="css" value="assets/global/plugins/jquery-file-upload/blueimp-gallery/blueimp-gallery.min.css" />
	<jsp:param name="css" value="/assets/global/plugins/jquery-file-upload/css/jquery.fileupload.css" />
	<jsp:param name="css" value="assets/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" />
	<jsp:param name="css" value="assets/apps/css/inbox.css" />
	
	
	<jsp:param name="js" value="assets/global/plugins/fancybox/source/jquery.fancybox.pack.js" />
	<jsp:param name="js" value="assets/global/plugins/bootstrap-wysihtml5/wysihtml5-0.3.0.js" />
	<jsp:param name="js" value="assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.js" />
	
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/vendor/tmpl.min.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/vendor/load-image.min.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/vendor/canvas-to-blob.min.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/blueimp-gallery/jquery.blueimp-gallery.min.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-image.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-audio.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-video.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/jquery.fileupload-ui.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery-file-upload/js/cors/jquery.xdr-transport.js" />
</jsp:include> 