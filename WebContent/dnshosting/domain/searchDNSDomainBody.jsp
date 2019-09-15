<%@page import="dns.domain.DNSDomainDTO"%>
<%@ page import="coLocation.*, login.*, util.*, common.*, common.repository.*, java.util.*, sessionmanager.*"%>
<%@ page language="java"%>


<%
	String msg = (String)request.getAttribute("msg");
	String url = "DNS/Domain/search";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_DNS_DOMAIN;
	LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
	<% if(msg != null){%>
		<div class="text-center" id="msgDiv">
			<p style="color: #5cb85c"><%=msg %></p>
		</div>
	<%} %>
		<form id="tableForm">
			<div class="table-responsive">
				<table id="tableData" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Domain</th>
							<th>Client</th>
							<th><input type="submit" class="btn btn-xs btn-danger" value="Delete"></th>
						</tr>
					</thead>
					<tbody>
					<%
					
					List<DNSDomainDTO> data = (ArrayList<DNSDomainDTO>) session.getAttribute(SessionConstants.VIEW_DNS_DOMAIN);
					if(data != null){
						for(DNSDomainDTO dnsDomainDTO: data){
					%>
						<tr>
							<td>
							<a target="_blank" href="${context }DNS/Domain/edit.do?domainID=<%=dnsDomainDTO.getID()%>">
							
								<%=dnsDomainDTO.getUnicodeDNSDomainName() %>
								</a>
							</td>
							<td>
							<a href="${context }GetClientForView.do?moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING%>
							&entityID=<%=dnsDomainDTO.getClientID()%>" target="_blank">
								<%=AllClientRepository.getInstance().getClientByClientID(dnsDomainDTO.getClientID()).getLoginName()%>
							</a>	
							</td>
							<td><input type="checkbox" id="<%=dnsDomainDTO.getID()%>" name="deleteIDs" value="<%=dnsDomainDTO.getID()%>"></td>
						</tr>
						<%			
						}
						%>
					</tbody>
					<%	
					}
					%>
				</table>
			</div>
		</form>
	</div>
</div>
<script>
	$(document).ready(function(){
		 $('#tableForm').submit(function(e) {
				e.preventDefault();
			var selected=false;
		    var set = $('#tableData').find('tbody > tr > td:last-child input[type="checkbox"]');
		    $(set).each(function() {
		    	if($(this).prop('checked')){
		    		selected=true;
		    	}
		    });
		    
		    if(!selected){
		    	 bootbox.alert("Select a DNS domain to delete!", function() { });
		    }else{
		    	 bootbox.confirm("Are you sure you want to delete DNS domain (s)?", function(result) {
		             if (result) {
		            	var formData = $("#tableForm").serialize();
						var url = context + "DNS/Domain/delete.do";
						callAjax(url, formData, function(data){
							if(data.responseCode == 1){
								toastr.success(data.msg);
								window.setTimeout(function(){
								   location.reload();
								}, 1000);
							}else {
								toastr.error(data.msg);
							}
						}, "POST");
		             }
		         });
		    }
		});
	});
</script>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
