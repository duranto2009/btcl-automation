<%@page import="util.*"%>
<%@page import="java.util.*"%>
<%@page import="crm.service.*"%>
<%@page import="crm.*"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="crm.repository.*"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	long complainID = Long.parseLong(request.getParameter("complainID"));
	long userID = loginDTO.getUserID();
	CrmComplainService crmComplainService = (CrmComplainService) ServiceDAOFactory
			.getService(CrmComplainService.class);
	CrmCommonPoolService crmCommonPoolService = (CrmCommonPoolService) ServiceDAOFactory
			.getService(CrmCommonPoolService.class);
	CrmEmployeeService crmEmployeeService = (CrmEmployeeService) ServiceDAOFactory
			.getService(CrmEmployeeService.class);
	CrmComplainDTO crmComplainDTO = crmComplainService.getComplainDTOByComplainID(complainID, userID);
	CrmCommonPoolDTO crmCommonPoolDTO = crmCommonPoolService
			.getClientComplainByComplainID(crmComplainDTO.getCommonPoolID());
	CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
			.getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());

	List<CrmEmployeeDTO> rootCrmEmployeeDTOList = CrmAllEmployeeRepository.getInstance().getRootEmployeeList();

	DatabaseConnection databaseConnection = DatabaseConnectionFactory.getNewDatabaseConnection();
	databaseConnection.dbOpen();
	List<CrmComplainHistoryDTO> crmComplainHistoryDTOs = crmComplainService
			.getCrmComplainHistoryDTOListByRootComplainHistoryID(crmComplainDTO.getRootCompalinHistoryID());
	databaseConnection.dbClose();
	DatabaseConnectionFactory.removeLastDatabaseConnection();
