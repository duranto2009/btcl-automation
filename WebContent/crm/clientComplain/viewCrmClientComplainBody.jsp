<%@page import="common.CategoryConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.DatabaseConnectionFactory"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="crm.repository.*"%>
<%@page import="crm.*"%>
<%@page import="crm.service.*"%>
<%@page import="crm.action.*" %>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="util.TimeConverter" %>
<%
	
	String firstHeader = "Details of Complain";
	String secondHeader = "Sub Complains";
	String thirdHeader = "Response to Client";
    LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    CrmCommonPoolDTO crmCommonPoolDTO = (CrmCommonPoolDTO) request.getAttribute("CrmCommonPoolDTO");
    CrmComplainService crmComplainService = (CrmComplainService) ServiceDAOFactory.getService(CrmComplainService.class);
    DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
    int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
	int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
	int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
	
	databaseConnection.dbOpen();
    List<CrmComplainDTO> crmCrmComplainDTOList = crmComplainService.getCrmComplainListByPoolID(crmCommonPoolDTO.getID());
    databaseConnection.dbClose();
    DatabaseConnectionFactory.removeLastDatabaseConnection();
    List<CrmEmployeeDTO>crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOListByUserID(loginDTO.getUserID());
	
    String currentFeedbackOfNOC = crmCommonPoolDTO.getFeedbackOfNoc();
    int currentStatus = crmCommonPoolDTO.getStatus();
%>

<style>
	label, button{
 		display: inline-block !important; 
		min-height: 100%; /* for the latest browsers which support min-height */
    	height: auto !important; /* for newer IE versions */
    	height: 100%; /* the only height-related attribute that IE6 does not ignore  */
	}
