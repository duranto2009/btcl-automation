<%@page import="common.EntityTypeConstant"%>
<%@page import="dns.domain.DNSSubDomainDTO"%>
<%@page import="java.util.List"%>
<%@page import="dns.domain.DNSDomainDTO"%>
<%
	DNSDomainDTO dnsDomainDTO = (DNSDomainDTO) request.getAttribute("dnsDomain");
	List <DNSSubDomainDTO> dnsSubDomainDTOs = (List<DNSSubDomainDTO>)request.getAttribute("dnsSubDomain");
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body">
		<div class="table-responsive">
			<table class="table table-bordered table-striped table-hover">
				<tbody>
					<tr>
						<th scope="row">DNS Domain</th>
						<td colspan = "5"> 
<%-- 							<a href="${context }DNS/Domain/view.do?domainID=<%=dnsDomainDTO.getID() %>" target="_blank"> --%>
								<%=dnsDomainDTO.getUnicodeDNSDomainName() %>
<!-- 							</a> -->
						</td>
					</tr>
					<%
					if(!dnsSubDomainDTOs.isEmpty()){
						for(DNSSubDomainDTO dnsSubDomainDTO: dnsSubDomainDTOs){
						
					%>
					<tr>
						<th scope="row">Sub Domain</th>
						<td><%=dnsSubDomainDTO.getSubDomainName() %></td>
					
						<th scope="row">IP</th>
						<td><%=dnsSubDomainDTO.getIpAddress() %></td>
						
						<th scope="row">Record Type</th>
						<td><%=EntityTypeConstant.mapOfRecordTypeToRecordTypeStr
										.get(dnsSubDomainDTO.getRecordType()) %></td>
					</tr>					
					<%	
							};
					} 
					
					%>
					
				</tbody>
			</table>
		</div>
	</div>
</div>