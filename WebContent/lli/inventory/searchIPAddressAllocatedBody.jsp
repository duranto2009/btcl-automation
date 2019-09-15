<%@page import="lli.link.LliLinkService"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="ipaddress.IpAddressService"%>
<%@page import="ipaddress.IpBlock"%>
<%@ page import="login.*, util.*, common.*, common.repository.*, java.util.*, sessionmanager.*"%>
<%@ page language="java"%>
<%
	String msg = (String)request.getAttribute("msg");
	String navigator = SessionConstants.NAV_LLI_ALLOCATION_IP;
	String url = "lli/inventory/ipAddress/allocated/search";
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
	String context = "../.." + request.getContextPath();
	LoginDTO localLoginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
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
		<div class="row">
			<div class="col-md-12 " style="margin-bottom: 5px;">
				<button class="btn btn-sm btn-info pull-right"  onclick="exportTableToCSV('ip_list_table.csv')">Export To CSV</button>
			</div>
		</div>
		<form id="tableForm">
			<div class="table-responsive">
				<table id="tableData" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Client Name</th>
							<th>Entity Name</th>
							<th>Division</th>
							<th>Block Size</th>
							<th>IP Type</th>
							<th>Usage</th>
							<th>IP Range</th>
							<th>Allocation Time</th>
							<th>Expiration Time</th>
						</tr>
					</thead>
					<tbody>
					<%
					
					ArrayList<IpBlock> data = (ArrayList<IpBlock>) session.getAttribute(SessionConstants.VIEW_LLI_ALLOCATION_IP);
					if(data != null){
						for(IpBlock ipBlock: data){
							try{
							%>
								<tr>
									<td>
										<%
											if(ipBlock.getClientID() != null){
											ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(ipBlock.getClientID());
										%>	
											<%=clientDTO.getLoginName()%>																
										<%}else { %>
											N/A
										<%}%>
									</td>
									<td>
										<% 	if(ipBlock.getEntityID() != null){ %>
										<%=ipBlock.getEntityID().equals(-1L)?"Reserved for Default Gateway":ipBlock.getEntityID() %>
										<%	}else { %>
										N/A
										<% 	} %>
									</td>
									<td><%=InventoryConstants.mapOfDivisionNameToDivisionID.get(ipBlock.getDivisionID() )%> </td>
									<td><%=ipBlock.getBlockSize() %></td>
									<td><%=InventoryConstants.mapOfIPTypeNameToIPType.get(ipBlock.getIPType())%></td>
									<td><%=InventoryConstants.mapOfUsageTypeNameToUsageType.get(ipBlock.getUsageType())%></td>
									<td>
										<%=IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + 
										IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress()) %>
									</td>
									<td>
										<%if(ipBlock.getAllocationTime() != null){ %>
												<%=TimeConverter.getTimeStringByDateFormat(ipBlock.getAllocationTime(), "dd/MM/yyyy hh:mm:ss") %>	
										<%	}else { %>
												Not Allocated
										<%	} %>
									</td>
									<td>
										<%if(  ipBlock.getExpirationTime() != null){%>
												<%
													String dateString = "";
													if(ipBlock.getExpirationTime() == 0 && ipBlock.getUsageType() == InventoryConstants.USAGE_ESSENTIAL) {
														dateString = "N/A";
													}else if(ipBlock.getExpirationTime() == 0){
														dateString = "";
													}else if(ipBlock.getExpirationTime() != 0){
														dateString = TimeConverter.getTimeStringByDateFormat(ipBlock.getExpirationTime(), "dd/MM/yyyy hh:mm:ss");
													}
												%>
												<%=dateString %>	
										<%	}else { %>
												Not Given
										<%	} %>
									</td>
								</tr>
							<%	
							}catch(Exception ex){
								ex.printStackTrace();
							}
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
function downloadCSV(csv, filename) {
    var csvFile;
    var downloadLink;

    // CSV file
    csvFile = new Blob([csv], {type: "text/csv"});

    // Download link
    downloadLink = document.createElement("a");

    // File name
    downloadLink.download = filename;

    // Create a link to the file
    downloadLink.href = window.URL.createObjectURL(csvFile);

    // Hide download link
    downloadLink.style.display = "none";

    // Add the link to DOM
    document.body.appendChild(downloadLink);

    // Click download link
    downloadLink.click();
}

function exportTableToCSV(filename) {
    var csv = [];
    var rows = document.querySelectorAll("table tr");
    
    for (var i = 0; i < rows.length; i++) {
        var row = [], cols = rows[i].querySelectorAll("td, th");
        
        for (var j = 0; j < cols.length; j++) 
            row.push(cols[j].innerText);
        
        csv.push(row.join(","));        
    }

    // Download CSV file
    downloadCSV(csv.join("\n"), filename);
}	
</script>
<script type="text/javascript" src="../../scripts/util.js"></script>