%>
<div class="row">
	<div class="col-md-12">
		<div class="portlet box portlet-btcl">

			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-question-circle-o"></i> Details of Sub Complain
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;" data-original-title=""
						title=""> </a>
				</div>
			</div>

			<div class="portlet-body">
			<%		
				if(crmComplainDTO.isBlocked()){
			%>
					<div>
					<h4 style="color: red; text-align: center">
						Sorry, The Complain(<%=crmComplainDTO.getID() %>) has been blocked. Contact NOC.
					</h4>
					</div>
			<%
				}
			%>
					
				<h3>Information</h3>
				<div class="table-responsive">
					<table class="table table-bordered table-striped table-hover">
						<tbody>
							<tr>
								<th scope="row">Token</th>
								<td><%=crmCommonPoolDTO.getID() %></td>
								<th>Sub Token</th>
								<td><%=crmComplainDTO.getID() %></td>
							</tr>
							<tr>
								<th scope="row">Resolver</th>
								<td>
								<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=crmEmployeeDTO.getCrmEmployeeID() %>">
								<%=crmEmployeeDTO.getName()%></a>
								</td>
								
								<th>Subject</th>
								
								<td>
                            	<%= crmCommonPoolDTO.getSubject() != null ? crmCommonPoolDTO.getSubject()  : "--" %>
                            	</td>
								
							</tr>
							<tr>
								<th scope="row">Service Type</th>
								<td>
									<%=(crmCommonPoolDTO.getEntityTypeID() != null && crmCommonPoolDTO.getEntityTypeID() != 0)
									? EntityTypeConstant.mapOfModuleNameToMainEntityTypeIdForCrm.get(crmCommonPoolDTO.getEntityTypeID())
									: ""%>
								</td>
								<th scope="row" colspan="2">Description</th>
								
								
							</tr>
							<tr>
								<th scope="row">Service Name</th>
								<td>
									<%=(crmCommonPoolDTO.getEntityID() != null && crmCommonPoolDTO.getEntityID() != 0)
									? crmCommonPoolDTO.getName()
									: ""%>
								</td>
								<td rowspan="6" colspan="2">
									<textarea
										class="inbox-editor inbox-wysihtml5 form-control" rows="7"
										readonly="readonly" disabled="disabled">
                            			<%=crmComplainDTO.getCurrentDescription()%>
                            		</textarea>
                            	</td>
							</tr>
							
							<tr>
								<th scope="row" style="color: green;">Frequency</th>
								
								
								<td>
								<%
								if (crmCommonPoolDTO.getFrequency() != null) {
								%>
									<%=crmCommonPoolDTO.getFrequency()%>
								<%
									}else {
								%>
									N/A
								<%} %>
								</td>
							</tr>
							<tr>
							
								<th scope="row" style="color: green;">Time of Incident</th>
								<td>
								<%
								if (crmCommonPoolDTO.getTimeOfIncident() != null) {
								%>
									<%=TimeConverter.getMeridiemTime(crmCommonPoolDTO.getTimeOfIncident())%>
								<%}else { %>
									N/A
								<%} %>
								</td>
							</tr>
							

							<tr>
								<th scope="row">Priority</th>
								<td><%=CrmComplainDTO.mapComplainPriorityStringToPriorityID.get(crmComplainDTO.getPriority())%></td>
							</tr>
							<tr>
								<th scope="row">Status</th>
								<td><%=CrmComplainDTO.mapComplainStatusStringToStatusID.get(crmComplainDTO.getCurrentStatus())%></td>
							</tr>
							<tr>
								<th scope="row">Message</th>
								<td><%=crmComplainDTO.getLastResolverMsg() == null ? "N/A" : crmComplainDTO.getLastResolverMsg()%></td>
							</tr>
						</tbody>
					</table>
				</div>
				<jsp:include page="../../common/fileListHelper.jsp" flush="true">
					<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.CRM%>" />
					<jsp:param name="entityID" value="<%=crmCommonPoolDTO.getID()%>" />
				</jsp:include>
				<%
					if (CrmLogic.canTakeAction(loginDTO, crmComplainDTO) && crmComplainDTO.isBlocked() == false) {
				%>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<h3>Action</h3>
						<div class="table-responsive">
							<table class="table table-bordered table-hover table-striped">
								
								<input id="complainID" type="hidden" class="form-control" name="complainID" value="<%=complainID%>">
								<%
									if (CrmLogic.canSendFeedback(loginDTO, crmComplainDTO)) {
								%>
								<tr>
									<td>
										<label>
											<input type="radio" id="change-feedback" value="1" name="actionList">Change Feedback
										</label>
									</td>
								</tr>
								
								<%	}  %>
								<tr class="tr-hide" id="tr-change-feedback" style="display:none">
									<td>
										<form class="form-horizontal" id="crmComplainFeedbackForm"
											method="post">
											<div class="form-body">
												<div class="form-group">
													<label class="col-sm-3 control-label">Feedback</label>
													<div class="col-sm-6">
														<textarea class="form-control border-radius textarea" placeholder="Give a feedback" id="feedback" name="feedback" autofocus></textarea>
													</div>
												</div>
												<div class="form-actions text-center">
													<button class="btn btn-submit-btcl" id="submit-feedback" type="submit">Send</button>
												</div>
											</div>
										</form>
									</td>
								</tr>
								<tr>
									<td>
										<label>
											<input type="radio" id="change-status" value="2" name="actionList">
											Change Status
										</label>
									</td>
								</tr>
								<tr class="tr-hide" id="tr-change-status" style="display:none">
									<td>
										<form class="form-horizontal" id="crmComplainStatusForm"
											action="<%=request.getContextPath()%>/CrmComplain/ChangeComplainStatus.do"
											method="post">
											<div class="form-body">
												<div class="form-group">
													<label class="col-sm-3 control-label">New Status</label>
													<div class="col-sm-6">
														<select class="form-control pull-right"
															name="currentStatus" id="currentStatus">
															
															<%
																if (crmEmployeeDTO.isHasPermissionToChangeStatus()) {
															%>
															<option value="<%=CrmComplainDTO.STARTED%>"
																<%if (CrmComplainDTO.STARTED == crmComplainDTO.getCurrentStatus()) {%>
																selected <%}%>>Started</option>
															<option value="<%=CrmComplainDTO.ON_GOING%>"
																<%if (CrmComplainDTO.ON_GOING == crmComplainDTO.getCurrentStatus()) {%>
																selected <%}%>>On Going</option>
																
															<%	} %>	
															<option value="<%=CrmComplainDTO.COMPLETED %>"> Complete </option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label">Message</label>
													<div class="col-sm-6">
														<textarea id='resolverMsg' class="form-control"><%=crmComplainDTO.getLastResolverMsg() == null ? "" : crmComplainDTO.getLastResolverMsg() %></textarea>
													</div>
												</div>
												<div class="form-actions text-center">
													<button class="btn btn-submit-btcl" type="submit">Submit</button>
												</div>
											</div>
										</form>
									</td>
								</tr>
								<%
									if (crmEmployeeDTO.isHasPermissionToPassComplain()) {
								%>
								<tr>
									<td>
										<label>
											<input type="radio" id="pass-complain" value="3" name="actionList">
											Pass Complain
										</label>
									</td>
								</tr>
								<%	} %>
								
								<tr class="tr-hide" id="tr-pass-complain" style="display:none">
									<td>
										<form class="form-horizontal" id="crmComplainPassForm"
											method="post">
											<div class="form-body">
												<div class="form-group">
													<label class="col-sm-3 control-label">Resolver<span class="required" aria-required="true">*</span></label>
													<div class="col-sm-6">
														<input id="complainResolverName" type="text"
															class="form-control" required placeholder="Type to select"
															name="complainResolverName"> 
														<input id="complainResolverID" type="hidden" class="form-control"
															name="complainResolverID" value="-1"><br>
													</div>
												</div>
												
												<div class="form-group">
													<label class="col-sm-3 control-label">Message</label>
													<div class="col-sm-6">
														<textarea class="form-control border-radius textarea"
															placeholder="Give a passing msg"
															id="passingMessage" name="passingMessage" autofocus></textarea>
													</div>
												</div>
												<div class="form-actions text-center">
													<button class="btn btn-submit-btcl" id="submit-pass" type="submit">Submit</button>
												</div>
											</div>
										</form>
									</td>
								</tr>
								<%
									if (crmEmployeeDTO.isHasPermissionToPassComplainToOtherDepartment()) {
								%>
								<tr>
									<td>
										<label>
											<input type="radio" id="pass-complain-other-dept" value="4" name="actionList">
											Pass Complain to Other Dept.
										</label>
									</td>
								</tr>
								<%	} %>
								
								<tr class="tr-hide" id="tr-pass-complain-other-dept" style="display:none">
									<td>
										<form class="form-horizontal" id="crmComplainPassToOtherForm"
											method="post">
												
											
											<div class="form-body">
												<div class="form-group">
													<label for="lnName" class="col-sm-3 control-label">District</label>
													<div class="col-sm-6">
														<input class="form-control"  name="districtNameInput" 
														placeholder="Type to select..."
															value="" type="text" id="districtNameInput"> 
														<input id="districtID" name="districtID" value="" type="hidden">
													</div>
												</div>
												<div class="form-group">
													<label for="lnName" class="col-sm-3 control-label">Upazila</label>
													<div class="col-sm-6">
														<input class="form-control"  name="upazilaNameInput" 
														placeholder="Type to select..."
															value="" type="text" id="upazilaNameInput"> 
														<input id="upazilaID" name="upazilaID" value="" type="hidden">
													</div>
												</div>
												<div class="form-group">
													<label for="lnName" class="col-sm-3 control-label">Union</label>
													<div class="col-sm-6">
														<input class="form-control"  name="unionNameInput" 
														placeholder="Type to select..."
															value="" type="text" id="unionNameInput"> 
														<input id="unionID" name="unionID" value="" type="hidden">
													</div>
												</div>
												<div class="form-group">
													<label for="lnName" class="col-sm-3 control-label">Department<span class="required">*</span></label>
													<div class="col-sm-6">
														<input class="form-control"  name="departmentNameInput" 
														placeholder="Type to select..."
															value="" type="text" id="departmentNameInput"> 
														<input id="departmentID" name="departmentID" value="" type="hidden">
													</div>
												</div>
												
												<div class="form-group">
													<label class="col-sm-3 control-label">Resolver<span class="required" aria-required="true">*</span></label>
													<div class="col-sm-6">
														<input id="resolverPartialName" type="text" class="form-control" required name="resolverPartialName" placeholder="Type to select an employee" >
														<input id="resolverPartialID" type="hidden" class="form-control" value = "-1" name="resolverPartiaID">
													</div>
												</div>	
												<div class="form-group">
													<label class="col-sm-3 control-label">Message</label>
													<div class="col-sm-6">
														<textarea class="form-control border-radius textarea" id="passingMessageToOther" name="passingMessage" placeholder="Give a passing msg" autofocus></textarea>
													</div>
												</div>
												<div class="form-actions text-center">
													<button class="btn btn-submit-btcl" id="submit-forward" type="submit">Submit</button>
												</div>
											</div>
											
										</form>
									</td>
								</tr>
								<%
									if (CrmLogic.canReject(loginDTO, crmComplainDTO)) {
								%>
								<tr>
									<td>
										<label>
											<input type="radio" id="reject-complain" value="5" name="actionList">
											Reject Complain
										</label>
									</td>
								</tr>
								<%	} %>
								<tr class="tr-hide" id="tr-reject-complain" style="display:none">
									<td>
										<form class="form-horizontal" id="crmComplainRejectForm"
											action="<%=request.getContextPath()%>/CrmComplain/RejectComplain.do"
											method="post">
											<div class="form-body">
												
												<input id="r_complainID" type="hidden" class="form-control"
													name="complainID" value="<%=complainID%>">
												<div class="form-group">
													<label class="col-sm-3 control-label">Reason</label>
													<div class="col-sm-6">
														<textarea class="form-control border-radius textarea"
															id="rejectionCause" name="rejectionCause" placeholder = "Give a Reason" autofocus></textarea>
													</div>
												</div>
												<div class="form-actions text-center">
													<button class="btn btn-submit-btcl" type="submit">Submit</button>
												</div>
											</div>
										</form>
									</td>
								</tr>
							</table>
						</div> <!-- table-responsive -->
					</div><!-- col -->
				</div><!-- row -->
				<%
					}
				%>
				<%
					if (loginDTO.isNOC()) {
				%>
				<h3>History</h3>
				<div class="timeline">
				<% for(CrmComplainHistoryDTO crmComplainHistoryDTO : crmComplainHistoryDTOs) {%>
					<div class="timeline-item">
						<div class="timeline-badge">
							<div class="timeline-icon">
								<i class="icon-user-following font-green-haze"></i>
							</div>
						</div>
						<div class="timeline-body">
							<div class="timeline-body-arrow"></div>
							<div class="timeline-body-head">
								<div class="timeline-body-head-caption">
									<a href="javascript:;"
										class="timeline-body-title font-blue-madison"><%= CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmComplainHistoryDTO.getActionTakerID()).getName() %></a> <span class="timeline-body-time font-grey-cascade">Submitted
										at <%= TimeConverter.getDateTimeStringFromDateTime( crmComplainHistoryDTO.getComplainHistorySubmissionTime()) %></span>
								</div>
							</div>
							<div class="timeline-body-content">
								<span class="font-grey-cascade"><%= crmComplainHistoryDTO.getDescription()%><br><b>Assigned To : </b> 
								<a><%= CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(crmComplainHistoryDTO.getComplainResolverID()).getName() %></a></span>
							</div>
						</div>
					</div> <!-- timeline item -->
					<%} %>
				<%} %>
				</div><!-- timeline -->
			</div><!-- portlet body -->
		</div><!-- portlet box btcl -->
	</div><!-- upper col -->
