<%@page import="util.TimeConverter"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="login.ColumnPermissionConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="file.FileService"%>
<%@page import="common.ModuleConstants"%>
<%@page import="util.SOP"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainService"%>
<%
DomainService domainService = new DomainService();
String context = "../../.." + request.getContextPath() + "/";
Logger logger = Logger.getLogger(this.getClass());
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
try{	
	int id = Integer.parseInt(request.getParameter("entityID"));
	String actionName = "../../domain/domainSearch/domainEdit.jsp?entityID=" + id;
	String back = "../../ViewDomain.do?entityID=" + id;
	
	DomainDTO domainDTO = domainService.getDomainByID(id);
	String failureMsg = (String)request.getAttribute("failureMsg");
%>
<%if(failureMsg != null){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong ><%=failureMsg %></strong>
</div><%}%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">Domain Edit</div>
	</div>
	<!-- Main Body -->
	<div class="portlet-body" style="min-height: 450px;">
		<div class="row">
			<!-- Domain Edit -->
			<div class="col-md-12">
				<div class="row domainBuy">
			<!-- Domain Search Result and Buy -->
			<div class="col-md-12" >
				<!-- Domain Buy Procedure -->
				<div  class="row">
				<div class="col-md-12" >
					<div class="portlet box portlet-btcl">
						<!-- Domain Buy Form -->
						<form method="post" action="../../DomainAction.do?mode=editDomain&requestedURL=<%=request.getAttribute("requestedURL")%>&columnID=<%=ColumnPermissionConstants.DOMAIN.EDIT_ACTIVE_DOMAIN%>" id="fileupload" >
							<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
							<input type="hidden" name="ID" value="<%=domainDTO.getID() %>">
							<input type="hidden" name="entityID" value="<%=domainDTO.getID() %>">
							<input type="hidden" name="domainAddress" value="<%=domainDTO.getDomainAddress() %>">
							
							<div class="row"> 
								<div class="col-md-12">
								<h4>General Information</h4>
									<div class="table">
										<table class="table table-striped table-bordered table-checkable dataTable no-footer">
											<tbody>
												<tr>
													<td colspan="1" width="25%"><label class="control-label">Domain Address</label></td>
													<td colspan="1" width="75%">
														 <%=domainDTO.getDomainAddress() %>
														 <input id="domainName" type="hidden" value="<%=domainDTO.getDomainAddress()%>" />
													</td>
												</tr>
												<tr>
													<td colspan="1" width="25%"><label class="control-label">Client Name</label></td>
													<td colspan="1" width="75%">
															<%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %>
										 					<input id="clientId" type="hidden" class="form-control" name="domainClientID" value="<%=domainDTO.getDomainClientID() %>">

													</td>
												</tr>
<%-- 												<tr>
													<div class="form-group">
													<td colspan="2">
														<!-- File upload -->
														<div class="row">
															<div class="col-md-12" style="padding-left:30px !important;">
															 	<div class="col-md-3" style="padding: 0px;" id="nid">
																	<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
																	<span class="btn btn-warning-btcl  fileinput-button">
																		<i class="fa fa-upload"></i>
																		<span> Document </span> 
																		<input class="jFile" id="doc" type="file" name="<%=FileTypeConstants.DOMAIN_BUY.DOMAIN_BUY_DOC %>" >
																	</span>
																</div>
										
																<div class="col-md-9">
																	 <!-- The global file processing state -->
																	 <span class="fileupload-process"></span>
															   		  <!-- The global progress state -->
															          <div class="col-lg-12 fileupload-progress fade">
															              <!-- The global progress bar -->
															              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
															                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
															              </div>
															              <!-- The extended global progress state -->
															              <div class="progress-extended">&nbsp;</div>
															          </div>
																</div>
																
																<%
																
																FileService fileDAO=new FileService();
																ArrayList<FileDTO> fileList=fileDAO.getFileByEntity(id );
																SOP.print(fileList.toString());
																
																%>
																<!-- The table listing the files available for upload/download -->
																<table role="presentation" class="table table-striped margin-top-10">
																	<tbody class="files">
																	
																	<%for(FileDTO file: fileList){ %>
																		<tr class="template-download fade in">
																			<%if(file.getDocActualFileName().endsWith(".jpg")|| file.getDocActualFileName().endsWith(".png")){ %>
																				<td>
																					<span class="preview">
																						<img style="height: 50px;" src="<%=request.getContextPath()+"/"+FileTypeConstants.FINAL_UPLOAD_DIR +file.getDocLocalFileName() %>">
																					</span>
																				</td>
																			<%}else{ %>
																				<td> No Preview </td>
																			<%} %>
																            <td class="name" width="30%">
																                <a href="<%=request.getContextPath()+"/"+FileTypeConstants.FINAL_UPLOAD_DIR +file.getDocLocalFileName() %>" 
																                title="<%=file.getDocActualFileName() %>" data-gallery="" download="<%=file.getDocActualFileName() %>"><%=file.getDocActualFileName() %></a>
																            </td>
																			<td class="size" width="40%"><span title="size"><%=FileTypeConstants.TYPE_ID_NAME.get(Integer.parseInt(file.getDocTypeID())) %></span></td>
																            <td class="size" width="20%"><span title="size"><%=file.getDocSizeStr() %></span></td>
																            <td colspan="2"></td>
																	         <td width="10%" align="right">
																	            <button type="button" title="cancel" class="btn default btn-sm delete" data-type="GET" data-url="/BTCL_Automation/JqueryFileUpload?delfile=<%=file.getDocLocalFileName() %>">
																	                <i class="fa fa-times"></i>
																	            </button>
																	            <input name="documents" type="hidden" value="<%=file.getDocLocalFileName() %>" >
																	        </td>
																    	</tr>
																    	<%} %>
																	</tbody>
																</table>
															</div>
													 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
														</div>
														<!-- /File upload -->
													</td>
													</div>
												</tr> --%>			
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!-- /Domain Buy : General Information -->
							<!-- Domain Buy : Server Information -->
							<div class="row"> 					
								<div class="col-md-12">
									<h4>Server Information</h4>
									<div class="portlet box" style="border: 1px solid #e6e9ec !important; padding: 15px !important;">
										<div class="row"> 					
											 <div class="col-md-7 primary-dns-name">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Primary Domain Name Server(DNS)<span class="required" aria-required="true"> * </span></label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="primaryDNS" value="<%=domainDTO.getPrimaryDNS() %>"> 
		                                       		</div>
		                                   		</div>
		                                   	</div>
		                                   	<div class="col-md-5 primary-dns-ip <%if(StringUtils.isBlank(domainDTO.getPrimaryDnsIP())){%> hidden <%}%>">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Primary IP</label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="primaryDnsIP" value="<%=domainDTO.getPrimaryDnsIP() %>"> 
		                                       		</div>
		                                   		</div>
		                                   	</div>
										</div>
										<br>
										<div class="row"> 					
											 <div class="col-md-7 secondary-dns-name">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Secondary  Domain Name Server(DNS)<span class="required" aria-required="true"> * </span></label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="secondaryDNS" value="<%=domainDTO.getSecondaryDNS() %>">  
		                                       		</div>
		                                   		</div>
		                                   	</div>
		                                   	<div class="col-md-5 secondary-dns-ip <%if(StringUtils.isBlank(domainDTO.getSecondaryDnsIP())){%> hidden <%}%>">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Secondary IP</label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="secondaryDnsIP" value="<%=domainDTO.getSecondaryDnsIP()%>"> 
		                                       		</div>
		                                   		</div>
		                                   	</div>
										</div>
										<br>
										<div class="row"> 					
											 <div class="col-md-7 tertiary-dns-name">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Tertiary Domain Name Server(DNS)</label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="tertiaryDNS" value="<%=domainDTO.getTertiaryDNS() %>">  
		                                       		</div>
		                                   		</div>
		                                   	</div>
		                                   	<div class="col-md-5 tertiary-dns-ip <%if(StringUtils.isBlank(domainDTO.getTertiaryDnsIP())){%> hidden <%}%>">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4">Tertiary IP</label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control" placeholder="" name="tertiaryDnsIP" value="<%=domainDTO.getTertiaryDnsIP() %>"> 
		                                       		</div>
		                                   		</div>
		                                   	</div>
										</div>
									</div>  
								</div>
							</div>
							<!-- /Domain Buy : Server Information -->	
							<% if (loginDTO.getIsAdmin() && loginDTO.getColumnPermission(ColumnPermissionConstants.DOMAIN.UPDATE_DOMAIN)){
							%>		
							
							<!-- Domain Buy : Date Information -->
							
							<div class="row"> 					
								<div class="col-md-12">
									<h4>Date Information</h4>
									<div class="portlet box" style="border: 1px solid #e6e9ec !important; padding: 15px !important;">
										<div class="row"> 		
											<div class="col-md-6">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4"> Activation Date<span class="required" aria-required="true"> * </span></label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control datepicker" placeholder="" name="activationDate" value="<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(domainDTO.getActivationDate(), "dd/MM/yyyy") %>"> 
		                                       			
		                                       		</div>
		                                   		</div>
		                                   	</div>			
											 <div class="col-md-6">
		                                   		<div class="form-group">
		                                       		<label class="control-label col-md-4"> Expiration Date<span class="required" aria-required="true"> * </span></label>
		                                       		<div class="col-md-8">
		                                           		<input type="text" class="form-control datepicker" placeholder="" name="expiryDate" value="<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(domainDTO.getExpiryDate(), "dd/MM/yyyy")%>"> 
		                                       			
		                                       		</div>
		                                   		</div>
		                                   	</div>
		                                   	
										</div>
										
										
									</div>  
								</div>
							</div>
							<!-- /Domain Buy : Date Information -->
							<%	}
							%>		
							<div class="form-actions">
								<div class="row">
				                     <div class="col-md-offset-4 col-md-8">
				                    	 <a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Cancel</a>
				                         <button type="reset" class="btn btn-reset-btcl">Reset</button>
				                         <button type="submit" class="btn btn-submit-btcl">Submit</button>
				                     </div>
				                 </div>
            				</div>
						</form>
						<!-- /Domain Buy Form -->
					</div>
				</div>
				</div>
				<!-- /Domain Buy Procedure -->
			</div>
		</div>
			</div>
			<!-- /Domain Edit -->
		</div>
	</div>
	<!-- /Main Body -->
</div>
<script src="${context}domain/client/domainClient.js" type="text/javascript"></script>
<script src="${context}domain/domainQueryForBuy/domain-edit-validation.js" type="text/javascript"></script>
<%}catch(Exception ex){
}%>
