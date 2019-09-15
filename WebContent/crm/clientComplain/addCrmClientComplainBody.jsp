<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="crm.CrmComplainDTO"%>
<%@page import="crm.CrmDesignationDTO"%>
<%@page import="crm.CrmEmployeeDTO"%>
<%@page import="crm.repository.CrmAllDesignationRepository"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="crm.repository.CrmDepartmentRepository"%>
<%@page import="crm.service.CrmDesignationService"%>
<%@page import="file.FileTypeConstants"%>
<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<link href="../../assets/global/css/components.min.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../../assets/global/css/plugins.min.css" rel="stylesheet"
	type="text/css" />
<link
	href="../../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="../../assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="../../assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="../../assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" type="text/css" />
<link href="../../assets/global/plugins/clockface/css/clockface.css"
	rel="stylesheet" type="text/css" />
<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	HashMap<Integer, Integer> mapOfModulesToEntityTypeID = EntityTypeConstant.mapOfModulesToEntityTypeID;
	List<CrmEmployeeDTO> crmEmployeeDTOs = CrmAllEmployeeRepository.getInstance()
			.getCrmEmployeeDTOListByUserID(loginDTO.getUserID());
	List<Integer> modulesForClient = new ArrayList<Integer>();
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-search-plus"></i> Write Your Query Below
		</div>
		<div class="tools">
			<a class="collapse" href="javascript:;" data-original-title=""
				title=""> </a>
		</div>
	</div>

	<div class="portlet-body form">
		<form class="form-horizontal" id="fileupload" method="post"
			enctype="multipart/form-data">
			<div class="form-body">
				<%
					if (loginDTO.isNOC()) {
				%>
				<div class="row">
					<div class="col-sm-6 col-md-6">
						<div class="form-group">
							<label class="col-sm-4 control-label">Client<span
								class="required" aria-required="true"> * </span></label>
							<div class="col-sm-8">
								<input id="clientIDStr" type="text" class="form-control"
									placeholder="Type to search client" name="clientIDStr">
								<input id="clientID" type="hidden" value="-1"
									class="form-control" name="clientID">
							</div>
						</div>
					</div>
					<div class="col-sm-6 col-md-6">
						<div class="form-group">
							<label class="control-label col-sm-4">On Behalf Of<span
								class="required" aria-required="true"> * </span></label>
							<div class="col-sm-8">
								<select name="crmEmployeeID" class="form-control border-radius ">
									<%
										for (CrmEmployeeDTO crmEmployeeDTO : crmEmployeeDTOs) {
												if (CrmDesignationService.isNOC(crmEmployeeDTO)) {
													CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance()
															.getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
													String designationName = crmDesignationDTO.getName();
													CrmDesignationDTO crmRootDesignationDTO = CrmAllDesignationRepository.getInstance()
															.getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
													String departmentName = CrmDepartmentRepository.getInstance()
															.getCrmDepartmentByRootDesignationID(crmRootDesignationDTO.getDesignationID())
															.getDepartmentName();
									%>
									<option value="<%=crmEmployeeDTO.getCrmEmployeeID()%>"><%=crmEmployeeDTO.getName()%>[<%=designationName%>][<%=departmentName%>]
									</option>
									<%
										}
									}
									%>
								</select>
							</div>
						</div>
					</div>
				</div>
				<%
					} else {
						modulesForClient = AllClientRepository.getInstance().getRegisteredModulesByClientID(loginDTO.getAccountID());
					}
				%>
				<div class="form-group">
					<label class="col-sm-2 control-label">Service Type<span
						class="required" aria-required="true"> * </span></label>
					<div class="col-sm-4">
						<select class="form-control select" id="entityTypeID"
							name="entityTypeID" style="width: 100%;">
							<%
							if (!loginDTO.isNOC()) {
								for(int entityTypeID : mapOfModulesToEntityTypeID.keySet()){
									int moduleID = mapOfModulesToEntityTypeID.get(entityTypeID);
									for(int id: modulesForClient){
										if(id == moduleID){
								%>
										<option value="<%=entityTypeID %>"><%=ModuleConstants.ActiveModuleMap.get(moduleID) %></option>
								<%			
										}
									}
								}
							}else {
							%>
								
							<%}%>	
							
						</select>

					</div>
					<label class="col-sm-2 control-label">Priority</label>
					<div class="col-sm-4">
						<select class="form-control pull-right" name="priority">
							<option value="<%=CrmComplainDTO.NORMAL%>">Normal</option>
							<option value="<%=CrmComplainDTO.LOW%>">Low</option>
							<option value="<%=CrmComplainDTO.HIGH%>">High</option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label id="serviceText" class="col-sm-2 control-label">Service</label>
					<div class="col-sm-4">
						<input id="entityName" type="text" class="form-control"
							placeholder="Type to search service type" name="name"> <input
							id="entityID" type="hidden" class="form-control" name="entityID">
					</div>
					<label class="col-sm-2 control-label">Time of
						Incident</label>
					<div class="col-sm-4">
						<div class="input-group date form_datetime">
							<input type="text" size="16" readonly class="form-control"
								name="timeOfIncidentString"> <span
								class="input-group-btn">
								<button class="btn default date-set" type="button">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Subject</label>
					<div class="col-sm-4">
						<input id="complainSubject" type="text" class="form-control"
							placeholder="Type to search subject" name="subject">
					</div>
					<label class="col-sm-2 control-label">Frequency</label>
					<div class="col-sm-4">
						<input type="number" class="form-control"
							placeholder="How many times you face this problem" min="0"
							name="frequency">
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label">Description<span
						class="required" aria-required="true"> * </span></label>
					<div class="col-sm-10">
						<div class="inbox-editor-open inbox-form-group">
							<textarea id="currentDescription"
								class="inbox-editor inbox-wysihtml5 form-control"
								name="clientComplain" rows="7" placeholder="Comment" required></textarea>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="text-center" style="padding: 0px;">
						<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
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
							<div class="progress progress-striped active" role="progressbar"
								aria-valuemin="0" aria-valuemax="100">
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
					<div class="row">
						<div>
							<button class="btn btn-reset-btcl" type="reset">Reset</button>
							<button class="btn btn-submit-btcl" id="btn-submit" type="submit">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<script src="../../assets/global/plugins/moment.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/clockface/js/clockface.js"
	type="text/javascript"></script>
