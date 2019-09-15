<%@page import="inventory.InventoryConstants"%>
<%@page import="ipaddress.IpBlock"%>
<%@page import="util.TimeConverter" %>
<%
	IpBlock ipBlock = (IpBlock) request.getAttribute("ipBlock");
	boolean isIPBlockNULL = (ipBlock == null) ? true : false;
	String actionName = (ipBlock == null) ? "Add" : "Update";
	
	
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	
	
	<div class="portlet-body form">
		<form id = "tableForm" class="form-horizontal">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">Starting IP Address</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="startingIPAddress" 
						name="startingIPAddressStr" value="${ipBlock.getIPV4StringFromLong() }"required>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Block Size</label>
					<div class="col-sm-6">
						<input type="number" class="form-control" id="blockSize" value="${ipBlock.blockSize }"
						name="blockSize" required>
						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Division</label>
					<div class="col-sm-6">
					
						<select class="form-control" name="divisionID" id="divisionID" >
							<%if(isIPBlockNULL){%>
								<option value="" selected>Select a division</option>
							<%}
							for(Integer id: InventoryConstants.mapOfDivisionNameToDivisionID.keySet()){
							%>
								<option value="<%=id%>" <%=!isIPBlockNULL?((ipBlock.getDivisionID()==id)?"selected":""):"" %>><%=InventoryConstants.mapOfDivisionNameToDivisionID.get(id) %></option>
							<%}%>
						</select>
					</div>
				</div>
<!-- 				<div class=form-group> -->
<!-- 					<label class="col-sm-3 control-label">Type</label> -->
<!-- 					<div class="col-sm-6"> -->
<!-- 						 <select class=form-control name=IPType> -->
<%-- 							 <%if(isIPBlockNULL){%> --%>
<!-- 							 	<option value="" selected>Select an IP Type </option> -->
<%-- 							 <%} --%>
<!-- 							 	for(int i=1;i<=2;i++) { //1->public 2->private -->
<!-- 							 %> -->
<%-- 							 <option value=<%=i %> <%=! isIPBlockNULL?((ipBlock.getIPType() == i)?"selected":""):"" %>> --%>
<%-- 							 	<%=InventoryConstants.mapOfIPTypeNameToIPType.get(i) %> --%>
<!-- 							 </option> -->
<%-- 							<%} %> --%>
<!-- 						 </select> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<div class=form-group>
					<label class="col-sm-3 control-label">Usage</label>
					<div class="col-sm-6">
						 <select class=form-control name=usageType>
						 <%if(isIPBlockNULL){ %>
						 	<option value="" <%=isIPBlockNULL?"selected":"" %> >Select an Usage Type </option>
						 <%}
						 	for(int i=1;i<=2;i++) {//1->essential, 2->additional
						 %>
						 	<option value=<%=i %> <%=! isIPBlockNULL?((ipBlock.getUsageType() == i)?"selected":""):"" %>>
						 		<%=InventoryConstants.mapOfUsageTypeNameToUsageType.get(i) %>
						 	</option>
						 	<%} %>
						 </select>
					</div>
				</div>
			</div>	
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset" id="reset-btn">Reset</button>
				<button class="btn btn-submit-btcl" type="submit" id="action-btn"><%=actionName %></button>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="../../scripts/util.js"></script>
<script>
$(document).ready(function(){
	
	var actionBtn = $('#action-btn');
	var resetBtn = $('#reset-btn');
	var form = $('#tableForm');
	actionBtn.click(function(){
		var formData = form.serialize();
		<%
			if(ipBlock == null){
		%>
				var url = context + "lli/inventory/ipAddress/insertIPAddress.do";
		<%		
			}else {
		%>
				var url = context + "lli/inventory/ipAddress/update.do";
				
				formData= formData+ '&blockID='+"<%=ipBlock.getIpBlockID()%>";
		<%	}
		%>
		
		LOG(formData + " " + url );
		callAjax(url, formData, function(data){
			if(data.responseCode == 1){
				toastr.success(data.msg);
				
				
		        var redirectTime = 1000;
		        setTimeout(function ()
		        {
		            window.location.reload();
		        }, redirectTime);
				
			}else {
				toastr.error(data.msg);
			}
		}, "POST");
		return false;
	});
	resetBtn.click(function(){
		form.trigger('reset');
		return false;
	});
});

</script>