</div><!-- upper row -->



<script type="text/javascript">
	
	$(document).ready(function() {
		$("#crmComplainCompleteForm").on("submit", function(event) {
			event.preventDefault();

			var url = $(this).attr('action');
			var param = {};

			param['complainID'] = $("#complainID").val();

			callAjax(url, param, complainAddCallback, "POST");
		});

		$("#crmComplainStatusForm").on("submit", function(event) {
			event.preventDefault();
			
			var currentStatus = $("#currentStatus").val();
			var complainID = $("#complainID").val();
			var url = "";
			var param = {};
			
			if(currentStatus == <%=CrmComplainDTO.COMPLETED%>){
				url = "<%=request.getContextPath()%>/CrmComplain/CompleteComplain.do";
				
			}else {
				url = $(this).attr('action');
				param.currentStatus = $("#currentStatus").val();
			}
			param.complainID = complainID;
			param.resolverMsg = $("#resolverMsg").val();
			callAjax(url, param, complainAddCallback, "POST");

		});

		$("#submit-feedback").click(function(event) {
			event.preventDefault();
			
			var url = context + 'CrmComplain/Feedback.do';
			var param = {};
			
			param['complainID'] = $("#complainID").val();
			param['feedback'] = $("#feedback").val();


			callAjax(url, param, complainAddCallback, "POST");
		});
		$("#submit-pass").click(function(event) {
			event.preventDefault();

			var url = context + "CrmComplain/PassComplain.do";
			var param = {};
			if($("#complainResolverID").val() < 0){
				$("#complainResolverID").closest('.form-group').addClass('has-error');
			}else {
				param['complainID'] = $("#complainID").val();
				param['complainResolverID'] = $("#complainResolverID").val();
				param['passingMessage'] = $("#passingMessage").val();

				callAjax(url, param, complainAddCallback, "POST");	
			}
			
		});
		$("#submit-forward").click(function(event) {
			event.preventDefault();
			var url = context + "CrmComplain/PassComplain.do";
			var param = {};
			if($("#resolverPartialID").val() < 0 ){
				$('#departmentNameInput').closest('.form-group').addClass('has-error');
				$("#resolverPartialID").closest('.form-group').addClass('has-error');
			}else{
				param['complainID'] = $("#complainID").val();
				param['complainResolverID'] = $("#resolverPartialID").val();
				param['passingMessage'] = $("#passingMessageToOther").val();

				callAjax(url, param, complainAddCallback, "POST");	
			}
			
		});

		$("#crmComplainRejectForm").on("submit", function(event) {
			event.preventDefault();

			var url = $(this).attr('action');
			var param = {};

			param['complainID'] = $("#r_complainID").val();
			param['rejectionCause'] = $("#rejectionCause").val();

			callAjax(url, param, complainAddCallback, "POST");
		});

		function complainAddCallback(data) {
			console.log(data);
			if (data['responseCode'] == 1) {
				toastr.success(data['msg']);
				setTimeout(location.reload(true), 1500);
			} else {

				toastr.error(data['msg']);
			}
		}
		var districtNameInput = $("#districtNameInput");
    	var upazilaNameInput = $("#upazilaNameInput");
    	var unionNameInput = $("#unionNameInput");
    	var departmentNameInput = $("#departmentNameInput");
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
		$('.tr-hide').hide();
		
		$('input:radio[name=actionList]').change(function() {
			$('.tr-hide').hide();
			var self = $(this).val();
			if(self == 1){
				$('#tr-change-feedback').show();
			}else if(self == 2){
				$('#tr-change-status').show();
			}else if(self == 3){
				$('#tr-pass-complain').show();
			}else if(self == 4){
				$('#tr-pass-complain-other-dept').show();
			}else if(self == 5){
				$('#tr-reject-complain').show();
			}
			
    	});
		function processList(url, formData, callback, type) {
			$.ajax({
				type : typeof type != 'undefined' ? type
						: "POST",
				url : url,
				data : formData,
				dataType : 'JSON',
				success : function(data) {
					if (data.responseCode == 2) {
						toastr.error(data.msg);
					} else {
						if (data.payload.length == 0) {
							toastr
									.error("Nothing found!");
						} else {
							callback(data.payload);
						}
					}
				},
				error : function(jqXHR, textStatus,
						errorThrown) {
					toastr.error("Error Code: "
							+ jqXHR.status + ", Type:"
							+ textStatus
							+ ", Message: "
							+ errorThrown);
				},
				failure : function(response) {
					toastr.error(response);
				}
			});
		}

		$("#complainResolverName").autocomplete({  // for pass
			source : function(request, response) {
				$("#complainResolverID")
						.val(-1);
				var term = request.term;
				var url = context
						+ 'CrmEmployee/GetDescendantEmployeesByPartialNameAndEmployeeID.do?';
				var param = {};
				param['resolverPartialName'] = $(
						"#complainResolverName")
						.val();
				param['complainID'] = $('#complainID').val();
				processList(url, param,
						response, "GET");
	
			},
			minLength : 1,
			select : function(e, ui) {
				$('#complainResolverName')
						.val(
								ui.item.employeeName
										+ "["
										+ ui.item.designationName
										+ "]");
				$('#complainResolverID').val(
						ui.item.employeeID);
				return false;
			},
			}).autocomplete("instance")._renderItem = function(ul, data) {
				return $("<li>").append(
					"<a>" + data.employeeName + "["
					+ data.designationName + "]"
					+ "</a>").appendTo(ul);
		};

		$("#resolverPartialName").autocomplete({ // for pass to other dept
			source : function(request, response) {
				$("#resolverPartialID").val(-1);
				var term = request.term;
				var url = context
						+ 'CrmEmployee/GetEmployeesByPartialName.do?';
				var param = {};
				param['assignee'] = $(
						"#resolverPartialName")
						.val();
	
				param['deptID'] = $(
						"#departmentID")
						.val();
	
				processList(url, param,
						response, "GET");
	
			},
			minLength : 1,
			select : function(e, ui) {
				$('#resolverPartialName')
						.val(
								ui.item.employeeName
										+ "["
										+ ui.item.designationName
										+ "]");
				$('#resolverPartialID').val(
						ui.item.employeeID);
				return false;
			},
		}).autocomplete("instance")._renderItem = function(ul, data) {
				return $("<li>").append(
				"<a>" + data.employeeName + "["
				+ data.designationName + "]"
				+ "</a>").appendTo(ul);
	};
});	
</script>