<script src="../../assets/global/scripts/app.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/pages/scripts/components-date-time-pickers.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function setCurrentServiceText() {
		var entityTypeID = $("#entityTypeID").val();
		if (entityTypeID == 101) {
			$("#serviceText").text("Domain Name");
		} else if (entityTypeID == 602) {
			$("#serviceText").text("Vpn Link Name");
		} else if (entityTypeID == 702) {
			$("#serviceText").text("LLI Connection Name");
		}else if (entityTypeID == 402) {
			$("#serviceText").text("CoLocation Connection Name");
		}else if (entityTypeID == 902) {
			$("#serviceText").text("NIX Connection Name");
		}
	}
	function complainAddCallback(data) {
		if (data['responseCode'] == 1) {
			toastr.success(data['msg']);
			var redirectURL = "${context}CrmClientComplain/Complain.do?id="
					+ data.payload;
			var redirectTime = 2500;
			setTimeout(function() {
				location.href = redirectURL
			}, redirectTime);
		} else {
			toastr.error(data['msg']);
		}
	}
	function validateForm() {
		var flag = true;
		if ($('#clientID').val() < 0) {
			$('#clientIDStr').closest('.form-group').addClass('has-error');
			flag = false;
		}
		if ($('#currentDescription').val().length == 0) {
			$('#currentDescription').closest('.form-group').addClass('has-error');
			$('#currentDescription').closest('div').addClass('has-error');
			flag = false;
		}
		if(!$('select[name=entityTypeID]').val() || $('select[name=entityTypeID]').val().length ==0){
			$('#entityTypeID').closest('div').addClass('has-error');
			$($('#entityTypeID').closest('.form-group').find('label')[0]).addClass('has-error');
			flag = false;
		}
		return flag;
	}
	function processList(url, formData, callback, type) {
		$.ajax({
			type : typeof type != 'undefined' ? type: "POST",
			url : url,
			data : formData,
			dataType : 'JSON',
			success : function(data) {
				if (data.responseCode == 2) {
					toastr.error(data.msg);
				} else {
					if (data.payload.length == 0) {
						toastr.error("No data found!");
					} else {
						callback(data.payload);
					}
				}
			},
			error : function(jqXHR, textStatus,errorThrown) {
				toastr.error("Error Code: "+ jqXHR.status + ", Type:"+ textStatus+ ", Message: "+ errorThrown);
			},
			failure : function(response) {
				toastr.error(response);
			}
		});
	}
	function callBackForRegisteredClient(data){
		var select = $('select[name=entityTypeID]');
		$.each(data, function(index, value){
			$(select).empty();
			$(select).append('<option value="-1">Select a Source Module</option>');
			<%
			for(int entityTypeID : mapOfModulesToEntityTypeID.keySet()){
				int moduleID = mapOfModulesToEntityTypeID.get(entityTypeID);
			%>
				$.each(data, function(index, value){
					if(value.moduleID == <%=moduleID%>)	{
						$(select).append('<option value="'+<%=entityTypeID%>+'">'+value.moduleName+'</option>');
					}
				});
			<%}%>
		});
	}
	$(document).ready(function() {
		$("#btn-submit").click( function(event) {
			event.preventDefault();
			if (validateForm() == false) {
				return false;
			} else {
				$(".jFile").attr('disabled',true);
				var url = context + "CrmClientComplain/CreateComplainViaWeb.do"
				var param = $("#fileupload").serialize();
				param['clientComplain'] = $("#currentDescription").val();
				param['clientID'] = $("#clientID").val();
				var documents = [];
				$('input[name=documents]').each(function() {
					documents.push($(this).val());
				});
				param['documents'] = documents;
				param['priority'] = $('input[name=priority]').val();
				param['name'] = $('input[name=name]').val();
				param['entityTypeID'] = $('select[name=entityTypeID]').val();
				param['subject'] = $('input[name=subject]').val();
				param['frequency'] = $('input[name=frequency]').val();
				param['timeOfIncidentSring'] = $('input[name=timeOfIncidentString]').val();
				param['crmEmployeeID'] = $('#crmEmployeeID').val();
				callAjax(url, param,complainAddCallback,"POST");
			}
		});
		var moduleID = -1;
		$("#clientIDStr").autocomplete({
			source : function(request, response) {
				$("#clientID").val(-1);
				var term = request.term;
				var url = context + 'AutoComplete.do?moduleID=' + moduleID + '&need=client&status=active';
				var formData = {};
				formData['name'] = term;
				if (term.length >= 2) {
					callAjax(url, formData,response, "GET");
				} else {
					delay(function() {
						toastr.info("Your search name should be at lest 2 characters");
					},
					systemConfig.getTypingDelay());
				}
			},
			minLength : 1,
			select : function(e, ui) {
				$('#clientIDStr').val(ui.item.data);
				$('#clientID').val(ui.item.id);
				var formData = {}, url=context + "AutoComplete.do?need=modulesForClient";
				formData.clientID = (ui.item.id);
				callAjax(url, formData, callBackForRegisteredClient, 'GET');
				return false;
			},
		}).autocomplete("instance")._renderItem = function( ul, item) {
			return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
		};
		$("#entityName").autocomplete({
			source : function(request, response) {
				var term = request.term;
				var url = context
						+ 'CrmEntity/GetEntityListByEntityTypeID.do?';
				var param = {};
				param['clientID'] = $(
						"#clientID").val();
				param['entityTypeID'] = $(
						"#entityTypeID").val();
				param['entityName'] = $(
						"#entityName").val();
				processList(url, param,
						response, "GET");
			},
			minLength : 1,
			select : function(e, ui) {
				$('#entityName').val(
						ui.item.value);
				$('#entityID').val(
						ui.item.key);
				return false;
			},
		}).autocomplete("instance")._renderItem = function(ul, data) {
			return $("<li>").append("<a>" + data.value + "</a>").appendTo(ul);
		};
		$("#complainSubject").autocomplete({
			source : function(request, response) {
				var term = request.term;
				var url = context+ 'CrmClientComplain/GetComplainSubjectByEntityTypeID.do?';
				var param = {};
				param['complainSubject'] = $("#complainSubject").val();
				param['entityTypeID'] = $("#entityTypeID").val();
				processList(url, param,response, "GET");
			},
			minLength : 1,
			select : function(e, ui) {
				$('#complainSubject').val(ui.item.subject);
				return false;
			},
		}).autocomplete("instance")._renderItem = function(ul, data) {
			return $("<li>").append("<a>" + data.subject + "</a>").appendTo(ul);
		};
		setCurrentServiceText();
		$("#entityTypeID").on('change', function() {
			setCurrentServiceText();
		});
	});
</script>


