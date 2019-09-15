<%@page import="file.FileService"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@ page import="client.IdentityTypeConstants" %>
<%
	int entityTypeID=Integer.parseInt((String)request.getParameter("entityTypeID"));
	long entityID=Long.parseLong((String)request.getParameter("entityID"));
	FileService fileDAO=new FileService();
	ArrayList<FileDTO> fileList=fileDAO.getFileByEntityTypeAndEntity(entityTypeID,entityID );
%>
<%if(fileList.size()>0) {%>
<!-- The table listing the files available for upload/download -->
<h3>Files</h3>
<table role="presentation" class="table table-striped margin-top-10">
	<tbody class="files">
	
	<%for(FileDTO file: fileList){ %>
		<tr class="template-download fade in">
			<%if(file.getDocActualFileName().toLowerCase().endsWith(".jpg")|| file.getDocActualFileName().toLowerCase().endsWith(".png") || file.getDocActualFileName().toLowerCase().endsWith(".jpeg")){ %>
				
<!-- 				<td class="size" width="20%"> -->
<!-- 					<span class="preview"> -->
<%-- 						<img style="height: 50px;" src="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>"> --%>
<!-- 					</span> -->
<!-- 				</td> -->
				 <td class="name no-print" width="10%" >
              	 	<a style="text-decoration: underline; display: inline-block; text-align: center" data-fancybox-type="image" href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" class="no-print fancybox "><span>Preview</span></a>
            	</td>
			<%}else if(file.getDocActualFileName().toLowerCase().endsWith(".pdf1")){ %>
				<td></td>
			 	<td class="name no-print" width="10%" >
              	 	<a style="text-decoration: underline; display: inline-block; text-align: center" data-fancybox-type="iframe" href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" class="no-print fancyboxPdf "><span>Preview</span></a>
            	</td>
			<%}else{ %>
				<td class="size" width="20%"> No Preview </td>
				<td class="size" width="10%"> </td>
			<%} %>
<!-- 			 <td class="name no-print" width="30%"> -->
<%--               	<a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" ><span> <%=file.getDocActualFileName() %></span></a> --%>
<!--             </td> -->
            <td class="size" width="20%"><span title="Type"><%=IdentityTypeConstants.IdentityTypeName.get(Integer.parseInt(file.getDocTypeID())) == null ? "N/A" : IdentityTypeConstants.IdentityTypeName.get(Integer.parseInt(file.getDocTypeID())) %></span></td>
			<td class="size" width="20%"><span title="Size"><%=file.getDocSizeStr() %></span></td>
		 	<td class="name" width="10%">
           	 	<a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" ><span>Download</span></a>
           	</td>
    	</tr>
    	<%} %>
	</tbody>
</table>
<%}else{%>
	
<%}%>