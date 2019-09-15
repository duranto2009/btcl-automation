<%@page import="common.repository.AllClientRepository"%>
<%@page import="complain.ComplainConstants"%>
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.ClientRepository"%>
<%@page import="java.util.HashMap"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<script src="${context}scripts/complain/complain.js" type="text/javascript"></script>
<%
	LoginDTO localLoginDTO =  (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	HashMap<Integer, String> modules = ModuleConstants.ModuleMap;
	HashMap<Integer, String> priority = ComplainConstants.PRIORITY_MAP;
	HashMap<Integer, String> status = ComplainConstants.STATUS_MAP;
	request.setAttribute("priority", priority);
	request.setAttribute("status", status);
	request.setAttribute("modules", modules);
	request.setAttribute("loginDTO", localLoginDTO);
%>
<style type="text/css">
.timeline:before {
	content: none;
}
#menu {
	margin: 1px;
}
</style>
<% 
String failureMsg = (String)request.getAttribute("failureMsg");
%>
<%if(failureMsg != null){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong ><%=failureMsg %></strong>
</div><%}%>
<div class="row">
	<div class="col-md-5">
		<!--  Summary -->
		<div class="portlet box green">
			<!-- /.box-header -->
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-stack-overflow"></i>Complain Details
				</div>
			</div>
			<div class="portlet-body">
				<div class="table-scrollable">
					<table class="table table-hover">
						<tbody>
							<tr>
								<td>Module</td>
								<td><c:out value="${modules[complain.moduleID]}"></c:out></td>
							</tr>
							<c:if test="${ complain.subjectID > 0  }">
								<tr>
									<td>Subject</td>
									<td><c:out value="${complainSubject}"></c:out></td>
								</tr>
							</c:if>
							<c:if test="${ complain.subjectID == 0  }">
								<tr>
									<td>Custom Subject</td>
									<td><c:out value="${complain.otherSubject}"></c:out></td>
								</tr>
							</c:if>
							<tr>
								<td>Summary</td>
								<td><c:out value="${complain.summary}"></c:out></td>
							</tr>
							<tr>
								<td>Priority</td>
								<td><c:out value="${priority[complain.priority]}"></c:out></td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td>Is Taken</td> -->
