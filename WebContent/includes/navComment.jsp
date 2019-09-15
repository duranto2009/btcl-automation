<%@page import="comment.CommentDTO"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="file.FileDTO"%>
<%@page import="file.FileService"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="util.RecordNavigator"%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%
	LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	FileService fileService= new FileService();
	
	System.out.println("Inside nav.jsp" + request.getParameter("entityID"));
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	System.out.println("rn " + rn);
	
	String actionParameter="moduleID="+request.getParameter("moduleID");
	actionParameter+="&entityID="+request.getParameter("entityID");
	actionParameter+="&entityTypeID="+request.getParameter("entityTypeID");
	actionParameter+="&currentTab="+request.getParameter("currentTab");
	
	String action = "/" + url+"?"+actionParameter;
	String context = "../../.."  + request.getContextPath() + "/";
	String link = context + url + ".do?"+actionParameter;
	String searchFieldInfo[][] = rn.getSearchFieldInfo();
	String totalPage = "1";
	if(rn != null)
		totalPage = rn.getTotalPages() + "";

%>

<div class="row">
    <div class="col-md-12 col-sm-12">
        <div class="portlet light bordered portlet-fit bg-inverse ">
         	<div class="portlet-title" style="border-bottom: none !important">
         	 	<div class="row text-center">
					<html:form action="<%=action%>" method="POST" styleClass="form-inline">
						<nav aria-label="Page navigation" >
						  <ul class="pagination" style="margin: 0px;">
						    <li class="page-item">
						      <a class="page-link" href="<%=link%>&id=first" aria-label="First"  title="Left">
						        <i class="fa fa-angle-double-left" aria-hidden="true"></i>
						        <span class="sr-only">First</span>
						      </a>
						    </li>
						    <li class="page-item">
						      <a class="page-link" href="<%=link%>&id=previous" aria-label="Previous" title="Previous">
						         <i class="fa fa-angle-left" aria-hidden="true"></i>
						        <span class="sr-only">Previous</span>
						      </a>
						    </li>
						
						     <li class="page-item">
						      <a class="page-link" href="<%=link%>&id=next" aria-label="Next" title="Next">
						         <i class="fa fa-angle-right" aria-hidden="true"></i>
						        <span class="sr-only">Next</span>
						      </a>
						    </li>
						    <li class="page-item">
						      <a class="page-link" href="<%=link%>&id=last" aria-label="Last"  title="Last">
						        <i class="fa fa-angle-double-right" aria-hidden="true"></i>
						        <span class="sr-only">Last</span>
						      </a>
						    </li>
						    <li>
						    	&nbsp;&nbsp;<i class="hidden-xs">Page </i><input type="text" class="custom-form-control " name="pageno" value='<%=pageno%>' size="15"> <i class="hidden-xs"> of </i><%=rn.getTotalPages()%>
								<html:hidden property="go" value="yes" />
								<input type="submit" class="btn btn-circle btn-sm green-meadow btn-outline sbold uppercase" value="GO"/>
						    </li>
						  </ul>
						</nav>
					</html:form>
				</div>
   	 		</div>
   	 		<div class="portlet-body">
              <%
				ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_COMMENT);
				if (data != null) { %>
				  <div class="timeline  white-bg ">
                                 		<!-- TIMELINE ITEM -->
					<%
					int size = data.size();
					for (int i = 0; i < size; i++) {
						CommentDTO row = (CommentDTO) data.get(i);
						String clientName;
						String profilePictureURL = request.getContextPath() +"/ClientProfileInfo.do?type=showPicture";
						try {

							ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getMemberID());
							clientName = clientDTO.getLoginName();
							profilePictureURL+="&clientID="+clientDTO.getClientID();
						}catch(Exception e) {

							if(loginDTO.getAccountID() > 0) {
								clientName = "System";
							} else {
								UserDTO userDTO=UserRepository.getInstance().getUserDTOByUserID(row.getMemberID()*(-1));
								clientName =  userDTO.getUsername();
								if(userDTO.getFullName().length() > 0) {
									clientName += " ("+userDTO.getFullName()+")";
								}
								profilePictureURL+="&userID="+userDTO.getUsername();
							}

						}
						Date requestDate = new Date(row.getLastModificationTime());
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						String requestDateString = df.format(requestDate);
				%>
		        <div class="timeline-item">
		            <div class="timeline-badge">
		                <img src="<%=profilePictureURL %>" class="timeline-badge-userpic"> </div>
		            <div class="timeline-body">
		                <div class="timeline-body-arrow"> </div>
		                <div class="timeline-body-head">
		                    <div class="timeline-body-head-caption">
		                        <a class="timeline-body-title font-blue-madison" href="javascript:;"><%=clientName %></a>
		                        <span class="timeline-body-time font-grey-cascade">Commented at <%=requestDateString %></span>
		                    </div>
		                </div>
		                <div class="timeline-body-content">
		                	<h4><%=row.getHeading() %></h4>
		                    <span class="font-grey-cascade"><%=row.getDescription() %> </span>
		                    	<%
		                    		ArrayList<FileDTO>  fileList=fileService.getFileByEntityTypeAndEntity(EntityTypeConstant.COMMENT, row.getID());
		                    	%>
		                    	
		                     	<%if(fileList.size()>0) {%>
							<!-- The table listing the files available for upload/download -->
							<table role="presentation" class="table table-striped margin-top-10">
								<tbody class="files">
								<%for(FileDTO file: fileList){ %>
									<tr class="template-download fade in">
										<%if(file.getDocActualFileName().toLowerCase().endsWith(".jpg")|| file.getDocActualFileName().toLowerCase().endsWith(".png") || file.getDocActualFileName().toLowerCase().endsWith(".jpeg") || file.getDocActualFileName().toLowerCase().endsWith(".gif")){ %>
											<td class="size" width="20%">
												<span class="preview">
													<img style="height: 50px;" src="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>">
												</span>
											</td>
											 <td class="name no-print" width="10%" >
							              	 	<a style="text-decoration: underline; display: inline-block; text-align: center" data-fancybox-type="image" href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" class="no-print fancybox "><span>Preview</span></a>
							            	</td>
										<%}else{ %>
											<td class="size" width="20%"> No Preview </td>
											<td class="size" width="10%"> </td>
										<%} %>
										 <td class="name no-print" width="30%">
							              	<a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" ><span> <%=file.getDocActualFileName() %></span></a>
							            </td>
							            <td class="size" width="20%"><span title="Type"><%=FileTypeConstants.TYPE_ID_NAME.get(Integer.parseInt(file.getDocTypeID())) %></span></td>
										<td class="size" width="20%"><span title="Size"><%=file.getDocSizeStr() %></span></td>
									 	<td class="name" width="10%%">
							           	 	<a href="<%=request.getContextPath()%>/DownloadFile.do?documentID=<%=file.getDocID()%>" ><span>Download</span></a>
							           	</td>
							    	</tr>
							    	<%} %>
								</tbody>
							</table>
						<%}%>
                         </div>
                     </div>
                 </div>
                 <!-- END TIMELINE ITEM -->
                                 <%
				}
			%>
			</div>
			<%
			}else{
			%>
				<h2 class="text-center">No comment available to show</h2>
            <%} %>
            </div>
     	</div>
     </div>
</div>
