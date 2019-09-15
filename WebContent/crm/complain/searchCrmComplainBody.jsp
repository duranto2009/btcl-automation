<%@page import="crm.CrmComplainDTO" %>
<%@page import="crm.CrmEmployeeDTO" %>
<%@page import="crm.repository.CrmAllEmployeeRepository" %>
<%@page import="login.LoginDTO" %>

<%@page import="org.apache.log4j.Logger" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="util.TimeConverter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>


<style type="text/css">
    button.dt-button, div.dt-button, a.dt-button {
        color: white !important;
        border-color: white !important;
    }
</style>


<%
    String msg = null;
    String url = "CrmComplainSearch/Complains";
    String navigator = SessionConstants.NAV_CRM_COMPLAIN;
    String context = "../../.." + request.getContextPath() + "/";
    Logger searchLogger = Logger.getLogger(getClass());
    LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    List<CrmEmployeeDTO> rootCrmEmployeeDTOList = CrmAllEmployeeRepository.getInstance().getRootEmployeeList();
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link
        href="<%=context%>/assets/global/plugins/datatables/datatables.min.css"
        rel="stylesheet" type="text/css"/>
<link
        href="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css"
        rel="stylesheet" type="text/css"/>

<!-- 
	<link href="<%=context%>/domain/domainQueryForBuy/domain-query-buy.css"
      rel="stylesheet" type="text/css"/>
 -->

      
 
<jsp:include page="../../includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>"/>
    <jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>

<%
    try {
%>

<div class="row">
    <div class="col-md-12">

        <div class="portlet box">
            <div class="portlet-body">
                <div class="table">
                    <table id="tableId"
                           class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th class="text-center">SubToken ID</th>
                            <%if(localLoginDTO.isNOC()){ %>
                             <th class="text-center">Token ID</th>
                            <%} %>
                            <th class="text-center">Priority</th>
                            <th class="text-center">Resolver</th>
                            <th class="text-center">Previous Resolver</th>
                            <th class="text-center">Status</th>
                            <th class="text-center">Submission Time</th>
                            <%if(localLoginDTO.isNOC()){ %>
                            	<th class="text-center">Block</th>
                            <%} %>
                        </tr>
                        </thead>
                        <tbody>

                        <%
                            ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_CRM_COMPLAIN);

                            if (data != null) {
                                int size = data.size();
                                for (int i = 0; i < size; i++) {
                                    try {
                                        CrmComplainDTO crmComplainDTO = (CrmComplainDTO) data.get(i);
                                        CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance()
                                                .getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getComplainResolverID());
                                        if(crmEmployeeDTO == null){
                                        	continue;
                                        }
                                        CrmEmployeeDTO previousEmployeeDTO = null;
                                        if(crmComplainDTO.getPreviousComplainResolverID() != null){
                                        	previousEmployeeDTO  = CrmAllEmployeeRepository.getInstance()
                                                .getCrmEmployeeDTOByEmployeeID(crmComplainDTO.getPreviousComplainResolverID());
                                        }
                        %>
                        <tr>

                            <td class="text-center"><a
                                    href="<%=context%>crm/complain/viewCrmComplain.jsp?complainID=<%=crmComplainDTO.getID()%>">Sub-Tok<%=crmComplainDTO.getID()%>
                            </a></td>
                            <%if(localLoginDTO.isNOC()){ %>
                             <td class="text-center">
                             <%if(crmComplainDTO.getCommonPoolID() != null){ %>
                             <a href="<%=context%>CrmClientComplain/Complain.do?id=<%=crmComplainDTO.getCommonPoolID() %>">Tok<%=crmComplainDTO.getCommonPoolID()%>
                            </a><%} %></td>
                            <%} %>
                            <td class="text-center"><%=CrmComplainDTO.mapComplainPriorityStringToPriorityID.get(crmComplainDTO.getPriority()) %>
                            </td>
                            <td class="text-center">
                            	
								<a target = "_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=crmEmployeeDTO.getCrmEmployeeID() %>">
                            		<%=crmEmployeeDTO.getName()%>
                            	</a>
                            	
                            </td>
                            <td class="text-center">
                            <%
									if(previousEmployeeDTO == null){
								%>
										-
								<%		
									}else {
								%>
									
									<a target="_blank" href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=previousEmployeeDTO.getCrmEmployeeID()%>">
										
											<%=previousEmployeeDTO.getName() %>
									</a>
								<%
									}
								%>
                            
                            
                            <td class="text-center"><%=CrmComplainDTO.mapComplainStatusStringToStatusID.get(crmComplainDTO.getCurrentStatus())%>
                            <td class="text-center"><%=TimeConverter.getMeridiemTime(crmComplainDTO.getGenerationTime()) %>
                            </td>
                            <%if(localLoginDTO.isNOC()){ %>
                            <td class="text-center">
                            	<button class="btn btn-danger <%=crmComplainDTO.isBlocked()==false?"active":"disabled" %> btn-block" 
                            		data-complain-id = "<%=crmComplainDTO.getID()%>" type="button">
                            		Block
                            	</button>
                            </td>
                            

							<%}
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                %>
                        </tr>
                        <%
                            }
                            session.removeAttribute(SessionConstants.VIEW_CRM_COMPLAIN);
                        %>
                        </tbody>
                    </table>
                </div>
                </form>
            </div>
        </div>
    </div>
</div>
            <%
	        } catch (Exception ex) {
		        searchLogger.debug("Exception ", ex);
	}
