<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<% Integer moduleIDForLocal = Integer.parseInt(request.getParameter("moduleID")); %>
<div class="portlet light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tv"></i>Display
		</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;">
		<div class="form-body">
		
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="1"></span>ID</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.clientID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Username</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Username" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="3"></span>Current Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.currentStatus" value="Status" disabled
					data-operator="in" data-comment="select" data-values="Not Active:0,Approved:1,Processing:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<%--<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="4"></span>ID Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.identityType" value="ID Type" disabled
					data-operator="eq" data-comment="select" data-values="NID:<%=moduleIDForLocal*ModuleConstants.MULTIPLIER + FileTypeConstants.NID_SUFFIX %>,Passport:<%=moduleIDForLocal*ModuleConstants.MULTIPLIER + FileTypeConstants.PASSPORT_SUFFIX  %>,Trade Licence:<%=moduleIDForLocal*ModuleConstants.MULTIPLIER + FileTypeConstants.TRADE_SUFFIX%>">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>--%>
			
			
			
			<%--<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>ID No.</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.identityNo" value="ID No" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			--%>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Registrant Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.registrantType" value="Registrant Type" disabled
					data-operator="eq" data-comment="select"  data-values="Govt./Semi-Govt./Autonomous:1,Military:2,Private:3,Foreign:4,Others:5">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Category Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.clientCategoryType" value="Category Type" disabled
					data-operator="eq" data-comment="select"  data-values="Individual:1,Org/Institution/Company:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>Mobile</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.phoneNumber" value="Mobile" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="11"></span>Email</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.email" value="Email" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="12"></span>Organization</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.organization" value="Organization" disabled
					data-operator="like" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="13"></span>Occupation</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.occupation" value="Occupation" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="14"></span>Country</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.country" value="Country" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="15"></span>City</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.city" value="City" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
		</div>
	</div>
	<!-- /.box-body -->
</div>