</style>
<div class="row">
    <div class="col-md-12">
        <div class="portlet box portlet-btcl">

            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-question-circle-o"></i><%=firstHeader+ " Token " + crmCommonPoolDTO.getID() %>
                </div>
                <div class="tools">
                    <a class="collapse" href="javascript:;" data-original-title=""
                       title=""> </a>
                </div>
            </div>

            <div class="portlet-body">
            <%		
				if(crmCommonPoolDTO.isBlocked()){
			%>
					<div>
					<h4 style="color: red; text-align: center">
						Sorry, This Complain has been blocked.
					</h4>
					</div>
			<%
				}
			%>
            <h3>Information</h3>
                <div class="table-responsive">
                    <table class="table table-bordered table-striped table-hover" id='client-complain-table'>
                        <tbody>
                        
                        <tr>
                            <th scope="row">Client</th>
                            <td>
                            	<a target="_blank" href="${context }GetClientForView.do?moduleID=<%=crmCommonPoolDTO.getEntityTypeID()/100%>
										&entityID=<%=crmCommonPoolDTO.getClientID()%>">
                            		<%=AllClientRepository.getInstance().getClientByClientID(crmCommonPoolDTO.getClientID()) == null ? "" :
                            			AllClientRepository.getInstance().getClientByClientID(crmCommonPoolDTO.getClientID()).getName()  %>
                            	
                            	</a>
                            </td>
                            <th scope="row">Creator</th>
                            <td>
                            
                            <%
                            	if(crmCommonPoolDTO.getCreatorEmployeeID() == null){
                            %>
                            		N/A
                            <%		
                            	}else {
                            		
                            %>
                            		<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=crmCommonPoolDTO.getCreatorEmployeeID()%>">
                            			<%=CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmCommonPoolDTO.getCreatorEmployeeID()) == null ? "" :
                            				CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmCommonPoolDTO.getCreatorEmployeeID()).getName() %>
                            		</a>
                            <%		
                            	}
                            %>
                            </td>
                        </tr>
						<tr>
                            <th scope="row">Service Type</th>
                            <td><%= (crmCommonPoolDTO.getEntityTypeID() != null &&
                            			crmCommonPoolDTO.getEntityTypeID() != 0)? 
                            			EntityTypeConstant.mapOfModuleNameToMainEntityTypeIdForCrm.get(crmCommonPoolDTO.getEntityTypeID()) : "" %>
							</td>
							<th>Subject</th>
                        	
                       		 <td>
                            	<%= crmCommonPoolDTO.getSubject() != null ? crmCommonPoolDTO.getSubject()  : "--" %>
                            </td>
							
                      	</tr>
                       	<tr>
                            <th scope="row">Service Name</th>
                            <td><%= (crmCommonPoolDTO.getEntityID() != null &&
                            			crmCommonPoolDTO.getEntityID() != 0)? 
                            			crmCommonPoolDTO.getName() : "" %>
							</td>
							<th scope="row" colspan="2">Description</th>
                        </tr>
                        <tr>
                            <th scope="row">Status</th>
                            <td id="statusBox">
                            <%=CrmComplainDTO.mapComplainStatusStringToStatusID.get(crmCommonPoolDTO.getStatus()) %>
                           	</td>
                           	<td colspan="2" rowspan="6">
	                            <textarea id="userDescription" class="inbox-editor inbox-wysihtml5 form-control"  rows="7"  readonly="readonly" disabled="disabled">
	                            	<%=crmCommonPoolDTO.getClientComplain()  %>
	                            </textarea>
                            </td> 
							
                        </tr>
                        <tr>
							<th scope="row">Priority</th>
                            <td id="statusBox">
                            <%=CrmComplainDTO.mapComplainPriorityStringToPriorityID.get(crmCommonPoolDTO.getPriority()) %>
                            
                           </td>	
                            
                        </tr>
                        
						 <tr>
                            <th scope="row" style="color:green;">Feedback Message</th>
                            
                            <td id="feedbackBox">
                            <%if(crmCommonPoolDTO.getFeedbackOfNoc() != null){ %>
                            	<%=crmCommonPoolDTO.getFeedbackOfNoc() %>
                            <%}else {%>
									N/A                            	
                            <%} %>
                            </td>
                        </tr>
                        
                         
						 <tr>
                            <th scope="row" style="color:green;">Frequency</th>
                            <td>
                         	<%if(crmCommonPoolDTO.getFrequency() != null){ %>   
                            <%=crmCommonPoolDTO.getFrequency() %>
                        	<%} else {%>    
                            	N/A
                            <%} %>
                            </td>
                        </tr>
                        
                         <tr>
                            <th scope="row" style="color:green;">Time of Incident</th>
                            <td>
                         	<%if(crmCommonPoolDTO.getTimeOfIncident()!= null){ %>
						    
                            	<%=TimeConverter.getMeridiemTime(crmCommonPoolDTO.getTimeOfIncident())  %>
                        	<%} else {%>
                        		N/A    
                            <%} %>
                            </td>
                        </tr>
                        
                        <tr>
                            <th scope="row">Submission Time</th>
                            <td><%=TimeConverter.getMeridiemTime(crmCommonPoolDTO.getSubmissionTime()) %>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                
                <jsp:include page="../../common/fileListHelper.jsp" flush="true">
					<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.CRM%>" />
					<jsp:param name="entityID" value="<%=crmCommonPoolDTO.getID()%>" />
				</jsp:include>
				
				<%if (loginDTO.getAccountID() > 0) { %>
				<div class="row">
				    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				        <h3>Further Query</h3>
		                <div class="portlet-body form">
		                    <form class="form-horizontal" id="fileupload"
		                          action="<%=request.getContextPath()%>/CrmClientComplain/ReQueryClientCrmComplain.do"
		                          method="post">
		                        <div class="form-body">
		                            <input type="hidden" id="xconID" name="ID" value="<%=crmCommonPoolDTO.getID()%>"/>
									<div class="form-group">
										<label for="" class="col-sm-2 control-label">Missing Something !</label>
										<div class="col-sm-10">
											<div class="inbox-editor-open inbox-form-group">
												<textarea id="currentDescription"
													class="inbox-editor inbox-wysihtml5 form-control"
													name="clientComplain" rows="7" placeholder="Write your query here" required></textarea>
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="text-center" style="padding: 0px;">
											<span class="btn yellow  fileinput-button"> <i
												class="fa fa-upload"></i> <span> Add Files </span> <input
												class="jFile" type="file"
												name="<%=FileTypeConstants.CRM.CLIENT_COMPLAIN%>" multiple>
											</span>
										</div>
										<div class="col-md-8">
											<!-- The global file processing state -->
											<span class="fileupload-process"></span>
											<!-- The global progress state -->
											<div class="col-lg-12 fileupload-progress fade">
												<!-- The global progress bar -->
												<div class="progress progress-striped active"
													role="progressbar" aria-valuemin="0" aria-valuemax="100">
													<div class="progress-bar progress-bar-success"
														style="width: 0%;"></div>
												</div>
												<!-- The extended global progress state -->
												<div class="progress-extended">&nbsp;</div>
											</div>
										</div>
										<!-- The table listing the files available for upload/download -->
										<table role="presentation"
											class="table table-striped margin-top-10">
											<tbody class="files"></tbody>
										</table>
										<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
									</div>
									<div class="form-actions text-center">
		                                <button class="btn btn-submit-btcl" type="submit">Submit</button>
		                            </div>
		                        </div>
		                    </form>
		                </div>
				    </div>
				</div>
				<%} %>
				<%-- --%>
				<%if (loginDTO.isNOC()) { 
					if(crmCommonPoolDTO.getStatus() != CrmComplainDTO.COMPLETED){
				%>
				
				<div class="row">
				    <div class="col-lg-12 col-xs-12 col-sm-12 col-md-12">
				        <h3>Response To Client</h3>
			            <div class="table-responsive">
		                    <table class="table table-bordered table-striped table-hover" id='client-complain-table'>
		                        <tbody>
		                        	<tr>
		                        		<th scope="row">Change Status</th>
		                        		<td>
		                        			<div class="form-group">
												<select class="form-control pull-right" name="status" id="dropdown-status">
										            <option value="<%=CrmComplainDTO.ON_GOING%>">On going</option>
										            <option value="<%=CrmComplainDTO.COMPLETED%>">Complete</option>
										        </select>
											</div>
		                        		</td>
		                        	
		                        	</tr>
		                        	<tr>
		                        		<th scope="row">Change Feedback</th>
		                        		<td>
		                        			<div class="form-group">
												<textarea id="feedbackTextArea" 
												class="form-control border-radius" name="feedbackOfNoc" 
												><%=crmCommonPoolDTO.getFeedbackOfNoc() == null ? "" : crmCommonPoolDTO.getFeedbackOfNoc()%></textarea>
											</div>
		                        		</td>
		                        	</tr>
		                        </tbody>
		                     </table>
		                    
		                     
		                     
		                    <div class="form-actions text-center">
		                    	<button type="button" id="change-status-feedback-btn" class="btn btn-submit-btcl">Submit</button>
		                    </div> 
		                </div>
				    </div>
				</div>
				<%} %>
						
				<%if(crmCommonPoolDTO.isBlocked()==false) {%>						
				<div class="row">
				    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				        <h3>Sub Complains</h3>   
		                <div class="table-responsive">
							<table id="tableId"
								class="table table-bordered table-striped table-hover">					
									<tr>
										<th class="text-center">Forwarding ID</th>
										<th class="text-center">Complain Resolver Name</th>
										<th class="text-center">Current Status</th>
										<th class="text-center">Priority</th>
									</tr>				
									<tbody>
									<% for(CrmComplainDTO crmComplainDTO:  crmCrmComplainDTOList){
									
										CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());
										String resolverName = crmEmployeeDTO.getName();		
									%>		
										<tr>
											<td class="text-center"><a href="../../crm/complain/viewCrmComplain.jsp?complainID=<%=crmComplainDTO.getID()%>">Sub-Tok<%=crmComplainDTO.getID() %></a></td>
											<td class="text-center">
											<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=crmEmployeeDTO.getCrmEmployeeID() %>">
												<%=resolverName %>
											
											</a> 
											</td>
											
											<td class="text-center"><%= CrmComplainDTO.mapComplainStatusStringToStatusID.get(crmComplainDTO.getCurrentStatus())  %></td>
											<td class="text-center"><%=CrmComplainDTO.mapComplainPriorityStringToPriorityID.get(crmComplainDTO.getPriority()) %></td>
										
										</tr>
									<%} %>
								</tbody>
							</table>
							<div class="form-actions text-center">
								<button class="btn btn-submit-btcl" id="create-sub-complain-btn" type="button">Create Sub Complain</button>
							</div>
						</div>
				    </div>
				</div>
				<%} %>
				<%-- --%>
				<div class="row" id="show-complain-form" style="display: none">
				    <div class="col-xs-12 col-sm-12 col-lg-12 col-md-12">
				        <div class="portlet box portlet-btcl">
				            <div class="portlet-body">
				                <div class="portlet-body form">
				                    <input type="hidden" id="currentBlockNum" value="0"/>
				                    <input type="hidden" id="commonPoolID" value="<%=crmCommonPoolDTO.getID()%>"/>
				                    <form class="form-horizontal" id="crmComplainAddForm"
				                          method="post">
				                        <div class="form-body extended">
				                            <div class='todo-tasklist-item todo-tasklist-item-border-green'>
				                                <input type='hidden' name='crmComplainDTOs[0].commonPoolID'
				                                       value='<%=crmCommonPoolDTO.getID()%>'/>
				                                       
				                                
												<div class="row">	
													<div class="col-md-4">
														<div class="row">
															<div class="form-group">
																<label for="lnName" class="control-label col-md-5">Assign as<span class="required" aria-required="true">*</span></label>
																<div class="col-md-7">
																	<select name="crmEmployeeID" class="form-control border-radius pull-right" required>
																		<%
																			for(CrmEmployeeDTO crmEmployeeDTO: crmEmployeeDTOs){
																				if(CrmDesignationService.isNOC(crmEmployeeDTO)){
																					CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(
																							crmEmployeeDTO.getInventoryCatagoryTypeID());
																					String designationName = crmDesignationDTO.getName();
																					CrmDesignationDTO crmRootDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
																					String departmentName = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(crmRootDesignationDTO.getDesignationID()).getDepartmentName();
																		%>
																			<option value="<%=crmEmployeeDTO.getCrmEmployeeID()%>"><%=crmEmployeeDTO.getName() %>[<%=designationName %>][<%=departmentName %>]</option>			
																		<%
																				}
																			}
																		%>
																	</select>
																</div>	
															</div>
														</div>
														
														<div class="row">
															<div class="form-group up-down-path">
																<label class="col-md-5 control-label" for="lnName">District</label>
																<div class="col-md-7">
																	<input class="fe-hide form-control category-item district"
																		name="feDistrictStr" id="districtName" placeholder="Type to select..." value=""
																		type="text"> 
																	<input name="districtID" id="districtID" value="" type="hidden">
																</div>
															</div>
														</div>
														<div class="row">
															<div class="form-group up-down-path">
																<label for="lnName" class="col-md-5 control-label">Upazila</label>
																<div class="col-md-7">
																	<input class="fe-hide form-control category-item upazila"
																		name="feUpazilaStr" id="upazilaName" placeholder="Type to select..."
																		value="" type="text"> 
																		<input id="upazilaID" name="upazilaID" value=""
																		type="hidden">
																</div>
															</div>
														</div>
														<div class="row">
															<div class="form-group up-down-path">
																<label for="lnName" class="col-md-5 control-label">Union</label>
																<div class="col-md-7">
																	<input class="fe-hide form-control category-item union"
																		name="feUnionStr" id="unionName" placeholder="Type to select..."
																		value="" type="text"> 
																	<input name="unionID" id="unionID" value="" type="hidden">
																</div>
															</div>
														</div>
														<div class="row">
															<div class="form-group up-down-path">
																<label for="lnName" class="col-md-5 control-label">Department<span class="required" aria-required="true">*</span></label>
																<div class="col-md-7">
																	<input class="fe-hide form-control"  name="feDeptStr" 
																	placeholder="Type to select..."
																		value="" type="text" id="departmentName"> 
																	<input id="departmentID" name="deptID" value="-1" type="hidden">
																</div>
															</div>
														</div>
														<div class="row">
															<div class="form-group">
							                                    <label for="" class="col-md-5 control-label">Assign to<span class="required" aria-required="true">*</span></label>
							                                    <div class="col-md-7">
							                                        <input type="text" id="assignToAutoComplete" class="form-control auto-complete complainResolverName_0"
							                                               placeholder="Type to select..." name="crmComplainDTOs[0].complainResolverName">
							                                        <input
							                                                type="hidden" value="-1" class="form-control complainResolverID_0"
							                                                name="crmComplainDTOs[0].complainResolverID" id="resolverID">
							                                    </div>
							                                </div>
							                             </div>
							                             <div class="row">
							                             	<div class="form-group">
							                                    <label class="col-md-5 control-label">Priority</label>
							                                    <div class="col-md-7">
							                                        <select class="form-control pull-right" name="crmComplainDTOs[0].priority">
							                                            <option value="<%=CrmComplainDTO.NORMAL%>">Normal</option>
							                                            <option value="<%=CrmComplainDTO.LOW%>">Low</option>
							                                            <option value="<%=CrmComplainDTO.HIGH%>">High</option>
							                                        </select>
							                                    </div>
							                                </div>
							                             </div>
													</div>
													<div class="col-md-1"></div>
													<div class="col-md-7">
														<div class="form-group">
															<div class="row">
																<label class="col-md-2 control-label">Description</label>
															</div>
															<div class="row">