%>

<script type="text/javascript">
$("#passAction").on("click", function(e) {
    e.preventDefault();
    var form = $('#complainFrom').attr('action', context + "CrmComplain/PassComplain.do");
    form.submit();
});
$("#passToOtherAction").on("click", function(e) {
    e.preventDefault();
    $("#passingMessage").val($('#passingMessageToOther').val());
    $("#complainResolverID").val($('#resolverPartialID').val());
    var form = $('#complainFrom').attr('action', context + "CrmComplain/PassComplains.do");
    form.submit();
});
$("#feedbackAction").on("click", function(e) {
    e.preventDefault();
    var form = $('#complainFrom').attr('action', context + "CrmComplain/Feedbacks.do");
    form.submit();
});
$(document).ready(function() {
	var blockBtn = $('.btn-block');
	blockBtn.click(function(event){
		var url = context + "CrmComplain/changeBlockedStatus.do";
		var param = {};
		var self = $(event.target);
		if(self.hasClass('disabled')){
			return false;
		}
		bootbox.confirm("Are you sure you want to block this complain? Once blocked it can not be undone", function(result){
			if(result){
				param.complainID = self.data('complain-id');
				callAjax(url, param, function(data){
					if(data.responseCode == 1){
						toastr.success(data.msg);
						self.removeClass('active');
						self.addClass('disabled');
					}else {
						toastr.error(data.msg);
					}
				}, "POST");		
			}
		});
		
		return false;
	});
    var table = $("#tableId");
    var currentForm = $("#tableForm");
    $('.group-checkable', table).change(function() {
        var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
        var checked = $(this).prop("checked");
        $(set).each(function() {
            $(this).prop("checked", checked);
        });
        $.uniform.update(set);
    });
    $('#tableForm button[type=submit]').click(function(e) {
        var currentSubmit = this;
        var selected = false;
        e.preventDefault();
        var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
        $(set).each(function() {
            if ($(this).prop('checked')) {
                selected = true;
            }
        });
        if (!selected) {
            bootbox.alert("Select Domain  to change privilege !", function() {});
        } else {
            bootbox.confirm("Are you sure to change privilege of these Domain(s)?", function(result) {
                if (result) {
                    $('#passAction').val($(currentSubmit).val());
                    currentForm.submit();
                }
            });
            /*bootbox.prompt({
             title : "Are you sure to pass this/these complain(s)?",
             inputType : 'text'
             callback : function(result){

             }
             });
             bootbox.prompt({
             title: "This is a prompt with a number input!",
             inputType: 'text',
             callback: function (result) {
             console.log(result);
             }
             });*/
        }
    });

    function processList(url, formData, callback, type) {
        $.ajax({
            type: typeof type != 'undefined' ? type : "POST",
            url: url,
            data: formData,
            dataType: 'JSON',
            success: function(data) {
                if (data.responseCode == 2) {
                    toastr.error(data.msg);
                } else {
                    if (data.payload.length == 0) {
                        toastr.error("No employee found!");
                    } else {
                        callback(data.payload);
                    }
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus + ", Message: " + errorThrown);
            },
            failure: function(response) {
                toastr.error(response);
            }
        });
    }
    $("#complainResolverName").autocomplete(  {
        source: function(request, response) {
            $("#complainResolverID").val(-1);
            var term = request.term;
            var url = context + 'CrmEmployee/GetDescendantEmployeesByPartialName.do?';
            var param = {};
            param['complainResolverName'] = $("#complainResolverName").val();
            processList(url, param, response, "GET");
        },
        minLength: 1,
        select: function(e, ui) {
            $('#complainResolverName').val(ui.item.employeeName + "[" + ui.item.designationName + "]");
            $('#complainResolverID').val(ui.item.employeeID);
            return false;
        },
    }).autocomplete("instance")._renderItem = function(ul, data) {
        return $("<li>").append("<a>" + data.employeeName + "[" + data.designationName + "]" + "</a>").appendTo(ul);
    };
    $("#resolverPartialName").autocomplete(
    {
        source: function(request, response) {
            $("#resolverPartialID").val(-1);
            var term = request.term;
            var url = context + 'CrmEmployee/GetDescendantEmployeesByPartialNameAndEmployeeID.do?';
            var param = {};
            param['resolverPartialName'] = $("#resolverPartialName").val();
            param['rootEmployeeID'] = $("#rootEmployeeID").val();
            processList(url, param, response, "GET");
        },
        minLength: 1,
        select: function(e, ui) {
            $('#resolverPartialName').val(ui.item.employeeName + "[" + ui.item.designationName + "]");
            $('#resolverPartialID').val(ui.item.employeeID);
            return false;
        },
    }).autocomplete("instance")._renderItem = function(ul, data) {
        return $("<li>").append("<a>" + data.employeeName + "[" + data.designationName + "]" + "</a>").appendTo(ul);
    };
})
</script>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>


