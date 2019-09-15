<%@page import="complain.ComplainConstants"%>
<%@page import="common.ClientRepository"%>
<%@page import="complain.ComplainSubjectDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="complain.ComplainDAO"%>
<%@page import="comment.CommentDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="common.ModuleConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%
	HashMap<Integer,String> modules = ModuleConstants.ModuleMap;
	ComplainDAO complainDAO = new ComplainDAO();
	ArrayList<ComplainSubjectDTO> complainSubjectDTOs = (ArrayList<ComplainSubjectDTO>)complainDAO.getAllComplainSubject();
	HashMap<Integer, String> priority = ComplainConstants.PRIORITY_MAP;
	HashMap<Integer, String> status = ComplainConstants.STATUS_MAP;
	request.setAttribute("priority", priority);
	request.setAttribute("status", status);
	request.setAttribute("modules", modules);
	request.setAttribute("complainSubjects",complainSubjectDTOs);
	Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
	String moduleName = modules.get(moduleID);
	
%>
<script type="text/javascript">
    function getSubject(sel) {
	if (sel.value == 0) {
	    $('#custom-subject').css('display', 'block');
	} else {
	    $('#custom-subject').css('display', 'none');
	}
    }
</script>

<div class="row">
	<div class="col-md-12">
		<!--  Summary -->
		<div class="portlet box green">
			<!-- /.box-header -->
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i>Add New Complain
				</div>
			</div>
			<div class="portlet-body form">
				<form class="form-horizontal" enctype="multipart/form-data" action="../Complain.do?mode=add" method="post">
					<div class="form-body">
						<div class="form-group" style="display: none;">
							<label for="cmDepartment" class="col-sm-2 control-label">Department</label>
							<div class="col-sm-10">
								<input type="hidden" name="moduleID" value="<%=moduleID %>">
								<input type="text"  class="form-control" value="<%=moduleName %>" disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<label for="cmPriority" class="col-sm-2 control-label">Priority</label>
							<div class="col-sm-10">
								<select class="form-control select" name="priority" style="width: 100%;">
								<c:forEach var="_priority" items="${priority}">
									<option value='${_priority.key }'><c:out value="${_priority.value }"></c:out></option>
								</c:forEach>
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="cmSubject" class="col-sm-2 control-label">Subject</label>
							<div class="col-sm-10">
								<select class="form-control select" name="subjectID" style="width: 100%;" onchange="getSubject(this);">
									<c:forEach var="complainSubject" items="${ complainSubjects }">
										<option value='${complainSubject.csID }'><c:out value="${complainSubject.csName}"></c:out></option>
									</c:forEach>
									<option value='0'>Custom Subject</option>
								</select>
							</div>
						</div>
						<div class="form-group" style="display: none;" id="custom-subject">
							<label for="cmSummary" class="col-sm-2 control-label">Custom Subject</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" name="otherSubject" placeholder="Write own subject...">
							</div>
						</div>
						<div class="form-group">
							<label for="cmSummary" class="col-sm-2 control-label">Summary</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" name="summary" placeholder="Write key problem..." required>
							</div>
						</div>
						<div class="form-group">
							<label for="cmMessage" class="col-sm-2 control-label">Message</label>
							<div class="col-sm-10">
								<textarea class="form-control" rows="5" name="complainHistory.message" placeholder="Describe your problem..." required></textarea>
							</div>
						</div>

						<!-- <div class="form-group">
							<label for="cmDocument" class="col-sm-2 control-label">Attach Document</label>
							<div class="col-sm-10">
								<input id="lefile" type="file" name="complainHistory.document" style="display: none">
								<div class="input-append">
									<input id="attachment" class="form-control" type="text" disabled> <a style="margin-top: 3px;"
										class="btn btn-default btn-sm" onclick="$('input[id=lefile]').click();">Browse</a><span
										style="padding-left: 10px; padding-top: 5px; color: red;">* if multiple file then zip them</span>
								</div>
							</div>
							<script type="text/javascript">
								$('input[id=lefile]').change(function() {
				    			$('#attachment').val($(this).val());
								});
			   				 </script>
						</div> -->

						<div class="form-actions right">
							<button type="submit" class="btn green">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>