<!-- 																<label for="" class="col-md-4 control-label">Description</label> -->
																<div class="col-md-11">
							                                    	<div class="inbox-editor-open inbox-form-group">
																		<textarea class="inbox-editor inbox-wysihtml5 form-control currentDescription"  rows="7"
							                                                  name="crmComplainDTOs[0].currentDescription">
							                                                  <%=crmCommonPoolDTO.getClientComplain() %>
							                                         	</textarea>
							                                    	</div>
					                                    		</div>
					                                    		<div class="col-md-1">
					                                    		</div>
															</div>
															
														</div>
													</div>
												</div>
				                            </div>
				                        </div>
				                        <div class="form-actions text-center">
				                            <button class="btn btn-reset-btcl" type="reset">Reset</button>
				                            <button class="btn btn-submit-btcl" id="submit-btn-subcomplain-form" type="submit">Submit</button>
				                            <button class="btn btn-cancel-btcl" id="cancel-btn-subcomplain-form" type="button">Cancel</button>
				                        </div>
				                    </form>
				                </div>   
				            </div>
				        </div>
				    </div>
				</div>
				<%} %>
			</div>
		</div>
    </div>
</div>

 
<link href="${context}assets/apps/css/todo-2.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
    $(document).ready(function () {
    	function checkForm(){
    		var flag = false;
    		if($('#departmentID').val()< 0){
    			$('#departmentID').closest('.form-group').addClass('has-error');
    			flag = true;
    		}
    		if($('#resolverID').val() < 0){
    			$('#resolverID').closest('.form-group').addClass('has-error');
    			flag = true;
    		}
    		return flag;
    	}
    	$('#submit-btn-subcomplain-form').click(function(){
    		if(checkForm()){
    			return false;
    		}
    		var url = context + "CrmClientComplain/CreateCrmComplain.do";
    		var param = $('#crmComplainAddForm').serialize();
    		callAjax(url, param, function(data){
    			if(data.responseCode==1){
    				toastr.success(data.msg);
    				setTimeout(function(){
    					location.href = context + 'CrmComplainSearch/Complains.do';
    				}, 1500);
    			}else {
    				toastr.error(data.msg);
    			}
    		}, "POST");
    		return false;
    	});
    	$('#change-status-feedback-btn').click(function(){
    		var url = "${context}CrmClientComplain/sendResponseToClient.do";
    		var formData = {};
    		formData.ID = "<%=crmCommonPoolDTO.getID()%>";
    		formData.status = $('#dropdown-status :selected').val();
    		formData.feedback = $('#feedbackTextArea').val();
    		
    		LOG(formData);
    		
    		callAjax(url, formData, function(data){
    			if(data.responseCode == 1){
    				toastr.success(data.msg);
    				setTimeout(function(){
    					location.reload();
    				},1000);
    			}else {
    				toastr.error(data.msg);
    			}
    		}, "POST");
    		return false;
    	})
    	function complainAddCallback(data) {
            if (data['responseCode'] == 1) {
                toastr.success(data['msg']);
                setTimeout(location.reload(true), 2500);
            } else {
                toastr.error(data['msg']);
            }
        }
        function processList(url, formData, callback, type) {
            $.ajax({
                type: typeof type != 'undefined' ? type : "POST",
                url: url,
                data: formData,
                dataType: 'JSON',
                success: function (data) {
                    callback(data.payload);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
                        + ", Message: " + errorThrown);
                },
                failure: function (response) {
                    toastr.error(response);
                }
            });
        }
        var initWysihtml = function (currentNumber) {
            $('.inbox-wysihtml5'+currentNumber).wysihtml5({
                "stylesheets": [systemConfig.getBaseUrl()+"assets/global/plugins/bootstrap-wysihtml5/wysiwyg-color.css"]
            });
        }
        var currentResolverName = "complainResolverName_" + $("#currentBlockNum").val();
        var currentResolverID = 'complainResolverID_' + $("#currentBlockNum").val();
        var commonPoolID = $('#commonPoolID').val();
        
        
        $(document).on('click', '.btn-add-btcl', function (event) {
            event.preventDefault();
            var currentNumber = $("#currentBlockNum").val();
            $("#currentBlockNum").val(++currentNumber);
            var newCrmComplainForm = $("<div class='todo-tasklist-item todo-tasklist-item-border-green'><input type='hidden' name='crmComplainDTOs[" + currentNumber + "].commonPoolID' value='" + commonPoolID + "'/>" +
                "<div class='form-group'><label class='col-sm-2 control-label'>Description</label>" +
                "<div class='col-sm-10'><div class='inbox-editor-open inbox-form-group'><textarea class='finbox-editor inbox-wysihtml5"+currentNumber+" form-control currentDescription'  rows='7' name='crmComplainDTOs[" + currentNumber + "].currentDescription'>" + $("#userDescription").text() + "</textarea></div></div></div>" +
                "<div class='form-group'>" +
                "<label class='col-sm-2 control-label'>Priority</label>" +
                "<div class='col-sm-10'>" +
                "<select class='form-control pull-right' name='crmComplainDTOs[" + currentNumber + "].priority'>" +
                "<option value='"+<%=CrmComplainDTO.NORMAL%>+
	            "'>Normal</option>" +
	            "<option value='"+<%=CrmComplainDTO.LOW%>+
	            "'>Low</option>" +
	            "<option value='"+<%=CrmComplainDTO.HIGH%>+
	            "'>High</option>" +
	            "</select></div></div>" +
	            "<div class='form-group up-down-path'>"+
				"<label class='col-sm-2 control-label' for='lnName'>District</label>"+
				"<div class='col-sm-10'>"+
				"<input class='fe-hide form-control category-item district' name='feDistrictStr' placeholder='Type to select...' value='' type='text'><input type='hidden'"+
					"value='"+<%=districtCategoryId%>+"' name='districtCategoryId' class='category-id'><input name='districtID'"+
					 "value='' type='hidden'>"+
					"</div>"+
				"</div>"+
				"<div class='form-group up-down-path'>"+
					"<label for='lnName' class='col-sm-2 control-label'>Upazila</label>"+
					"<div class='col-sm-10'>"+
						"<input class='fe-hide form-control category-item upazila' name='feUpazilaStr' placeholder='Type to select upazila...' value='' type='text'><input type='hidden'"
							+"value='"+<%=upazilaCategoryId%>+"' name='upazilaCategoryId' class='category-id'><input name='upazilaID' value='' type='hidden'>"+
					"</div>"+
				"</div>"+
				"<div class='form-group up-down-path'>"+
					"<label for='lnName' class='col-sm-2 control-label'>Union</label>"+
					"<div class='col-sm-10'>"+
						"<input class='fe-hide form-control category-item union' name='feUnionStr' placeholder='Type to select union...' value='' type='text'>"+ "<input type='hidden'"
							+"value='"+<%=unionCategoryId%>+"' name='upazilaCategoryId' class='category-id'><input name='unionID' value='' type='hidden'>"+
					"</div>"+
				"</div>"+
	            "<div class='form-group'><label for='' class='col-sm-2 control-label'>Complain Resolver</label>" +
	            "<div class='col-sm-10'><input type='text' class='form-control auto-complete complainResolverName_" + currentNumber + " ui-autocomplete-input' autocomplete='off' placeholder='Type to add' name='complainResolverName[" + currentNumber + "]'> " +
	            "<input type='hidden' class='form-control complainResolverID_" + $("#currentBlockNum").val() + "' name='crmComplainDTOs[" + currentNumber + "].complainResolverID'></div></div>" +
	            "<div class='form-group'>" +
	            "<label class='col-sm-2 control-label'>Tag</label>" +
	            "<div class='col-sm-10'>" +
	            "<input class='form-control' placeholder='' name='crmComplainDTOs[" + currentNumber + "].tag' type='text'>" +
	            "</div></div></div>"
            );

            $(".extended").append(newCrmComplainForm);

            bindAutoComplete('complainResolverName_' + currentNumber);
            initWysihtml(currentNumber);
        });

        $("#nocFeedbackForm").on("submit", function (event) {
            event.preventDefault();
            var url = $(this).attr('action');
            var param = {};

            param['ID'] = event.currentTarget[0].value;
            param['status'] = event.currentTarget[1].value;
            param['feedbackOfNoc'] = event.currentTarget[2].value;

            callAjax(url, param, complainAddCallback, "POST");
        });
        
        $("#fileupload").on("submit", function (event) {
            event.preventDefault();		
            $(".jFile").attr('disabled', true)
            var url = $(this).attr('action');
            var param = $("#fileupload").serialize();

            param['ID'] = $('#xconID').val();
            param['clientComplain'] = $("#currentDescription").val();
            var documents = [];
            $('input[name=documents]').each(function() {
                documents.push($(this).val());
            })
            param['documents'] = documents;
            callAjax(url, param, complainAddCallback, "POST");
        });
        
        var districtNameInput = $("#districtName");
    	var upazilaNameInput = $("#upazilaName");
    	var unionNameInput = $("#unionName");
    	var departmentNameInput = $("#departmentName");
    	var districtID = $("#districtID");
    	var upazilaID = $("#upazilaID");
    	var unionID = $("#unionID");
    	var departmentID = $("#departmentID");
		districtNameInput.change(function(){
			if(	$.trim(	districtNameInput.val()	).length ==0	){
				districtOnChange();
			}
		});
		upazilaNameInput.change(function(){
			if(	$.trim(	upazilaNameInput.val()	).length ==0	){
				upazilaOnChange();
			}
		});
    	unionNameInput.change(function(){
			if(	$.trim(	unionNameInput.val()	).length ==0	){
				unionOnChange();
			}
		});
    	departmentNameInput.change(function(){
			if(	$.trim(	departmentNameInput.val()	).length ==0	){
				departmentOnChange();
			}
		});
    	function districtOnChange(){
    		districtID.val('');
    		upazilaNameInput.val('');
    		upazilaOnChange();
    	};
    	
    	function upazilaOnChange(){
    		upazilaID.val('');
    		unionNameInput.val('');
    		unionOnChange();
    	}
    	function unionOnChange(){
    		unionID.val('');
    		departmentNameInput.val('');
    		departmentOnChange();
    	}
    	
    	function departmentOnChange(){
    		departmentID.val('');
    	}
    	districtNameInput.autocomplete({
    		source : function(request, response) {
    			districtOnChange();
    			var url = '../../CrmDesignation/department/autoComplete.do';
    			var formData={};
    			formData.name = request.term;
    			formData.categoryType = 1;
    			processList(url,formData,response,"POST");
    		},
    		minLength : 1,
    		select : function(e, ui) {
    			districtNameInput.val(ui.item.name);
    			districtID.val(ui.item.ID);
    			return false;
    		},
    	}).autocomplete("instance")._renderItem = function(ul, data) {
    		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
    	};
    	
    	upazilaNameInput.autocomplete({
    		source : function(request, response) {
    			upazilaOnChange();
    			var url = '../../CrmDesignation/department/autoComplete.do';
    			var formData={};
    			formData.name = request.term;
    			formData.categoryType=2;
    			formData.parentID = districtID.val();
    			processList(url,formData,response,"POST");
    		},
    		minLength : 1,
    		select : function(e, ui) {
    			upazilaNameInput.val(ui.item.name);
    			upazilaID.val(ui.item.ID);
    			return false;
    		},
    	}).autocomplete("instance")._renderItem = function(ul, data) {
    		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
    	};
    	
    	unionNameInput.autocomplete({
    		source : function(request, response) {
    			unionOnChange();
    			var url = '../../CrmDesignation/department/autoComplete.do';
    			var formData={};
    			formData.name = request.term;
    			formData.categoryType=3;
    			formData.parentID = upazilaID.val();
    			processList(url,formData,response,"POST");
    		},
    		minLength : 1,
    		select : function(e, ui) {
    			unionNameInput.val(ui.item.name);
    			unionID.val(ui.item.ID);
    			return false;
    		},
    	}).autocomplete("instance")._renderItem = function(ul, data) {
    		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
    	};

    	
    	
    	departmentNameInput.autocomplete({
    		source : function(request, response) {
    			var url = context+"CrmDesignation/department/autocompleteDept.do";
    			var formData={};
    			departmentOnChange();
    			formData.deptName = request.term;
    			formData.districtID = districtID.val();
    			formData.upazilaID = upazilaID.val();
    			formData.unionID = unionID.val();
    			
    			processList(url,formData,response,"POST");
    		},
    		minLength : 1,
    		select : function(e, ui) {
    			departmentNameInput.val(ui.item.departmentName);
    			departmentID.val(ui.item.ID);
    			return false;
    		},
    	}).autocomplete("instance")._renderItem = function(ul, data) {
    		return $("<li>").append("<a>" + data.departmentName+ "</a>").appendTo(ul);
    	};
    	
    	$("#create-sub-complain-btn").click(function(){
    		$("#show-complain-form").show();
    		window.location.hash = "show-complain-form";
    		return false;
    	});
    	$('#cancel-btn-subcomplain-form').click(function(){
    		$('#show-complain-form').hide();
    		window.history.back(1);
    	});
    	
    	
    	$("#assignToAutoComplete").autocomplete({
    		source : function(request, response) {
    			var url = context+"CrmEmployee/GetEmployeesByPartialName.do";
    			var formData={};
    			formData.assignee = request.term;
    			formData.deptID = departmentID.val();
    			processList(url,formData,response,"GET");
    		},
    		minLength : 1,
    		select : function(e, ui) {
    			$('#assignToAutoComplete').val(ui.item.employeeName);
    			$('input[name="crmComplainDTOs[0].complainResolverID"]').val(ui.item.employeeID);
    			return false;
    		},
    	}).autocomplete("instance")._renderItem = function(ul, data) {
    		return $("<li>").append("<a>" + data.employeeName+ "</a>").appendTo(ul);
    	};
    });
   
</script>
