
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainNameDTO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ModuleConstants"%>
<div class="row">
	<!-- Domain Search -->
	<div class="col-md-12">
		<div class="search-bar bordered">
			<div class="row">
				<div class="col-md-offset-1 col-md-10" style="padding-top: 20px; padding-bottom: 20px;">
						<input type="hidden" id="actionType" value="1"/>
						<%
							if (loginDTO.getIsAdmin()) {
						%>
						<table class="table table-bordered table-checkable no-footer">
							<%
								if(domainClientID != null)
								{
									ClientDetailsDTO clientDetailsDTO =  AllClientRepository.getInstance().getVpnClientByClientID(domainClientID, ModuleConstants.Module_ID_DOMAIN);
							%>
								<tr>
									<td><label class="control-label" style="padding-top: 3px;">Client
											Name<span class="required" aria-required="true"> * </span>
									</label></td>
									<td>
										<div class="form-group" style="margin-bottom: 0px;">
											<input id="clientIdStr" type="text" class="form-control"
												placeholder="Type to search client" name="clientIdStr" value="<%=clientDetailsDTO.getLoginName() %>">
											<input id="clientId" type="hidden" class="form-control"
												name="domainClientID" value="<%=domainClientID %>">
										</div>
									</td>
								</tr>
							<%
								}else{
							%>
							<tr>
								<td><label class="control-label" style="padding-top: 3px;">
									Client Name
								<span class="required" aria-required="true"> * </span>
								</label></td>
								<td>
									<div class="form-group" style="margin-bottom: 0px;">
										<input id="clientIdStr" type="text" class="form-control"
											placeholder="Type to search client" name="clientIdStr">
										<input id="clientId" type="hidden" class="form-control"
											name="domainClientID">
									</div>
								</td>
							</tr>
							<% } %>
						</table>
						<%
							}
						%>
						<div class="" style="padding-bottom: 5px; padding-left: 5px;">
							<div class="domain-validation-message has-error"
								style="position: relative; top: 0px;">Please write a
								domain name without www.</div>
						</div>
						<%
						if(domainDTO != null){
							domainName = domainDTO.getDomainAddress();
							if(domainName.endsWith(DomainDTO.TLD_TYPE_BANGLA)){
								domainExt = DomainNameDTO.BANGLA_EXT+"";
							}else{
								domainExt = DomainNameDTO.BD_EXT+"";
							}
							
							int lastIndexOfDot = domainName.lastIndexOf(".");
							domainName = domainName.substring(0, lastIndexOfDot);
						}
						%>
						<div class="input-group  my-group">
							<input id="domainName" type="text" name="domainName"
								class="form-control" style="width: 80%;" data-placement="bottom"
								data-toggle="popover" data-container="body" data-html="true"
								placeholder="Search for Domain"
								title="Please use bangla keyboard for .বাংলা   or use suggested second level domains for .bd   and write a domain name without www." <%if(domainName != null){ %>value="<%=domainName%>"<%} %>
								>
							<select id="domainExt" name="domainExt" style="width: 20%;"
								class="selectpicker form-control" data-live-search="true"
								title="Please select extension">
								<%
									if (DomainDTO.isDotBdAvailable) {
								%>
								<option value="1" <%if(domainExt.equals("1")){%>selected<%}%>>.bd</option>
								<%
									}
								%>
								<option value="2" <%if(domainExt.equals("2")){%>selected<%}%> ><%=DomainDTO.TLD_TYPE_BANGLA %></option>
							</select> <span class="input-group-btn">
								<button name="searchDomain" value="yes" class="btn blue uppercase bold  my-group-button searchRequest" id="checkDomain" type="submit"  >Search</button>
							</span>
						</div>
					<br>
					<hr>
				</div>
				<br> <br>
			</div>
		</div>
	</div>
	<div id="popover-content" class="col-md-12 bangla-keyboard hide">
		<%@ include file="../../../keyboard/amarBangla.jsp"%>
	</div>
</div>