<%@page import="file.FileService"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%
	int entityTypeID=Integer.parseInt((String)request.getParameter("entityTypeID"));
	long entityID=Long.parseLong((String)request.getParameter("entityID"));
	FileService fileService=new FileService();
	ArrayList<FileDTO> fileList=fileService.getFileByEntityTypeAndEntity(entityTypeID,entityID);
%>
<%if(fileList.size()>0) {%>
	<!-- The table listing the files available for upload/download -->
	<table role="presentation" class="table table-striped margin-top-10">
		<tbody class="files">
		<%for(FileDTO file: fileList){ %>
			<tr class="template-download fade in">
				<%if(file.getDocActualFileName().endsWith(".jpg")|| file.getDocActualFileName().endsWith(".png")){ %>
					<td class="size" width="40%">
						<span class="preview">
							<img style="height: 50px;" src="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>">
						</span>
					</td>
				<%}else{ %>
					<td class="size" width="40%"> No Preview </td>
				<%} %>
	            <td class="name" width="50%">
	               <%=file.getDocActualFileName() %>
	            </td>
	             <!--td class="size" width="40%"><span title="size"><%=FileTypeConstants.TYPE_ID_NAME.get(Integer.parseInt(file.getDocTypeID())) %></span></td-->
				 <td class="size" width="20%"><span title="size"><%=file.getDocSizeStr() %></span></td>
				 <td class="size" width="10%"><a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" >Download</a></td>
	    	</tr>
	    	<%} %>
		</tbody>
	</table>
<%}%>