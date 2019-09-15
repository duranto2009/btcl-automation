<%@page import="client.IdentityTypeConstants"%>
<%@page import="file.FileDTO"%>
<%@page import="file.FileService"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%
	int entityTypeID=Integer.parseInt(request.getParameter("entityTypeID"));
	long entityID=Long.parseLong(request.getParameter("entityID"));
	FileService fileService=new FileService();
	ArrayList<FileDTO> fileList=fileService.getFileByEntityTypeAndEntity(entityTypeID,entityID );
%>
<table role="presentation" class="table table-striped margin-top-10">
	<tbody class="files">
	
	<%for(FileDTO file: fileList){ %>
		<tr class="template-download fade in">
			<%if(file.getDocActualFileName().endsWith(".jpg")|| file.getDocActualFileName().endsWith(".png")){ %>
<!-- 				<td class="size" width="20%"> -->
<!-- 					<span class="preview"> -->
<%-- 						<img style="height: 50px;" src="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>"> --%>
<!-- 					</span> -->
<!-- 				</td> -->
			  	<td class="name" width="30%">
               		<a style="text-decoration: underline; display: inline-block; text-align: center" data-fancybox-type="image" href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" class="no-print fancybox ">
               			<span><%=file.getDocActualFileName()%></span>
               		</a>
           	 	</td>
			<%}else{ %>
				<td class="size" width="20%"> No Preview </td>
				<td class="size" width="30%"><a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" ><span><%=file.getDocActualFileName()%></span></a> </td>
			<%} %>

			<td class="size" width="20%"><span title="Type">
				<%=IdentityTypeConstants.IdentityTypeName
						.get(Integer.parseInt(file.getDocTypeID())) == null ? "N/A" : IdentityTypeConstants.IdentityTypeName.get(Integer.parseInt(file.getDocTypeID())) %>
			</span></td>
            <td class="size" width="20%"><span title="size"><%=file.getDocSizeStr() %></span></td>
            <td colspan="2"></td>
	         <td width="10%" align="right">
	            <button type="button" title="cancel" class="btn default btn-sm delete" data-type="GET" data-url="<%=request.getContextPath() %>/JqueryFileUpload?delfile=<%=file.getDocLocalFileName() %>">
	                <i class="fa fa-times"></i>
	            </button>
	            <input name="documents" type="hidden" value="<%=file.getDocLocalFileName()%>" data-documentType="<%=file.getDocTypeID()%>" >
	        </td>
    	</tr>
    	<%} %>
	</tbody>
</table>