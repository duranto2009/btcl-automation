<div class="table-responsive">
	<table class="table table-bordered table-striped custom">
		<thead>
			<tr>
				<th class="text-center" colspan="4"><h3>
						<b><%=domainDTO.getDomainAddress() %></b>'s Information
					</h3></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th scope="row" width="50%">Domain</th>
				<td><%=domainDTO.getDomainAddress() %></td>
			</tr>
			<tr>
				<th scope="row">Client</th>
				<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
			</tr>
			<tr>
				<th scope="row">Domain Status</th>
				<td><%=commonDAO.getActivationStatusName(domainDTO.getCurrentStatus()) %></td>
			</tr>
			<tr>
				<th scope="row">Activation Date</th>
				<td>
					<%if(domainDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getActivationDate()) %>
					<%}else{ %>N/A<%} %>
				</td>
			</tr>
			<tr>
				<th scope="row">Expire Date</th>
				<td>
					<%if(domainDTO.getExpiryDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getExpiryDate()) %>
					<%}else{ %>N/A<%} %>
				</td>
			</tr>
		</tbody>
	</table>
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th colspan="4"><h3>Server Info</h3></th>
			</tr>
		</thead>

		<tbody>
			<tr>
				<th scope="row" width="15%">Primary DNS</th>
				<td width="35%"><%=domainDTO.getPrimaryDNS()%></td>
				<th scope="row" width="15%">Primary IP</th>
				<td width="35%"><%=domainDTO.getPrimaryDnsIP()%></td>
			</tr>
			<tr>
				<th scope="row">Secondary DNS</th>
				<td><%=domainDTO.getSecondaryDNS()%></td>
				<th scope="row">Secondary IP</th>
				<td><%=domainDTO.getSecondaryDnsIP()%></td>
			</tr>
			<tr>
				<th scope="row">Tertiary DNS</th>
				<td><%=domainDTO.getTertiaryDNS()%></td>
				<th scope="row">Tertiary IP</th>
				<td><%=domainDTO.getTertiaryDnsIP()%></td>
			</tr>
		</tbody>
	</table>
</div>