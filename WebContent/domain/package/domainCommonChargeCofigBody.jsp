<%@page import="domain.DomainChargeConfigDTO"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
DomainChargeConfigDTO domainChargeConfigDTO = (DomainChargeConfigDTO) request.getAttribute("form");
System.out.println(domainChargeConfigDTO);
Integer msgType = (Integer)request.getAttribute("msg");
%>
<%if(msgType != null && msgType == -1){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Error!</strong>
</div><%}else if(msgType != null && msgType == 1){ %>
<div class="alert alert-block alert-success fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Success!</strong>
</div>
<%} %>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-cogs"></i> Domain Common Config
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form role="form" action="../../DomainChargeConfig.do?mode=domainChargeConfig" class="form-horizontal" method="post">
			<div class="form-body">
				<div class="form-group">
					<label for="categoryID" class="col-sm-4 control-label">Ownership Change Fee</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="ownershipChangeFee" value="<%=domainChargeConfigDTO.getOwnershipChangeFee() %>" required>
					</div>
				</div>
				<div class="form-group">
					<label for="categoryID" class="col-sm-4 control-label">Late Fee For Renew Within 30 Days</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="lateFeeWithin30Days" value="<%=domainChargeConfigDTO.getLateFeeWithin30Days() %>" required>
					</div>
				</div>
				<div class="form-group">
					<label for="categoryID" class="col-sm-4 control-label">Late Fee For Renew Within 90 Days</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="lateFeeWithin90Days" value="<%=domainChargeConfigDTO.getLateFeeWithin90Days() %>" required>
					</div>
				</div>
				
				<div class="form-group">
					<label for="categoryID" class="col-sm-4 control-label">Minimun Renewal Time In Days</label>	
					<div class="col-sm-6">
						<input type="text" class="form-control" name="minimunRenewalTimeInDays" value="<%=domainChargeConfigDTO.getMinimunRenewalTimeInDays() %>" required>
					</div>
				</div>
			</div>
			<div class="form-actions">
				<div class="row">
					<div class="col-md-offset-4 col-md-8"><input
							type="submit" class="btn btn-btcl btn-submit-btcl" value="Update Configuration">
					</div>
				</div>

			</div>
		</form>
	</div>
</div>