<%-- 								<td><c:choose> --%>
<%-- 										<c:when test="${complain.isTaken  > 0 }"> --%>
<!-- 												Yes -->
<%-- 											</c:when> --%>
<%-- 										<c:otherwise> --%>
<!-- 												No -->
<%-- 											</c:otherwise> --%>
<%-- 									</c:choose></td> --%>
<!-- 							</tr> -->
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-7">
		<!--  Summary -->
		<div class="portlet box green">
			<!-- /.box-header -->
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-opencart"></i>Full Conversation
				</div>
			</div>
			<div class="portlet-body">
				<div class="timeline">
					<!-- Complain Conversation-->
					<div class="timeline-item" id="load-history">
						<c:forEach var="complainHistory" items="${ complainHistoryAll }">			
								<c:if test="${complainHistory.accountID > 0 }">
									<div class="timeline-body" style="margin-left: 0px; background-color:#eee">
								</c:if>
								<c:if test="${complainHistory.accountID < 0 }">
									<div class="timeline-body" style="margin-left: 0px; background-color:#EED">
								</c:if>
								<div class="timeline-body-head">
									<c:if test="${complainHistory.accountID > 0 }">
										<c:set var="clientID" value="${complainHistory.accountID}"></c:set>
										<%
											long clientID = (Long)pageContext.getAttribute("clientID");ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);request.setAttribute("client", clientDTO);
										%>
										<div class="timeline-body-head-caption">
											<a href="javascript:;" class="timeline-body-title font-blue-madison"><i class="fa fa-user"></i>&nbsp;<c:out
													value="${client.loginName }"></c:out> </a><span class="timeline-body-time font-grey-cascade"> <jsp:setProperty
													name="date" property="time" value="${complainHistory.lastModificationTime }" /> <fmt:formatDate value="${date}"
													pattern="dd/MM/yyyy hh:mm a" />
											</span>
										</div>
									</c:if>
									<c:if test="${complainHistory.accountID < 0 }">
										<c:set var="adminID" value="${complainHistory.accountID}"></c:set>
										<%
											long adminID = (Long)pageContext.getAttribute("adminID");
											UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(-adminID);
											request.setAttribute("admin", userDTO);
										%>
										<div class="timeline-body-head-caption">
											<a href="javascript:;" class="timeline-body-title font-blue-madison"><i class="fa fa-user-plus"></i>&nbsp;<c:out
													value="${admin.username }"></c:out></a><span class="timeline-body-time font-grey-cascade"> <jsp:setProperty
													name="date" property="time" value="${complainHistory.lastModificationTime }" /> <fmt:formatDate value="${date}"
													pattern="dd/MM/yyyy hh:mm a" />
											</span>
										</div>
									</c:if>
								</div>
								<div class="timeline-body-content">
									<c:if test="${complainHistory.accountID < 0 }">
										<strong style="color: green;">Status : <c:out value="${ status[complainHistory.status] }"></c:out></strong>
										<br>
										<br>
									</c:if>

									<c:if test="${ loginDTO.isAdmin}">
										<span class="font-grey-cascade"> <c:if test="${ complainHistory.note != null}">
												<strong>Note : <c:out value=" ${complainHistory.note }"></c:out>
												</strong>
												<br>
												<br>
											</c:if> <c:out value="${ complainHistory.message }"></c:out></span>
									</c:if>
									<c:if test="${!loginDTO.isAdmin}">
										<span class="font-grey-cascade"> <c:out value="${ complainHistory.message }"></c:out></span>
									</c:if>
									<c:if test="${histoyFile[complainHistory.ID] != null}">
										<c:set var="et" value="${histoyFile[complainHistory.ID].docEntityTypeID}"></c:set>
										<c:set var="e" value="${histoyFile[complainHistory.ID].docEntityID}"></c:set>
										<%
											String fileURI = "../common/downloadFile.jsp?et="+pageContext.getAttribute("et")+"&e="+pageContext.getAttribute("e");
										%>
										<br>
										<br>
										<span class="font-grey-cascade"><strong>Attachment : </strong> <a href="<%=fileURI%>"><c:out
													value="${histoyFile[complainHistory.ID].docActualFileName }"></c:out></a> </span>
									</c:if>
								</div>
							</div>
						</c:forEach>
						<!-- load history here -->
					</div>
					<!-- /Complain Conversation -->
					<hr>
					<h3 style="color: #1ba39c; font-weight: 500;">&nbsp;Add More Conversation</h3>
					<br>
					<c:choose>
						<c:when test="${loginDTO.isAdmin}">
							<form method="post" action="../ComplainHistory.do?mode=history&requestedURL=<%=request.getAttribute("requestedURL")%>"  id="complainHistoryForm"
								enctype="multipart/form-data">
								<div class="form-body">
									<div id="statusDiv">
										<div class="form-group">
											<select class="form-control" name="status" id="reply_status">
												<option value='-1'>Please select correct reply type</option>
												<c:forEach var="_status" items="${status}">
													<c:if test="${_status.key > 0}">
														<option value='${_status.key}'><c:out value="${_status.value}"></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</div>
									</div>

									<div id="othersDiv">
										<input type="hidden" name="complainID" value="${complain.ID}" id="complainID"> <input type="hidden"
											name="accountID" value="${ -loginDTO.userID}" id="accountID">
										<div class="form-group">
											<input class="form-control spinner" type="text" id="note" name="note"
												placeholder="Write note for furture help" required>
										</div>

										<div class="form-group">
											<textarea class="form-control spinner" rows="3" id="message" name="message"
												placeholder="Write your message here" required></textarea>
										</div>
										<!-- <div class="form-group">
											<div class="row">
												<div class="col-md-9">
													<input id="lefile" type="file" name="document" style="display: none">
													<div class="input-append">
														<input id="attachment" class="form-control" type="text" placeholder="Browse file to upload" disabled>
													</div>
												</div>
												<div class="col-md-3">
													<a class="btn btn-default btn-md" onclick="$('input[id=lefile]').click();">Browse</a>
												</div>
											</div>
											<span style="padding-left: 10px; padding-top: 5px; color: #f36a5a; font-size: 10px;">If multiple file
												then zip them</span>
											<script type="text/javascript">
												fileAdd();
					   						 </script>
										</div> -->
										<div class="form-group">
											<input type="submit" class="btn green-sharp btn-outline  btn-block sbold uppercase" id="submit-comment">
										</div>
									</div>

								</div>
							</form>
							<script type="text/javascript">
				replyStatusSelect();
			    </script>
						</c:when>
						<c:otherwise>
							<form method="post" action="../ComplainHistory.do?mode=history&requestedURL=<%=request.getAttribute("requestedURL")%>"  id="complainHistoryForm"
								enctype="multipart/form-data">
								<div class="form-body">
									<input type="hidden" name="complainID" value="${complain.ID }" id="complainID"> <input type="hidden"
										name="accountID" value="${ loginDTO.accountID }" id="accountID" required>

									<div class="form-group">
										<textarea class="form-control spinner" rows="3" id="message" name="message"
											placeholder="Write your message here" required></textarea>
									</div>
									<!-- <div class="form-group">
										<div class="row">
											<div class="col-md-9">
												<input id="lefile" type="file" name="document" style="display: none">
												<div class="input-append">
													<input id="attachment" class="form-control" type="text" placeholder="Browse file to upload" disabled>
												</div>
											</div>
											<div class="col-md-3">
												<a class="btn btn-default btn-md" onclick="$('input[id=lefile]').click();">Browse</a>
											</div>
										</div>
										<span style="padding-left: 5px; padding-top: 5px; color: #f36a5a; font-size: 11px;">If multiple file
											then zip them</span>
										<script type="text/javascript">
					    fileAdd();
					</script>
									</div> -->
									<div class="form-group">
										<input type="submit" class="btn green-sharp btn-outline  btn-block sbold uppercase" id="submit-comment">
									</div>

								</div>
							</form>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
